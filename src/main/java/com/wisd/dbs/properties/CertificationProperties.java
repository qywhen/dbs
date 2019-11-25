package com.wisd.dbs.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/7/4 9:14
 */
@ConfigurationProperties(prefix = "certification")
@Component
@Data
public class CertificationProperties {
    private String serverUrl;
}
