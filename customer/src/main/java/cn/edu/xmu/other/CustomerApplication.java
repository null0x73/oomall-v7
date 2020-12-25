package cn.edu.xmu.other;

import cn.edu.xmu.goods.client.IGoodsService;
import cn.edu.xmu.ooad.util.JwtHelper;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cn.edu.xmu.ooad","cn.edu.xmu.other"})
public class CustomerApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class, args);
    }

    @DubboReference(version = "0.0.1-SNAPSHOT")
    IGoodsService goodsService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

//        System.out.println(goodsService.getSku(279L));
//        System.out.println(goodsService.getSimpleSpuById(279L));
//        System.out.println(goodsService.getPrice(279L));

    }

}
