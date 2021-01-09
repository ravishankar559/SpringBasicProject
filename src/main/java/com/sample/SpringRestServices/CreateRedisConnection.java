package com.sample.SpringRestServices;

import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.Jedis;


public class CreateRedisConnection {
    
	private Jedis redisConnection;
    
	@Value("${RedisHost}")
	private String redisHost;
	
	@Value("${RedisPort}")
	private int redisPort;
	
	@Value("${RedisPassword}")
	private String redisPassword;
		
	public Jedis createConnection(){
		try {
			this.redisConnection = new Jedis(redisHost, redisPort);
			System.out.println(redisConnection.ping());
			return redisConnection;
		}catch(Exception e) {
		    return null;	
		}
	}
	
	public void closeConnection() {
		if(null != this.redisConnection) {
			this.redisConnection.close();
		}
	}
}
