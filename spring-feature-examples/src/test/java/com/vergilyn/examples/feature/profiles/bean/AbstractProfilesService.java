package com.vergilyn.examples.feature.profiles.bean;

public interface AbstractProfilesService {
	public static final String PROFILE_DEV = "dev";
	public static final String PROFILE_PROD = "prod";

	default String getClassName() {
		return this.getClass().getName();
	}
}
