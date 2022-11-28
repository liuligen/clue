package com.thoughtworks.clue.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TableColumns {
    Long tableObjectId;
    Long id;
    String field;
    String type;
    String comment;
}
