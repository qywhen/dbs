package com.wisd.dbs.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author scarlet
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = false, of = "epId")
@Accessors(chain = true)
public class EndPointView extends Endpoint {
    /**
     * //1调看 2互动
     */
    private Integer mode;
    /**
     * //id别名
     */
    private Integer epId;
    /**
     * //一路视频的标识
     */
    private Integer streamId;
    /**
     * //所在直播间
     */
    private Integer roomId;

}
