package com.vergilyn.examples.retry;

import org.springframework.retry.support.RetryTemplate;

/**
 * @author vergilyn
 * @since 2022-03-23
 *
 * @see org.springframework.retry.annotation.EnableRetry
 */
public abstract class AbstractSpringRetryTests {

	protected final RetryTemplate _retryTemplate = new RetryTemplate();

}
