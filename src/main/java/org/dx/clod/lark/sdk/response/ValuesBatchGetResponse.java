package org.dx.clod.lark.sdk.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.dx.clod.lark.sdk.model.ValueRange;

/**
 * @author lizifeng
 * @date 2021/12/22 10:26
 */
@Data
public class ValuesBatchGetResponse extends CommonResponse {

    @JSONField(name = "data")
    private ValuesBatchGetResponseData data;

    @Data
    public static class ValuesBatchGetResponseData {

        @JSONField(name = "valueRanges")
        private ValueRange[] valueRanges;
    }
}
