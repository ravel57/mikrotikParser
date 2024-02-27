package ru.ravel.mikrotikparcer

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
//@EnableTransactionManagement
@EnableScheduling
class MikrotikParserApplication {

	static void main(String[] args) {
		SpringApplication.run(MikrotikParserApplication, args)
	}

}
