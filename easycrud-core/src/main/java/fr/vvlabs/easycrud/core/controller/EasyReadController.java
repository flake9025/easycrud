package fr.vvlabs.easycrud.core.controller;

import fr.vvlabs.easycrud.core.entity.BaseUser;
import fr.vvlabs.easycrud.core.entity.EasyEntity;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface EasyReadController {

  Class<? extends EasyEntity> entity();

  Class<?> dto();

  Class<?> id();

  Class<? extends BaseUser> user();
}
