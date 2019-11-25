package com.wisd.dbs.websocket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @time 2019/6/14 10:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WsMessage {
    private String message;
}
