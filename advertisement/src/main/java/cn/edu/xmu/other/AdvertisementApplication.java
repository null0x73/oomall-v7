package cn.edu.xmu.other;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.ooad","cn.edu.xmu.other"})
public class AdvertisementApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdvertisementApplication.class, args);
    }

}
