package com.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.java.util.ConfigProperties;
import com.java.util.MyappProperties;

@SpringBootApplication
public class Starter {
	

	public static void main(String[] args) {
		ApplicationContext context=SpringApplication.run(Starter.class, args);
		ConfigProperties p1= context.getBean(ConfigProperties.class);
		MyappProperties  p2= context.getBean(MyappProperties.class);
		System.out.println(p1.getFirstName());
		System.out.println(p2.getFirstName());
	}

}
