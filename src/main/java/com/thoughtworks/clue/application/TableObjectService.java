package com.thoughtworks.clue.application;

import com.google.common.collect.ImmutableList;
import com.thoughtworks.clue.model.TableObject;
import com.thoughtworks.clue.utils.ExcelData;
import com.thoughtworks.clue.utils.ExcelDataImpl;
import com.thoughtworks.clue.utils.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@Slf4j
public class TableObjectService {

    @Resource
    private MongoTemplate mongoTemplate;

    @PostConstruct
    public void readFromExcel() {
        List<ExcelDataImpl> excelDataList = ExcelUtil.readExcel("/Users/lgliu/Downloads/all_etl.xlsx", "xlsx", convertRowToData());
        Map<String, ExcelDataImpl> mergeDuplicateData = excelDataList.stream().collect(Collectors.toMap(
                ExcelDataImpl::getTableName, Function.identity(), (a, b) -> mergeDuplicateExcelDataFunction.apply(a, b)
        ));
        List<TableObject> tableObjects = mergeDuplicateData.values().stream().map(o -> toTableObjectFunction.apply(o)).collect(Collectors.toList());

        mongoTemplate.dropCollection("tables");
        mongoTemplate.insert(tableObjects, "tables");
    }

    BiFunction<ExcelDataImpl, ExcelDataImpl, ExcelDataImpl> mergeDuplicateExcelDataFunction = (a, b) -> {
        ExcelDataImpl excelData = new ExcelDataImpl();
        excelData.setFlow(Stream.of(a.getFlow(), b.getFlow()).flatMap(Collection::stream).distinct().collect(Collectors.toList()));
        excelData.setEtlName(Stream.of(a.getEtlName(), b.getEtlName()).flatMap(Collection::stream).distinct().collect(Collectors.toList()));
        excelData.setEtlPath(Stream.of(a.getEtlPath(), b.getEtlPath()).flatMap(Collection::stream).distinct().collect(Collectors.toList()));
        excelData.setDependenceTables(Stream.of(a.getDependenceTables(), b.getDependenceTables()).flatMap(Collection::stream).distinct().collect(Collectors.toList()));
        excelData.setSchema(a.getSchema());
        excelData.setTableName(a.getTableName());
        return excelData;
    };

    Function<ExcelDataImpl, TableObject> toTableObjectFunction = excelData -> {
        return TableObject.builder()
                .etlFlow(excelData.getFlow().stream().collect(Collectors.joining(",")))
                .etlName(excelData.getEtlName().stream().collect(Collectors.joining(",")))
                .etlPath(excelData.getEtlPath().stream().collect(Collectors.joining(",")))
                .schema(excelData.getSchema())
                .tableName(excelData.getTableName())
                .dependenceTables(excelData.getDependenceTables())
                .build();
    };

    public Function convertRowToData() {
        Function<Row, ExcelData> rowExcelDataFunction = row -> {
            ExcelDataImpl excelData = new ExcelDataImpl();
            int cellNum = 1; //从第二列开始，第一列是合并列。不想写代码
            String flow = ExcelUtil.convertCellValueToString(row.getCell(cellNum++));

            if (!StringUtils.hasLength(flow)) {
                return null;
            }
            String[] flows = flow.split("\n");
            List<String> flowList = Arrays.stream(flows).filter(s -> !s.isEmpty()).collect(Collectors.toList());
            excelData.setFlow(ImmutableList.of(flowList.get(0)));

            String etlInfo = ExcelUtil.convertCellValueToString(row.getCell(cellNum++));
            if (!StringUtils.hasLength(etlInfo)) {
                return null;
            }
            String[] etlInfos = etlInfo.split("\n");
            List<String> etlInfoList = Arrays.stream(etlInfos).filter(s -> !s.isEmpty()).collect(Collectors.toList());
            excelData.setEtlName(ImmutableList.of(etlInfoList.get(0)));
            if (etlInfos.length > 1) {
                excelData.setEtlPath(ImmutableList.of(etlInfoList.get(1)));
            }

            String tableinfo = ExcelUtil.convertCellValueToString(row.getCell(cellNum++));
            if (!StringUtils.hasLength(tableinfo)) {
                return null;
            }
            String[] tableInfos = tableinfo.split("\n");
            List<String> tableInfoList = Arrays.stream(tableInfos).filter(s -> !s.isEmpty()).collect(Collectors.toList());
            if (tableInfoList.size() > 1) {
                log.error("tableinfo:{} more than one table");
                return null;
            }
            String[] schemaAndTableArray = tableinfo.split("\\.");
            if(schemaAndTableArray.length < 1) {
                return null;
            }
            List<String> schemaAndTableList = Arrays.stream(schemaAndTableArray).filter(s -> !s.isEmpty()).collect(Collectors.toList());
            if (schemaAndTableList.size() > 1) {
                excelData.setSchema(schemaAndTableList.get(0));
                excelData.setTableName(schemaAndTableList.get(1));
            } else {
                excelData.setTableName(schemaAndTableList.get(0));
            }
            String dependenceTable = ExcelUtil.convertCellValueToString(row.getCell(cellNum++));
            if (!StringUtils.hasLength(dependenceTable)) {
                return null;
            }
            List<String> dependenceTableList = Arrays.stream(dependenceTable.split("\n")).filter(s -> !s.isEmpty()).collect(Collectors.toList());
            excelData.setDependenceTables(dependenceTableList);
            return excelData;
        };
        Function<Row, ExcelData> function = rowExcelDataFunction;
        return
                function;
    }
}
