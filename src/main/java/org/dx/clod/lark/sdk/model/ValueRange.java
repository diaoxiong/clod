package org.dx.clod.lark.sdk.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author lizifeng
 * @date 2021/12/22 17:38
 */
@Data
public class ValueRange {

    private String range;

    @JSONField(name = "values")
    private Object[][] values;
}
