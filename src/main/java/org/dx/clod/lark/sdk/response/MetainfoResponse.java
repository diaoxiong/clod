package org.dx.clod.lark.sdk.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import org.dx.clod.lark.sdk.model.Sheet;

/**
 * @author lizifeng
 * @date 2021/12/21 16:36
 */
@Data
public class MetainfoResponse extends CommonResponse {

    @JSONField(name = "data")
    private MetainfoResponseData data;

    @Data
    public static class MetainfoResponseData {
        @JSONField(name = "sheets")
        private Sheet[] sheets;
    }
}
