package fr.vvlabs.easycrud.core.mapper;

import fr.vvlabs.easycrud.core.entity.BaseUser;
import fr.vvlabs.easycrud.core.entity.EasyEntity;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.mapstruct.Mapper;

@Retention(RetentionPolicy.CLASS)
@Mapper(componentModel = "spring")
public @interface EasyReadMapper {

  Class<? extends EasyEntity> entity();

  Class<?> dto();

  Class<? extends BaseUser> user();
}
