package com.lab.training;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目启动类（整个程序唯一的入口）。
 *
 * <p>@SpringBootApplication 是一个组合注解，等价于：
 * <ul>
 *     <li>@SpringBootConfiguration —— 声明这是一个配置类</li>
 *     <li>@EnableAutoConfiguration —— 开启自动装配（Spring Boot 帮你 new 好各种对象）</li>
 *     <li>@ComponentScan —— 扫描本类所在包及其子包下的 @Component / @Service / @Controller</li>
 * </ul>
 *
 * <p><b>注意：</b>因为 @ComponentScan 只扫 "com.lab.training" 及其子包，
 * 所以你的所有代码都必须放在 com.lab.training 下面，放到外面 Spring 是找不到的。
 */
@SpringBootApplication
public class TrainingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrainingApplication.class, args);
        System.out.println("""
                
                ====================================================
                  项目启动成功！
                  测试接口: http://localhost:8080/api/departments/1
                ====================================================
                """);
    }
}
