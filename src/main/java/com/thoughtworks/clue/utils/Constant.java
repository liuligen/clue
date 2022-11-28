package com.thoughtworks.clue.utils;

public class Constant {
    public static final String USER_PHONE_REGEXP = "^1\\d{10}";
    public static final String PASSWORD_REGEXP = "^[a-zA-Z0-9]{6,20}$";
    public static final String THIRD_PARTY_ORGANIZATION_CODE_REGEXP = "\\d{4}";
    public static final String EXCEL_2007_REGEXP = "^.+\\.(?i)(xlsx)$";
    public static final String EMAIL_REGEXP = "^[\\.a-zA-Z0-9_-]+@[\\.a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
    public static final String AUTHORIZATION = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final int USER_REMARK_MAX_LENGTH = 20;

    public static final String SUCCESS = "CODE";
    public static final String SUCCESSMSG = "MSG";
    public static final String SUCCESSVALUE = "0000";

    public static final String XLS = "xls";
    public static final String XLSX = "xlsx";
}
