package com.books.integrate.spring.react;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootReactMysqlApplication {

	public static void main(String[] args) {
            try{
                SpringApplication.run(SpringBootReactMysqlApplication.class, args);
            } catch (Throwable e){
                e.printStackTrace();
            }
	
	}

}
