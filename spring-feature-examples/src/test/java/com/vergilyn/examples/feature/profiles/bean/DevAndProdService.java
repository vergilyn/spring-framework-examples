package com.vergilyn.examples.feature.profiles.bean;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static com.vergilyn.examples.feature.profiles.bean.AbstractProfilesService.PROFILE_DEV;
import static com.vergilyn.examples.feature.profiles.bean.AbstractProfilesService.PROFILE_PROD;

@Profile({PROFILE_DEV, PROFILE_PROD})
@Component
public class DevAndProdService implements AbstractProfilesService {

}
