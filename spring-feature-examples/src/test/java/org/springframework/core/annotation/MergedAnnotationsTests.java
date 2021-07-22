package org.springframework.core.annotation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.MergedAnnotations.SearchStrategy;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author vergilyn
 * @since 2021-06-22
 */
@SuppressWarnings("JavadocReference")
class MergedAnnotationsTests {

	/**
	 * <pre>SEE:
	 *   {@linkplain org.springframework.boot.SpringApplication#run(String...)}
	 *   --> `SpringApplication#prepareContext(...)`
	 *   --> {@linkplain org.springframework.boot.SpringApplication#load(ApplicationContext, Object[])}
	 *   --> {@linkplain org.springframework.boot.BeanDefinitionLoader#load(Class)}
	 * </pre>
	 */
	@Test
	public void basic(){
		TypeMappedAnnotations typeMappedAnnotations = (TypeMappedAnnotations) MergedAnnotations
				.from(TestApplication.class, SearchStrategy.TYPE_HIERARCHY);

		boolean isComponent = typeMappedAnnotations.isPresent(Component.class);
		assertThat(isComponent).isTrue();


	}


	@SpringBootApplication
	private static class TestApplication{

	}
}
