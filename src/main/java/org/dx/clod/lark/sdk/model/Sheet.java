package org.dx.clod.lark.sdk.model;

import lombok.Data;

/**
 * @author lizifeng
 * @date 2021/12/21 16:39
 */
@Data
public class Sheet {

    private String title;

    private String sheetId;

    private Integer index;

    private Integer columnCount;

    private Integer rowCount;

    private Integer frozenColCount;

    private Integer frozenRowCount;
}
