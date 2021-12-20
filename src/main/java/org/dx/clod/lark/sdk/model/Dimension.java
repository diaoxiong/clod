package org.dx.clod.lark.sdk.model;

import lombok.Data;

/**
 * @author lizifeng
 * @date 2021/12/22 17:13
 */
@Data
public class Dimension {

    private String sheetId;

    private String majorDimension;

    private Integer startIndex;

    private Integer endIndex;

    private Integer length;
}
