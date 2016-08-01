package cn.larry.demo.dubbo.consumer;

import cn.larry.demo.dubbo.DemoService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fugz on 2016/4/12.
 */
public class Consumer {
    public static void mains(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring/consumer-context.xml");
        context.start();

        DemoService demoService = (DemoService)context.getBean("demoService"); // 获取远程服务代理
        String hello = demoService.sayHello("world"); // 执行远程方法

        System.out.println( hello ); // 显示调用结果
    }

    public static void main(String[] args) {
        List<Number> nums = new ArrayList<>();
        nums.add(12);
        long l = 12;
        nums.contains(l);
        System.out.println(nums.contains(l));
    }

}
