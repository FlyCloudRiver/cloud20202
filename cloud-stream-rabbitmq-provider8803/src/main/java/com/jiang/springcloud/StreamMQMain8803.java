package com.jiang.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @ClassName StreamMQMain8803
 * @Description: TODO
 * @Author Administrator
 * @Date 2020/9/29
 * @Version V1.0
 **/
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class StreamMQMain8803 {

    public static void main(String[] args) {
        SpringApplication.run(StreamMQMain8803.class,args);
    }
}
