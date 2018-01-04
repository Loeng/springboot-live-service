package com.sgc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.sgc.comm.filter.JwtInfo;
import com.sgc.comm.util.JedisClusterUtils;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Slf4j
@SpringBootApplication
@EnableSwagger2
@Import(JedisClusterUtils.class)
@EnableConfigurationProperties(JwtInfo.class)
@EnableTransactionManagement
public class LiveApplication {

	public static void main(String[] args) {
		Environment env = SpringApplication.run(LiveApplication.class, args).getEnvironment();
		log.info(
				"\n----------------------------------------------------------\n\t"
						+ "Application '{}' is running! Access URLs:\n\t"
						+ "Local: \t\thttp://localhost:{}\n----------------------------------------------------------",
				env.getProperty("spring.application.name"), env.getProperty("server.port"));

	}
}
