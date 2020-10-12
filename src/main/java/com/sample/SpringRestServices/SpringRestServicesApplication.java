package com.sample.SpringRestServices;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import redis.clients.jedis.Jedis;



@SpringBootApplication
public class SpringRestServicesApplication {

	public static void main(String[] args) {
		
		ApplicationContext applicationContext = 
		  SpringApplication.run(SpringRestServicesApplication.class, args);
		
		CreateRedisConnection RedisConnection = applicationContext.getBean(CreateRedisConnection.class);
		
		Jedis redisConnection = RedisConnection.createConnection();
		
		if(null != redisConnection) {
			System.out.println("Created");
			RedisConnection.closeConnection();
		}

	}
}
