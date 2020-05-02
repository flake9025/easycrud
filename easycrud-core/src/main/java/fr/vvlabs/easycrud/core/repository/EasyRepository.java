package fr.vvlabs.easycrud.core.repository;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface EasyRepository {

  Class entity();

  Class id();
}
