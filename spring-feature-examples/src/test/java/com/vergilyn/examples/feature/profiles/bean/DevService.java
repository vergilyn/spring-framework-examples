package com.vergilyn.examples.feature.profiles.bean;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import static com.vergilyn.examples.feature.profiles.bean.AbstractProfilesService.PROFILE_DEV;

@Profile(PROFILE_DEV)
@Component
public class DevService implements AbstractProfilesService {

}
