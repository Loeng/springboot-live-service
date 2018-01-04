package com.sgc.comm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
public class RedisConfig {
	@Autowired
	private Environment env;
	@Bean(name = "redisTemplate")
	public RedisTemplate<byte[], Object> redisTemplate() {
		RedisTemplate<byte[], Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory());
		template.setKeySerializer(stringRedisSerializer());
		//template.setValueSerializer(stringRedisSerializer());
		template.setHashKeySerializer(stringRedisSerializer());
		template.setHashValueSerializer(stringRedisSerializer());
		return template;
	}

	private RedisSerializer<?> stringRedisSerializer() {
		return new StringRedisSerializer();
	}

	@Bean
	public JedisConnectionFactory connectionFactory() {
		JedisConnectionFactory conn = new JedisConnectionFactory();
		conn.setDatabase(1);
		System.err.println(env.getProperty("spring.redis.host"));
		conn.setHostName(env.getProperty("spring.redis.host"));
		conn.setPassword(env.getProperty("spring.redis.password"));
		conn.setPort(Integer.parseInt(env.getProperty("spring.redis.port")));
		conn.setTimeout(Integer.parseInt(env.getProperty("spring.redis.timeout")));
		return conn;
	}

}
