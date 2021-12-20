package org.dx.clod.lark.sdk.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lizifeng
 * @date 2021/12/21 15:14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppAccessTokenResponse extends CommonResponse {

    @JSONField(name = "app_access_token")
    private String appAccessToken;

    @JSONField(name = "tenant_access_token")
    private String tenantAccessToken;

    private Integer expire;
}
