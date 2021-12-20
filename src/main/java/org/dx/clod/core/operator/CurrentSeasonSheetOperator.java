package org.dx.clod.core.operator;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import org.apache.commons.lang3.StringUtils;
import org.dx.clod.core.model.SpreadsheetUnit;
import org.dx.clod.lark.sdk.model.ValueRange;
import org.dx.clod.lark.sdk.response.ValuesBatchGetResponse;
import org.dx.clod.utils.ExcelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lizifeng
 * @date 2021/12/27 13:48
 */
public class CurrentSeasonSheetOperator extends AbstractSheetOperator {

    private final Logger logger = LoggerFactory.getLogger(CurrentSeasonSheetOperator.class);

    public CurrentSeasonSheetOperator(String spreadsheetToken, String sheetName) {
        super(spreadsheetToken, sheetName);
    }

    public void addInheritColumn() {
        spreadsheetsOperator.addInheritColumn(sheet);

        refreshSheetInfo();
    }

    public Map<String, Integer> getNameIndexMap() {
        int end = sheet.getRowCount() - 1;
        String range = buildSingleColRange("A", 2, end);
        ValuesBatchGetResponse valuesBatchGetResponse = spreadsheetsOperator.valuesBatchGet(range);

        ValueRange valueRange = valuesBatchGetResponse.getData().getValueRanges()[0];
        Object[][] values = valueRange.getValues();
        Map<String, Integer> map = new HashMap<>(16);
        for (int i = 0; i < values.length; i++) {
            map.put((String) values[i][0], i);
        }
        return map;
    }

    public Object[][] convertToObjectArray(Object[][] currentSettleResult, Map<String, Integer> nameIndexMap) {
        logger.debug("currentSettleResult=[{}]", JSONUtil.toJsonStr(currentSettleResult));
        logger.debug("nameIndexMap=[{}]", JSONUtil.toJsonStr(nameIndexMap));
        Object[][] param = new Object[nameIndexMap.size() + 2][1];
        Arrays.fill(param, new Object[]{"-"});

        param[0] = new Object[]{DateUtil.format(new Date(), "MM.dd")};
        for (Object[] objects : currentSettleResult) {
            String name = (String) objects[0];
            if (StringUtils.isBlank(name)) {
                continue;
            }

            Integer index = nameIndexMap.get(name);
            if (index == null) {
                index = addPlayer(name) - 2;
                param = Arrays.copyOfRange(param, 0, param.length + 1);
            }
            param[index + 1] = new Object[]{objects[2]};
        }

        Map<String, String> formulaUnit = new HashMap<>(4);
        formulaUnit.put("type", "formula");
        String columnStr = ExcelUtil.excelColIndexToStr(sheet.getColumnCount());
        formulaUnit.put("text", String.format("=SUM(%s%d:%1$s%d)", columnStr, 2, param.length - 1));
        param[param.length - 1] = new Object[]{formulaUnit};

        logger.debug("param=[{}]", JSONUtil.toJsonStr(param));
        return param;
    }

    public int addPlayer(String playerName) {
        spreadsheetsOperator.insertRow(sheet, sheet.getRowCount() - 1, sheet.getRowCount());
        String range = buildSingleRowRange(sheet.getRowCount(), 1, sheet.getColumnCount());

        Map<String, String> formulaUnit = new HashMap<>(4);
        formulaUnit.put("type", "formula");
        formulaUnit.put("text", String.format("=SUM(C%d:AAA%<d)", sheet.getRowCount()));

        Object[][] param = new Object[1][sheet.getColumnCount()];
        Arrays.fill(param[0], "-");
        param[0][0] = playerName;
        param[0][1] = formulaUnit;
        spreadsheetsOperator.values(range, param);

        refreshSheetInfo();
        return sheet.getRowCount() - 1;
    }

    public void fulfillLastColumn(Object[][] param) {
        String range = buildSingleColRange(sheet.getColumnCount(), 1, sheet.getRowCount());
        spreadsheetsOperator.values(range, param);

        refreshSheetInfo();
    }

    public void reorderCurrentSeason() {
        int end = sheet.getRowCount() - 1;
        SpreadsheetUnit startUnit = new SpreadsheetUnit(1, 2);
        SpreadsheetUnit endUnit = new SpreadsheetUnit(sheet.getColumnCount(), end);
        String range = buildRange(startUnit, endUnit);
        ValuesBatchGetResponse valuesBatchGetResponse = spreadsheetsOperator.valuesBatchGet(range, "FormattedValue");
        Object[][] values = valuesBatchGetResponse.getData().getValueRanges()[0].getValues();
        Arrays.sort(values, (value1, value2) -> (int) value2[1] - (int) value1[1]);

        Object[][] nameParam = new Object[values.length][];
        for (int i = 0; i < values.length; i++) {
            nameParam[i] = new Object[]{values[i][0]};
        }
        range = buildSingleColRange(1, 2, end);
        spreadsheetsOperator.values(range, nameParam);

        Object[][] valuesParam = new Object[values.length][];
        for (int i = 0; i < values.length; i++) {
            valuesParam[i] = Arrays.copyOfRange(values[i], 2, values[i].length);
        }

        startUnit = new SpreadsheetUnit(3, 2);
        endUnit = new SpreadsheetUnit(sheet.getColumnCount(), end);
        range = buildRange(startUnit, endUnit);
        spreadsheetsOperator.values(range, valuesParam);

        refreshSheetInfo();
    }
}
