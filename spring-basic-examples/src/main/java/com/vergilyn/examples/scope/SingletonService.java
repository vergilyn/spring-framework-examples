package com.vergilyn.examples.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 *
 * @author vergilyn
 * @date 2020-01-26
 */
@Service
@Scope("singleton")
public class SingletonService {
    private int number = 0;

    public int getNumber() {
        return number;
    }

    public int incrNumber() {
        return ++number;
    }
}


