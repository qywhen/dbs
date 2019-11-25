package com.wisd.dbs.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/7/24 14:37
 */
@ConfigurationProperties(prefix = "live")
@Component
@Data
public class LiveProperties {
    private String urlprefix;
    private String urlsuffix;
    private int earlyStartMinute;
    private String documentUrlPrefix;
    private Integer period=5;
    private Integer wbpServerPort;
}
