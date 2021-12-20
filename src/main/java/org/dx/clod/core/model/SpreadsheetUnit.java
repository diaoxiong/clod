package org.dx.clod.core.model;

import lombok.Data;
import org.dx.clod.utils.ExcelUtil;

/**
 * @author lizifeng
 * @date 2021/12/27 14:34
 */
@Data
public class SpreadsheetUnit {

    private String colStr;

    private int col;

    private int row;

    public SpreadsheetUnit(String colStr, int row) {
        this.colStr = colStr;
        this.row = row;
        this.col = ExcelUtil.excelColStrToIndex(colStr);
    }

    public SpreadsheetUnit(int col, int row) {
        this.col = col;
        this.row = row;
        this.colStr = ExcelUtil.excelColIndexToStr(col);
    }
}
