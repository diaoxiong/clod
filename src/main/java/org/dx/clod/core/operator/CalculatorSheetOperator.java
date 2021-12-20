package org.dx.clod.core.operator;

import org.dx.clod.core.model.SpreadsheetUnit;
import org.dx.clod.lark.sdk.model.ValueRange;
import org.dx.clod.lark.sdk.response.ValuesBatchGetResponse;

import java.util.Arrays;

/**
 * @author lizifeng
 * @date 2021/12/27 13:48
 */
public class CalculatorSheetOperator extends AbstractSheetOperator {

    private static final String CN_YES = "是";
    private static final String CN_NO = "否";
    private static final String CN_FINISH_SETTLE = "已完成结算";

    public CalculatorSheetOperator(String spreadsheetToken, String sheetName) {
        super(spreadsheetToken, sheetName);
    }

    public boolean isNeedSettle() {
        Integer rowCount = sheet.getRowCount();
        int row = rowCount - 1;
        String range = buildSingleUnitRange("B", row);
        ValuesBatchGetResponse valuesBatchGetResponse = spreadsheetsOperator.valuesBatchGet(range);
        ValueRange valueRange = valuesBatchGetResponse.getData().getValueRanges()[0];

        return CN_YES.equals(valueRange.getValues()[0][0]);
    }

    public boolean isNeedReset() {
        String range = buildSingleUnitRange("B", sheet.getRowCount());
        ValuesBatchGetResponse valuesBatchGetResponse = spreadsheetsOperator.valuesBatchGet(range);
        ValueRange valueRange = valuesBatchGetResponse.getData().getValueRanges()[0];

        return CN_YES.equals(valueRange.getValues()[0][0]);
    }

    public Object[][] getCurrentSettleResult() {
        int end = sheet.getRowCount() - 3;
        SpreadsheetUnit startUnit = new SpreadsheetUnit("A", 4);
        SpreadsheetUnit endUnit = new SpreadsheetUnit("C", end);
        String range = buildRange(startUnit, endUnit);
        ValuesBatchGetResponse valuesBatchGetResponse = spreadsheetsOperator.valuesBatchGet(range, "FormattedValue");
        ValueRange valueRange = valuesBatchGetResponse.getData().getValueRanges()[0];
        return valueRange.getValues();
    }

    public void changeCalculatorStatus() {
        String range = buildSingleUnitRange(2, sheet.getRowCount() - 1);
        spreadsheetsOperator.values(range, new Object[][]{{CN_FINISH_SETTLE}});
        refreshSheetInfo();
    }

    public void resetCalculator() {
        String range = buildSingleColRange(2, 4, sheet.getRowCount() - 3);

        Object[][] param = new Object[sheet.getRowCount() - 6][];
        Arrays.fill(param, new Object[]{"-"});
        spreadsheetsOperator.values(range, param);

        range = buildSingleColRange(4, 4, sheet.getRowCount() - 3);
        spreadsheetsOperator.values(range, param);

        range = buildSingleColRange(2, sheet.getRowCount() - 1, sheet.getRowCount());
        spreadsheetsOperator.values(range, new Object[][]{{CN_NO}, {CN_NO}});
        refreshSheetInfo();
    }
}
