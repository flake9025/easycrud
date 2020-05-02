package fr.vvlabs.easycrud.core.mapper;

import java.io.Serializable;
import java.util.Optional;

public interface TargetMapper<T, USER> {

  <TargetDTO extends Serializable> TargetDTO toTargetDto(T entity, Class<TargetDTO> targetClass, Optional<USER> user);
}
