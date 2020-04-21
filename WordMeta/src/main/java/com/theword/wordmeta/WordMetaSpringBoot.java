package com.theword.wordmeta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WordMetaSpringBoot {

	public static void main(String[] args) {
		SpringApplication.run(WordMetaSpringBoot.class, args);
	}
}

