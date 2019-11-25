package com.wisd.dbs.exception;

import com.wisd.dbs.bean.ReturnCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet
 * @date 2018-12-05
 * @time 11:25
 */
@Setter
@Getter
@AllArgsConstructor
public class MyException extends RuntimeException {
    private ReturnCode returnCode;
}
