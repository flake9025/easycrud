package fr.vvlabs.easycrud.core.service;

import fr.vvlabs.easycrud.core.entity.BaseUser;
import fr.vvlabs.easycrud.core.entity.EasyEntity;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.io.Serializable;

@Retention(RetentionPolicy.CLASS)
public @interface EasyReadService {

  Class<? extends EasyEntity> entity();

  Class<? extends Serializable> id();

  Class<? extends BaseUser> user();
}
