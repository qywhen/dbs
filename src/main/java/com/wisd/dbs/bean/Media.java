package com.wisd.dbs.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Transient;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/18 21:18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Media {
    @Transient
    private Integer id;
    private String ip;
    private Integer port;
}
