package com.jiang.springcloud.service.impl;

import cn.hutool.core.util.IdUtil;
import com.jiang.springcloud.service.PaymentService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;


@Service
public class PaymentServiceImpl implements PaymentService {


    /**
     * @MethodName: paymentInfo_OK
     * @Description: 能够正常访问的方法
     * @Param: [id]
     * @Return: java.lang.String
     * @Author: jiang
     * @Date: 2020/4/4
    **/
    @Override
    public String paymentInfo_OK(Integer id) {
        return "线程池"+Thread.currentThread().getName()+"    paymentInfo_OK,id:"+id+"\t"+"O(∩_∩)O哈哈~";
    }


    // 3秒以内正常（消费端降级处理，一般在客服端做降级出来）
    @HystrixCommand(fallbackMethod = "paymentInfo_TimeOutHandler",commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "5000")
    })
    @Override
    public String paymentInfo_TimeOut(Integer id) {
         int timeNumber = 3;
        //int age = 10/0;
        try {
            TimeUnit.SECONDS.sleep(timeNumber);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return "线程池"+Thread.currentThread().getName()+"    paymentInfo_TimeOut,id:"+id+"\t"+"O(∩_∩)O哈哈~"+"  耗时（秒）" +timeNumber;
    }

    // 服务降级  用来兜底的方法
    public String paymentInfo_TimeOutHandler(Integer id) {
        return "线程池"+Thread.currentThread().getName()+"    系统忙碌或运行报错，请稍后再试,id:"+id+"\t"+"o(╥﹏╥)o";

    }

    //=======服务熔断（次数多了，熔断，然后慢慢恢复）
    @HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback" , commandProperties = {
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),// 是否开启断路器
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),// 请求次数
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),
    })
    public String paymentCircuitBreaker(@PathVariable("id") Integer id){
            if(id < 0){
                throw new RuntimeException("****id 不能负数");
            }
            String serialNumber = IdUtil.simpleUUID();
            return Thread.currentThread().getName()+"\t"+" 调用成功，流水号 " + serialNumber;
    }

    public String paymentCircuitBreaker_fallback(@PathVariable("id") Integer id){
        return "id 不能负数。请稍后再试，/( ToT)/~~id: " +id;

    }

}
