package org.dx.clod.lark.sdk.accesstoken;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import org.dx.clod.core.model.Config;
import org.dx.clod.lark.sdk.request.AppAccessTokenRequest;
import org.dx.clod.lark.sdk.response.AppAccessTokenResponse;
import org.dx.clod.utils.ConfigUtil;

/**
 * @author lizifeng
 * @date 2021/12/21 15:30
 */
public class AppAccessTokenOperator {

    private static String appAccessToken;

    private static Long expireTime;

    private AppAccessTokenOperator() {}

    public static String getAuthorizationValue() {
        return "Bearer " + getAppAccessToken();
    }

    public static String getAppAccessToken() {
        if (expireTime != null && DateUtil.currentSeconds() < expireTime) {
            return appAccessToken;
        }

        Config config = ConfigUtil.getConfig();
        AppAccessTokenRequest appAccessTokenRequest = new AppAccessTokenRequest();
        appAccessTokenRequest.setAppId(config.getAppId());
        appAccessTokenRequest.setAppSecret(config.getAppSecret());

        String response = HttpUtil.post("https://open.feishu.cn/open-apis/auth/v3/app_access_token/internal/", JSON.toJSONString(appAccessTokenRequest));
        AppAccessTokenResponse appAccessTokenResponse = JSONUtil.toBean(response, AppAccessTokenResponse.class);
        if (appAccessTokenResponse.getCode() != 0) {
            throw new RuntimeException(String.format("getAppAccessToken error, appAccessTokenResponse=[%s]", JSONUtil.toJsonStr(appAccessTokenResponse)));
        }
        expireTime = DateUtil.currentSeconds() + appAccessTokenResponse.getExpire();
        appAccessToken = appAccessTokenResponse.getAppAccessToken();
        return appAccessToken;
    }
}
