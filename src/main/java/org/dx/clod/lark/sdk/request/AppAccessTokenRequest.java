package org.dx.clod.lark.sdk.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author lizifeng
 * @date 2021/12/21 15:11
 */
@Data
public class AppAccessTokenRequest {

    @JSONField(name = "app_id")
    private String appId;

    @JSONField(name = "app_secret")
    private String appSecret;
}
