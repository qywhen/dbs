package com.wisd.dbs;

import com.google.gson.Gson;
import com.wisd.dbs.bean.SharedPool;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.LockSupport;

//@RunWith(SpringRunner.class)
//@SpringBootTest
@Slf4j
public class DbsApplicationTests {
    @Autowired
    StringEncryptor encryptor;

    @Test
    public void testJasypt() {
        final val url = encryptor.encrypt(
                "jdbc:mysql://192.168.20.51:3306/wd_educate?serverTimezone=UTC&useUnicode" +
                "=true&characterEncoding=utf8&useSSL=false&allowPublicKeyRetrieval=true");
        final val username = encryptor.encrypt("edu");
        final val password = encryptor.encrypt("wisdom");
        System.out.println("url:" + url);
        System.out.println("username:" + username);
        System.out.println("password:" + password);
    }


}