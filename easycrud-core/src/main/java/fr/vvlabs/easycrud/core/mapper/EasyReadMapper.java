package fr.vvlabs.easycrud.core.mapper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.mapstruct.Mapper;

@Retention(RetentionPolicy.CLASS)
@Mapper(componentModel = "spring")
public @interface EasyReadMapper {

  Class<?> entity();

  Class<?> dto();

  Class<?> user();
}
