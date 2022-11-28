package com.thoughtworks.clue.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExcelDataImpl implements ExcelData {
    private List<String> flow;
    private List<String> etlName;
    private List<String> etlPath;
    private String schema;
    private String tableName;
    private List<String> dependenceTables;
}
