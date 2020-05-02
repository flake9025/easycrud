package fr.vvlabs.easycrud.core.service;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface EasyReadService {

  Class<?> entity();

  Class<?> id();

  Class<?> user();
}
