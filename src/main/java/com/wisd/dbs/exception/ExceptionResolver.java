package com.wisd.dbs.exception;

import com.wisd.dbs.bean.Response;
import com.wisd.dbs.bean.ReturnCode;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Created with IntelliJ IDEA.
 *
 * @author scarlet* @date 2018-11-22
 * @time 14:52
 */
@RestControllerAdvice
@Slf4j
public class ExceptionResolver {
    @ExceptionHandler(Exception.class)
    Response exceptionHandler(Exception exception) {
        if (exception instanceof MyException) {
            MyException exception1 = (MyException) exception;
            log.warn(exception1.getReturnCode() + "--" + exception1.getMessage());
            return Response.build(exception1);
        }
        if (exception instanceof MethodArgumentNotValidException ||
            exception instanceof ConstraintViolationException) {
            log.warn(exception.getMessage());
            return Response.build(ReturnCode.invalidParam);
        }
        String ex = logExceptionStackTrace(exception);
        log.error("exception happened:{}", ex);

        return Response.err();
    }

    @SneakyThrows
    public static String logExceptionStackTrace(Exception exception) {
        @Cleanup
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        @Cleanup
        PrintStream printStream = new PrintStream(baos);
        exception.printStackTrace(printStream);
        return baos.toString();
    }
}
