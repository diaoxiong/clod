package org.dx.clod.core.operator;

import org.dx.clod.core.model.SpreadsheetUnit;
import org.dx.clod.lark.sdk.model.Sheet;
import org.dx.clod.lark.sdk.spreadsheet.operator.SpreadsheetsOperator;

/**
 * @author lizifeng
 * @date 2021/12/27 14:09
 */
public abstract class AbstractSheetOperator {

    protected Sheet sheet;

    protected String sheetName;

    protected SpreadsheetsOperator spreadsheetsOperator;

    public AbstractSheetOperator(String spreadsheetToken, String sheetName) {
        this.spreadsheetsOperator = new SpreadsheetsOperator(spreadsheetToken);
        this.sheetName = sheetName;
        refreshSheetInfo();
    }

    public void refreshSheetInfo() {
        sheet = spreadsheetsOperator.findSheet(sheetName);
    }

    protected String buildSingleRowRange(int row, int startCol, int endCol) {
        return buildRange(new SpreadsheetUnit(startCol, row), new SpreadsheetUnit(endCol, row));
    }

    protected String buildSingleColRange(int col, int startRow, int endRow) {
        return buildRange(new SpreadsheetUnit(col, startRow), new SpreadsheetUnit(col, endRow));
    }

    protected String buildSingleColRange(String colStr, int startRow, int endRow) {
        return buildRange(new SpreadsheetUnit(colStr, startRow), new SpreadsheetUnit(colStr, endRow));
    }

    protected String buildSingleUnitRange(int col, int row) {
        return buildSingleUnitRange(new SpreadsheetUnit(col, row));
    }

    protected String buildSingleUnitRange(String colStr, int row) {
        return buildSingleUnitRange(new SpreadsheetUnit(colStr, row));
    }

    protected String buildSingleUnitRange(SpreadsheetUnit spreadsheetUnit) {
        return buildRange(spreadsheetUnit, spreadsheetUnit);
    }

    protected String buildRange(SpreadsheetUnit startUnit, SpreadsheetUnit endUnit) {
        return String.format("%s!%s%d:%s%d", sheet.getSheetId(), startUnit.getColStr(), startUnit.getRow(), endUnit.getColStr(), endUnit.getRow());
    }
}
