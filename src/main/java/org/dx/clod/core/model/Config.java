package org.dx.clod.core.model;

import lombok.Data;

/**
 * @author lizifeng
 * @date 2021/12/9 10:41
 */
@Data
public class Config {

    private String appId;

    private String appSecret;

    private String spreadsheetToken;

    private String calculatorSheetName;

    private String currentSeasonSheetName;
}
