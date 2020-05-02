package fr.vvlabs.easycrud.core.mapper;

import fr.vvlabs.easycrud.core.entity.EasyEntity;
import fr.vvlabs.easycrud.core.exception.ResourceNotFoundException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.mapstruct.Mapper;
import org.mapstruct.TargetType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

@Mapper(componentModel = "spring")
public class JpaMapper {

  @Autowired
  private Optional<EntityManager> entityManager;

  public <T extends EasyEntity> T mapEntity(Serializable key, @TargetType Class<T> entityClass) throws ResourceNotFoundException {
    if (key == null) {
      return null;
    }
    Assert.isTrue(entityManager.isPresent(), "No EntityManager available, check JPA configuration");
    T entity = entityManager.get().find(entityClass, key);
    if (entity == null) {
      throw new ResourceNotFoundException(entityClass.getSimpleName(), key);
    }
    return entity;
  }

  public <T extends Enum<T>> T mapEnum(Serializable key, @TargetType Class<T> enumClass) throws ResourceNotFoundException {
    return key == null ? null : Arrays.stream(enumClass.getEnumConstants()).filter(key::equals).findFirst().orElseThrow(() ->
        new ResourceNotFoundException("Enum de type '" + enumClass.getSimpleName() + "': ", key));
  }
}
