package com.vergilyn.examples.feature.profiles;

import com.vergilyn.examples.feature.AbstractSpringFeatureTests;
import com.vergilyn.examples.feature.profiles.bean.DevAndProdService;
import com.vergilyn.examples.feature.profiles.bean.DevService;
import com.vergilyn.examples.feature.profiles.bean.ProdService;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static com.vergilyn.examples.feature.profiles.bean.AbstractProfilesService.PROFILE_DEV;
import static com.vergilyn.examples.feature.profiles.bean.AbstractProfilesService.PROFILE_PROD;

/**
 *
 * @author vergilyn
 * @since 2021-07-22
 */
public class ActiveProfilesTests extends AbstractSpringFeatureTests {

	@Test
	public void dev() {
		AnnotationConfigApplicationContext context = context(PROFILE_DEV);

		DevService dev = context.getBean(DevService.class);
		System.out.println("dev >>>> " + dev.getClassName());

		ProdService prod;
		try {
			prod = context.getBean(ProdService.class);
			System.out.println("prod >>>> " + prod.getClassName());
		}catch (Exception e){
			System.out.println("prod >>>> is null, error-msg: " + e.getMessage());
		}

		DevAndProdService devAndProd = context.getBean(DevAndProdService.class);
		System.out.println("dev&prod >>>> " + devAndProd.getClassName());

	}

	@Test
	public void prod() {
		AnnotationConfigApplicationContext context = context(PROFILE_PROD);

		DevService dev;
		try {
			dev = context.getBean(DevService.class);
			System.out.println("dev >>>> " + dev.getClassName());
		}catch (Exception e){
			System.out.println("dev >>>> is null, error-msg: " + e.getMessage());
		}

		ProdService prod = context.getBean(ProdService.class);
		System.out.println("prod >>>> " + prod.getClassName());

		DevAndProdService devAndProd = context.getBean(DevAndProdService.class);
		System.out.println("dev&prod >>>> " + devAndProd.getClassName());

	}

	private AnnotationConfigApplicationContext context(String activeProfile){
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		context.getEnvironment().setActiveProfiles(activeProfile);
		context.scan("com.vergilyn.examples.feature.profiles.bean");
		context.refresh();

		return context;
	}
}
