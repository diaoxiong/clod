package org.dx.clod.lark.sdk.spreadsheet.operator;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.dx.clod.lark.sdk.model.Dimension;
import org.dx.clod.lark.sdk.model.Sheet;
import org.dx.clod.lark.sdk.model.ValueRange;
import org.dx.clod.lark.sdk.request.DimensionRangeRequest;
import org.dx.clod.lark.sdk.request.InsertDimensionRangeRequest;
import org.dx.clod.lark.sdk.request.ValuesRequest;
import org.dx.clod.lark.sdk.response.CommonResponse;
import org.dx.clod.lark.sdk.response.MetainfoResponse;
import org.dx.clod.lark.sdk.response.ValuesBatchGetResponse;
import org.dx.clod.utils.RequestUtil;

import java.util.Arrays;

/**
 * @author lizifeng
 * @date 2021/12/21 17:03
 */
public class SpreadsheetsOperator {

    private final String spreadsheetToken;

    public SpreadsheetsOperator(String spreadsheetToken) {
        this.spreadsheetToken = spreadsheetToken;
    }

    public Sheet[] getSheetInfo() {
        String url = String.format("https://open.feishu.cn/open-apis/sheets/v2/spreadsheets/%s/metainfo", spreadsheetToken);
        String response = RequestUtil.get(url);
        MetainfoResponse metainfoResponse = JSON.parseObject(response, MetainfoResponse.class);
        if (metainfoResponse.getCode() != 0) {
            throw new RuntimeException(String.format("getSheetInfo error, metainfoResponse=[%s]", JSONUtil.toJsonStr(metainfoResponse)));
        }
        return metainfoResponse.getData().getSheets();
    }

    public Sheet findSheet(String sheetName) {
        Sheet[] sheets = getSheetInfo();
        return Arrays.stream(sheets)
                .filter(item -> sheetName.equals(item.getTitle()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(String.format("can not find sheet of %s", sheetName)));
    }

    public ValuesBatchGetResponse valuesBatchGet(String range) {
        return valuesBatchGet(range, null);
    }

    public ValuesBatchGetResponse valuesBatchGet(String range, String valueRenderOption) {
        String url = String.format("https://open.feishu.cn/open-apis/sheets/v2/spreadsheets/%s/values_batch_get?ranges=%s", spreadsheetToken, range);
        if (StringUtils.isNotBlank(valueRenderOption)) {
            url = url + "&valueRenderOption=" + valueRenderOption;
        }

        String response = RequestUtil.get(url);
        ValuesBatchGetResponse valuesBatchGetResponse = JSON.parseObject(response, ValuesBatchGetResponse.class);
        if (valuesBatchGetResponse.getCode() != 0) {
            throw new RuntimeException(String.format("valuesBatchGet error, valuesBatchGetResponse=[%s]", JSONUtil.toJsonStr(valuesBatchGetResponse)));
        }
        return valuesBatchGetResponse;
    }

    public void addInheritColumn(Sheet sheet) {
        addColumn(sheet);
        insertColumn(sheet, sheet.getColumnCount(), sheet.getColumnCount() + 1);
        deleteColumn(sheet, sheet.getColumnCount() + 2, sheet.getColumnCount() + 2);
    }

    public void addColumn(Sheet sheet) {
        String url = String.format("https://open.feishu.cn/open-apis/sheets/v2/spreadsheets/%s/dimension_range", spreadsheetToken);
        DimensionRangeRequest dimensionRangeRequest = new DimensionRangeRequest();
        Dimension dimension = new Dimension();
        dimension.setSheetId(sheet.getSheetId());
        dimension.setMajorDimension("COLUMNS");
        dimension.setLength(1);
        dimensionRangeRequest.setDimension(dimension);
        String response = RequestUtil.post(url, JSON.toJSONString(dimensionRangeRequest));
        CommonResponse commonResponse = JSON.parseObject(response, CommonResponse.class);
        if (commonResponse.getCode() != 0) {
            throw new RuntimeException(String.format("addColumn error, commonResponse=[%s]", JSONUtil.toJsonStr(commonResponse)));
        }
    }

    public void insertColumn(Sheet sheet, int start, int end) {
        insertDimensionRange(sheet, "COLUMNS", start, end);
    }

    public void insertRow(Sheet sheet, int start, int end) {
        insertDimensionRange(sheet, "ROWS", start, end);
    }

    public void insertDimensionRange(Sheet sheet, String majorDimension, int start, int end) {
        String url = String.format("https://open.feishu.cn/open-apis/sheets/v2/spreadsheets/%s/insert_dimension_range", spreadsheetToken);
        InsertDimensionRangeRequest insertDimensionRangeRequest = new InsertDimensionRangeRequest();
        Dimension dimension = new Dimension();
        dimension.setSheetId(sheet.getSheetId());
        dimension.setMajorDimension(majorDimension);
        dimension.setStartIndex(start);
        dimension.setEndIndex(end);
        insertDimensionRangeRequest.setDimension(dimension);
        insertDimensionRangeRequest.setInheritStyle("BEFORE");
        String response = RequestUtil.post(url, JSON.toJSONString(insertDimensionRangeRequest));
        CommonResponse commonResponse = JSON.parseObject(response, CommonResponse.class);
        if (commonResponse.getCode() != 0) {
            throw new RuntimeException(String.format("insertDimensionRange error, commonResponse=[%s]", JSONUtil.toJsonStr(commonResponse)));
        }
    }

    public void deleteColumn(Sheet sheet, int start, int end) {
        String url = String.format("https://open.feishu.cn/open-apis/sheets/v2/spreadsheets/%s/dimension_range", spreadsheetToken);
        DimensionRangeRequest dimensionRangeRequest = new DimensionRangeRequest();
        Dimension dimension = new Dimension();
        dimension.setSheetId(sheet.getSheetId());
        dimension.setMajorDimension("COLUMNS");
        dimension.setStartIndex(start);
        dimension.setEndIndex(end);
        dimensionRangeRequest.setDimension(dimension);
        String response = RequestUtil.delete(url, JSON.toJSONString(dimensionRangeRequest));
        CommonResponse commonResponse = JSON.parseObject(response, CommonResponse.class);
        if (commonResponse.getCode() != 0) {
            throw new RuntimeException(String.format("deleteColumn error, commonResponse=[%s]", JSONUtil.toJsonStr(commonResponse)));
        }
    }

    public void values(String range, Object[][] param) {
        String url = String.format("https://open.feishu.cn/open-apis/sheets/v2/spreadsheets/%s/values", spreadsheetToken);
        ValuesRequest valuesRequest = new ValuesRequest();
        ValueRange valueRange = new ValueRange();
        valueRange.setRange(range);
        valueRange.setValues(param);
        valuesRequest.setValueRange(valueRange);
        String response = RequestUtil.put(url, JSON.toJSONString(valuesRequest));
        CommonResponse commonResponse = JSON.parseObject(response, CommonResponse.class);
        if (commonResponse.getCode() != 0) {
            throw new RuntimeException(String.format("values error, commonResponse=[%s]", JSONUtil.toJsonStr(commonResponse)));
        }
    }
}