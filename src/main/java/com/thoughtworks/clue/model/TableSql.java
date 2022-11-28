package com.thoughtworks.clue.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TableSql {
    Long tableObjectId;
    Long id;
    String sqlFragment;
    String comment;
}
