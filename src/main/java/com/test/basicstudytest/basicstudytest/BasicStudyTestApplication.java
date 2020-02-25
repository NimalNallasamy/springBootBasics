package com.test.basicstudytest.basicstudytest;

import com.test.basicstudytest.basicstudytest.configuration.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * We’ll need to enable configuration properties by adding the @EnableConfigurationProperties annotation.
 * */

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class BasicStudyTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasicStudyTestApplication.class, args);
	}

}

