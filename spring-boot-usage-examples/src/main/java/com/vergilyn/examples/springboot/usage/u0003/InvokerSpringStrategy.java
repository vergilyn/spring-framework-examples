package com.vergilyn.examples.springboot.usage.u0003;

import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.vergilyn.examples.springboot.usage.u0002.SpringStrategy;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.function.Consumer;

/**
 * 利用spring构造函数注入实现策略模式。
 *
 * <h3> XXX 2022-06-08，方法签名太繁琐</h3>
 * <pre>例如，实际使用时一般都是：
 *   {@code
 *      @Autowired
 *      private ChainInvokerSpringStrategyTemplate<String, Handler> handlerStrategy;
 *
 *      // 期望如此
 *      handlerStrategy.lookupBeans(key);
 *
 *      // 实际需要传递`class`，感觉太繁琐了。
 *      handlerStrategy.lookupBeans(key, class);
 *   }
 * </pre>
 *
 * <p> 1. 其实感觉类似 {@link com.alibaba.fastjson.JSON#parseObject(String, Class)}，也会通过参数传递class。
 * <p> 2. 关于如果 class 还存在泛型，可能类似 {@link com.alibaba.fastjson.JSON#parseObject(String, TypeReference, Feature...)}。
 *
 * @param <K> 区分策略的key，即`map.key`。
 * @param <V> spring-bean。需要实现{@linkplain SpringStrategy}
 *
 * @author vergilyn
 * @since 2022-06-08
 *
 * @see org.springframework.core.Ordered
 * @see org.springframework.core.annotation.Order
 */
public interface InvokerSpringStrategy<K, V extends SpringStrategy<K>> {

	List<V> lookupBeans(K key, Class<V> clazz);

	List<V> lookupBeans(K key, ParameterizedTypeReference<V> typeReference);

	V getHighestPriority(K key, Class<V> clazz);

	V getLowerPriority(K key, Class<V> clazz);

	void invoke(K key, Class<V> clazz, Consumer<V> invoker);
}
