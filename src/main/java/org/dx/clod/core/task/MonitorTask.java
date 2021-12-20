package org.dx.clod.core.task;

import org.dx.clod.core.enums.ExitCodeEnum;
import org.dx.clod.core.operator.CalculatorSheetOperator;
import org.dx.clod.core.operator.CurrentSeasonSheetOperator;
import org.dx.clod.utils.ConfigUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author lizifeng
 * @date 2021/12/9 10:10
 */
public class MonitorTask implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(MonitorTask.class);

    @Override
    public void run() {
        logger.info("monitorTask is running...");
        try {
            CalculatorSheetOperator calculatorSheetOperator = new CalculatorSheetOperator(ConfigUtil.getConfig().getSpreadsheetToken(), ConfigUtil.getConfig().getCalculatorSheetName());
            if (calculatorSheetOperator.isNeedSettle()) {
                CurrentSeasonSheetOperator currentSeasonSheetOperator = new CurrentSeasonSheetOperator(ConfigUtil.getConfig().getSpreadsheetToken(), ConfigUtil.getConfig().getCurrentSeasonSheetName());
                currentSeasonSheetOperator.addInheritColumn();
                Map<String, Integer> nameIndexMap = currentSeasonSheetOperator.getNameIndexMap();
                Object[][] currentSettleResult = calculatorSheetOperator.getCurrentSettleResult();
                Object[][] param = currentSeasonSheetOperator.convertToObjectArray(currentSettleResult, nameIndexMap);
                currentSeasonSheetOperator.fulfillLastColumn(param);
                currentSeasonSheetOperator.reorderCurrentSeason();
                calculatorSheetOperator.changeCalculatorStatus();
            }

            if (calculatorSheetOperator.isNeedReset()) {
                calculatorSheetOperator.resetCalculator();
            }
        } catch (Exception e) {
            logger.error("monitorTask run error", e);
            System.exit(ExitCodeEnum.EXCEPTION_ERROR.getCode());
        }
    }
}
