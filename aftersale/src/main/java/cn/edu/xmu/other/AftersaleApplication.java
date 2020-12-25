package cn.edu.xmu.other;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.ooad","cn.edu.xmu.other"})
public class AftersaleApplication {

    public static void main(String[] args) {
        SpringApplication.run(AftersaleApplication.class, args);
    }

}
