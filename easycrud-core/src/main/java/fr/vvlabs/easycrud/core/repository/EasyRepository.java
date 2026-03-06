package fr.vvlabs.easycrud.core.repository;

import fr.vvlabs.easycrud.core.entity.EasyEntity;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.io.Serializable;

@Retention(RetentionPolicy.CLASS)
public @interface EasyRepository {

  Class<? extends EasyEntity> entity();

  Class<? extends Serializable> id();
}
