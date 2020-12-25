package cn.edu.xmu.other.gateway;

import cn.edu.xmu.ooad.util.JwtHelper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class UserGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserGatewayApplication.class, args);
	}

}
