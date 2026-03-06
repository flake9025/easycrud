package fr.vvlabs.easycrud.core.mapper;

import fr.vvlabs.easycrud.core.entity.EasyEntity;
import fr.vvlabs.easycrud.core.entity.BaseUser;
import java.io.Serializable;
import java.util.Optional;

public interface TargetMapper<T extends EasyEntity, USER extends BaseUser> {

  <TargetDTO extends Serializable> TargetDTO toTargetDto(T entity, Class<TargetDTO> targetClass, Optional<USER> user);
}
