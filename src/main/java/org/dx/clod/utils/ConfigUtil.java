package org.dx.clod.utils;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;
import org.dx.clod.core.enums.ExitCodeEnum;
import org.dx.clod.core.model.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lizifeng
 * @date 2021/12/9 10:08
 */
public class ConfigUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigUtil.class);

    private static final String CONFIG_PATH = "config.json";

    private static Config config;

    static {
        try {
            String configString = ResourceUtil.readUtf8Str(CONFIG_PATH);
            config = JSONUtil.toBean(configString, Config.class);
        } catch (Exception e) {
            LOGGER.error("load config error", e);
            System.exit(ExitCodeEnum.CONFIG_ERROR.getCode());
        }
    }

    public static Config getConfig() {
        return config;
    }
}
