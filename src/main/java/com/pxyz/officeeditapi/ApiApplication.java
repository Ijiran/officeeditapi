package com.pxyz.officeeditapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.ws.rs.core.Application;

/**
 * @author Ijiran
 * @Package com.pxyz.officeeditapi
 * @date 2020-07-04 15:59
 */
@MapperScan("com.pxyz.officeeditapi.mapper")
@SpringBootApplication
public class ApiApplication extends Application {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

}

