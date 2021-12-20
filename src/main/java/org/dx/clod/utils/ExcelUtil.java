package org.dx.clod.utils;

/**
 * @author lizifeng
 * @date 2021/12/23 10:54
 */
public class ExcelUtil {

    public static String excelColIndexToStr(int columnIndex) {
        if (columnIndex <= 0) {
            throw new RuntimeException("columnIndex should larger than 0");
        }

        StringBuilder stringBuilder = new StringBuilder();

        while (columnIndex > 0) {
            columnIndex--;
            char c = (char) ((columnIndex % 26) + 'A');
            stringBuilder.insert(0, c);
            columnIndex = columnIndex / 26;
        }

        return stringBuilder.toString();
    }

    public static int excelColStrToIndex(String colStr) {
        return cn.hutool.poi.excel.ExcelUtil.colNameToIndex(colStr) + 1;
    }
}
