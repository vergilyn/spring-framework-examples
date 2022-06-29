package com.vergilyn.examples.feature.scanner;

import com.google.common.collect.Lists;
import com.vergilyn.examples.feature.AbstractSpringFeatureTests;
import com.vergilyn.examples.feature.scanner.pojo.ChildA;
import com.vergilyn.examples.feature.scanner.pojo.ChildB;
import com.vergilyn.examples.feature.scanner.pojo.ParentInterface;
import com.vergilyn.examples.feature.scanner.pojo.SubChild;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Modifier;
import java.util.List;

/**
 * 如何通过 super-class 找到其 all-sub-class：
 * <a href="https://stackoverflow.com/questions/492184/how-do-you-find-all-subclasses-of-a-given-class-in-java">
 *     How do you find all subclasses of a given class in Java?</a>
 *
 * @author vergilyn
 * @since 2022-06-27
 */
@SuppressWarnings("JavadocReference")
public class PathMatchingResourcePatternResolverTests extends AbstractSpringFeatureTests {
	private String resourcePattern = "**/*.class";

	private final ClassLoader _classLoader = this.getClass().getClassLoader();
	private final PathMatchingResourcePatternResolver _resolver = new PathMatchingResourcePatternResolver(_classLoader);
	private final MetadataReaderFactory _metadataReaderFactory = new CachingMetadataReaderFactory(_classLoader);

	/**
	 * 可以通过 {@link ClassPathBeanDefinitionScanner#scan(String...)} 阅读spring源码是如何实现 scan 的。
	 */
	@Test
	public void test(){
		AnnotationConfigApplicationContext context = initApplicationContext();

		ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(context);
		scanner.scan("com.vergilyn.examples.feature.case01");

	}

	/**
	 * <pre>
	 *   - {@link PathMatchingResourcePatternResolver#findPathMatchingResources(String)}
	 *   - {@link PathMatchingResourcePatternResolver#doFindPathMatchingFileResources(Resource, String)}:
	 *      如果不希望依赖spring，可以重点看这个方法是如何找到 Files （spring找到files后会包装成 spring.Resource）。
	 * </pre>
	 *
	 * {@link ClassPathBeanDefinitionScanner#scan(String...)} 最终的核心逻辑其实就是下面的代码。
	 * 其实比较重要的是：{@link org.springframework.core.type.classreading.SimpleMetadataReader#SimpleMetadataReader(Resource, ClassLoader)}，
	 * 其构造函数中会解析并设置 {@link AnnotationMetadata#getClassName()}
	 *
	 * <pre>
	 *   如果不依赖spring，其实比较麻烦的是 {@link MetadataReaderFactory#getMetadataReader(Resource)}。
	 *   <b>需要去了解 java ASM。</b>
	 *   （虽然通过前面获取到了 files，但files不一定代表class-name，而且可能存在 内部类，那么从 files 其实无法知道内部类的classname）
	 * </pre>
	 */
	@Test
	@SneakyThrows
	public void getResources(){

		// org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider#scanCandidateComponents()
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
				+ ClassUtils.convertClassNameToResourcePath("com.vergilyn.examples.feature.scanner")
				+ '/' + this.resourcePattern;

		Resource[] resources = _resolver.getResources(packageSearchPath);

		for (Resource resource : resources) {
			System.out.printf("%s >>>> \n", resource.getFilename());
			// XXX 2022-06-28, 通过这种方法获取 class-name，然后再`Class.forName(...)`是否太麻烦了？
			MetadataReader metadataReader = _metadataReaderFactory.getMetadataReader(resource);

			// ClassMetadata & AnnotationMetadata 具体的区别是？
			// 貌似更多的都是用 `AnnotationMetadata`
			ClassMetadata classMetadata = metadataReader.getClassMetadata();
			AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
			//
			// System.out.printf(" ClassMetadata: %s \n", JSON.toJSONString(classMetadata, true));
			// System.out.printf(" AnnotationMetadata: %s \n", JSON.toJSONString(annotationMetadata, true));

			System.out.printf(" class-name: %s \n", classMetadata.getClassName());
			System.out.printf(" super-class-name: %s \n", classMetadata.getSuperClassName());

			Class<?> clazz = ClassUtils.forName(classMetadata.getClassName(), this.getClass().getClassLoader());
			System.out.printf(" class: %s \n", clazz);
			System.out.printf(" isAssignable : %s \n", ClassUtils.isAssignable(AbstractSpringFeatureTests.class, clazz));


			// 都只是当前类的直接 superClass或者interface。
			String superClassName = annotationMetadata.getSuperClassName();
			String[] interfaceNames = annotationMetadata.getInterfaceNames();

			// 例如 `SubChild` 貌似也只能得到 `ParentInterface`，没有返回`SuperParentInterface`
			Class<?>[] allInterfacesForClass = ClassUtils.getAllInterfacesForClass(clazz);

			System.out.println();
		}
	}

	/**
	 * 期望：找到 {@link com.alibaba.fastjson.serializer.SerializeFilter} 的所有实现类。
	 * 不包括 interface/abstract-class，但包括 孙子辈 实现类。
	 */
	@Test
	@SneakyThrows
	public void findAllSubClass(){
		Class<ParentInterface> expectedSuperClass = ParentInterface.class;

		// ChildC 不在指定的 package 下。
		List<Class<?>> expectedClasses = Lists.newArrayList(
				ChildA.class, ChildB.class, SubChild.class
		);

		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
				// 约定：只找 相同package 下的实现类。（有效避免 扫过过多的 jar或者package）
				+ ClassUtils.convertClassNameToResourcePath(expectedSuperClass.getPackage().getName())
				+ '/' + this.resourcePattern;

		Resource[] resources = _resolver.getResources(packageSearchPath);

		List<Class<?>> actualClasses = Lists.newArrayList();
		for (Resource resource : resources) {
			MetadataReader metadataReader = _metadataReaderFactory.getMetadataReader(resource);

			AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();

			String className = annotationMetadata.getClassName();

			Class<?> clazz = ClassUtils.forName(className, _classLoader);

			if (!ClassUtils.isAssignable(expectedSuperClass, clazz)){
				continue;
			}

			int modifiers = clazz.getModifiers();
			if (Modifier.isInterface(modifiers) || Modifier.isAbstract(modifiers)){
				continue;
			}

			actualClasses.add(clazz);


		}

		actualClasses.stream().map(Class::getName).sorted().forEachOrdered(System.out::println);

		Assertions.assertIterableEquals(expectedClasses, actualClasses);
	}

}
