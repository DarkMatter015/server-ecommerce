package br.edu.utfpr.pb.ecommerce.server_ecommerce;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

//@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@SpringBootApplication
@EnableFeignClients
@EnableRabbit
@EnableAsync
public class ServerEcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerEcommerceApplication.class, args);
	}

}
