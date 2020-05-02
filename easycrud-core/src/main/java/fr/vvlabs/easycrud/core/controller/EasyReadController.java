package fr.vvlabs.easycrud.core.controller;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface EasyReadController {

  Class<?> entity();

  Class<?> dto();

  Class<?> id();

  Class<?> user();
}
