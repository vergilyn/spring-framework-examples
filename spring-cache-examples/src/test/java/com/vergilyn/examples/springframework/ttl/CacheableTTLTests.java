package com.vergilyn.examples.springframework.ttl;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

import com.google.common.collect.Maps;
import com.vergilyn.examples.springframework.AbstractSpringCacheApplicationTests;
import com.vergilyn.examples.springframework.ttl.configuration.TTLAutoConfiguration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = TTLAutoConfiguration.class)
class CacheableTTLTests extends AbstractSpringCacheApplicationTests {

	@RepeatedTest(2)
	public void ttl(){
		CacheableService service = registerAndGetBean(CacheableService.class);

		User user = service.getById(1L);

		printJson(user);
	}

	public static class CacheableService{

		@Cacheable(key = "'cache:user:id:' + #p0", cacheNames = "redis1d")
		public User getById(Long id){
			System.out.println("[vergilyn] >>>> get By Datasource");
			return User.DATASOURCE.get(id);
		}
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class User implements Serializable {
		public static final Map<Long, User> DATASOURCE = Maps.newHashMap();
		static {
			DATASOURCE.put(1L, build(1L));
			DATASOURCE.put(2L, build(2L));
		}

		private Long id;
		private String username;
		private String nickname;
		private LocalDateTime dateTime;

		public static User build(Long id){
			User user = new User();
			user.setId(id);
			user.setUsername("username-" + id);
			user.setNickname("nickname-" + id);
			user.setDateTime(LocalDateTime.now());

			return user;
		}
	}
}
