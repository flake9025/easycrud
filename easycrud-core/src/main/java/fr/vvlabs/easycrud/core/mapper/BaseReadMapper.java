package fr.vvlabs.easycrud.core.mapper;

import fr.vvlabs.easycrud.core.entity.BaseUser;
import java.io.Serializable;
import java.util.Optional;
import org.mapstruct.MapperConfig;

@MapperConfig(uses = JpaMapper.class)
public interface BaseReadMapper<T, GETDTO, USER extends BaseUser> extends TargetMapper<T, USER> {

  GETDTO toDto(T entity);

  @Override
  @SuppressWarnings("unchecked")
  default <TargetDTO extends Serializable> TargetDTO toTargetDto(T entity, Class<TargetDTO> targetClass, Optional<USER> user) {
    return (TargetDTO) toDto(entity);
  }
}
