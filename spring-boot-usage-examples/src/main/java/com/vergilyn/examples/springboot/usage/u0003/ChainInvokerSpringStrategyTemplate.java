package com.vergilyn.examples.springboot.usage.u0003;

import com.google.common.collect.Lists;
import com.vergilyn.examples.springboot.usage.u0002.SpringStrategy;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 *
 * @author vergilyn
 * @since 2022-06-08
 *
 * @see org.springframework.data.redis.core.RedisTemplate
 * @see org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
 */
@SuppressWarnings("JavadocReference")
public class ChainInvokerSpringStrategyTemplate<K, V extends SpringStrategy<K>>
		implements InvokerSpringStrategy<K, V>, ApplicationContextAware {

	private ApplicationContext applicationContext;

	/**
	 *
	 * <pre>例如，实际使用时一般都是：
	 *   {@code
	 *      @Autowired
	 *      private ChainInvokerSpringStrategyTemplate<String, Handler> handlerStrategy;
	 *   }
	 * </pre>
	 *
	 * 如果调用时<b>每个方法都需要传递`Handler.class`</b>，感觉代码太繁琐。
	 *
	 * <p> XXX 2022-06-08，是否有方法不传{@code clazz}，通过某种方式拿到 `Handler.class`。
	 */
	@Override
	public List<V> all(K key, Class<V> clazz) {
		List<V> beans = lookupBeans(key, clazz);

		return Optional.ofNullable(beans).orElse(Lists.newArrayList());
	}

	@Override
	public V getHighestPriority(K key, Class<V> clazz) {
		List<V> all = all(key, clazz);

		if (all.isEmpty()){
			return null;
		}

		return all.get(0);
	}

	@Override
	public V getLowerPriority(K key, Class<V> clazz) {
		List<V> all = all(key, clazz);

		if (all.isEmpty()){
			return null;
		}

		return all.get(all.size() - 1);
	}

	@Override
	public void invoke(K key, Class<V> clazz, Consumer<V> invoker) {
		for (V bean : all(key, clazz)) {
			invoker.accept(bean);
		}
	}


	/**
	 * <p> 1. TODO 2022-06-08，可以做一层 cache-map。
	 * <p> 2. 可以考虑 {@linkplain ApplicationContext#getBeansWithAnnotation(Class)}
	 * <p> 3. {@link ApplicationContext#getBeansOfType(Class)} 虽然是 {@link java.util.LinkedHashMap}，
	 *   但是貌似不支持{@link Order}（支持 {@link Ordered}），所以用{@link AnnotationAwareOrderComparator}重新排序。
	 */
	private List<V> lookupBeans(K key, Class<V> clazz){
		Assert.notNull(key, "`key` must not be null.");

		// 实际得到的是 `LinkedHashMap`
		Map<String, V> beans = applicationContext.getBeansOfType(clazz);
		if (beans == null || beans.isEmpty()){
			return null;
		}

		List<V> list = beans.values().stream()
				.filter(v -> key.equals(v.getStrategyKey()))
				.collect(Collectors.toList());

		// OrderComparator.sort(list);  // 不支持`@Order`注解

		AnnotationAwareOrderComparator.sort(list);

		return list;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
