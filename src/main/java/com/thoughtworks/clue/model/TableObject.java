package com.thoughtworks.clue.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TableObject {
    @MongoId
    String id;
    String tag;
    String name;
    String etlName;
    String etlPath;
    String schema;
    String tableName;
    List<String> dependenceTables;
    List<TableObject> dependenceTableObject;
    List<String> correlationTables;
    List<TableObject> correlationTableObject;
    String etlFlow;
    String upstream;
    String tableType;
    String stratum;
    String tableSql;



}
