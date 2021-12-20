package org.dx.clod.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import org.dx.clod.lark.sdk.accesstoken.AppAccessTokenOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lizifeng
 * @date 2021/12/21 17:21
 */
public class RequestUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestUtil.class);

    public static String get(String url) {
        String authorizationValue = AppAccessTokenOperator.getAuthorizationValue();
        LOGGER.debug("send get request, url=[{}]", url);
        String response = HttpUtil.createGet(url).header("Authorization", authorizationValue).execute().body();
        LOGGER.debug("response=[{}]", response);
        return response;
    }

    public static String post(String url, String requestBody) {
        String authorizationValue = AppAccessTokenOperator.getAuthorizationValue();
        LOGGER.debug("send post request, url=[{}]", url);
        String response = HttpUtil.createPost(url).header("Authorization", authorizationValue).body(requestBody).execute().body();
        LOGGER.debug("response=[{}]", response);
        return response;
    }

    public static String delete(String url, String requestBody) {
        String authorizationValue = AppAccessTokenOperator.getAuthorizationValue();
        LOGGER.debug("send delete request, url=[{}]", url);
        String response = HttpRequest.delete(url).header("Authorization", authorizationValue).body(requestBody).execute().body();
        LOGGER.debug("response=[{}]", response);
        return response;
    }

    public static String put(String url, String requestBody) {
        String authorizationValue = AppAccessTokenOperator.getAuthorizationValue();
        LOGGER.debug("send put request, url=[{}], requestBody=[{}]", url, requestBody);
        String response = HttpRequest.put(url).header("Authorization", authorizationValue).body(requestBody).execute().body();
        LOGGER.debug("response=[{}]", response);
        return response;
    }
}
