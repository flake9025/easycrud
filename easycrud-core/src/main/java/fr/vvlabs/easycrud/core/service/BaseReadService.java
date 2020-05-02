package fr.vvlabs.easycrud.core.service;

import fr.vvlabs.easycrud.core.entity.BaseUser;
import fr.vvlabs.easycrud.core.exception.FunctionalException;
import fr.vvlabs.easycrud.core.exception.ResourceNotFoundException;
import fr.vvlabs.easycrud.core.exception.TechnicalException;
import fr.vvlabs.easycrud.core.mapper.TargetMapper;
import fr.vvlabs.easycrud.core.repository.BaseRepository;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.transaction.annotation.Transactional;

public interface BaseReadService<T, ID extends Serializable, USER extends BaseUser> {

  // ===========================================================
  // Interface Methods
  // ===========================================================

  BaseRepository<T, ID> getBaseRepository();

  Optional<TargetMapper<T, USER>> getModelToDtoMapper();

  // ===========================================================
  // Default Methods
  // ===========================================================

  default Class<T> getModelType() {
    return ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
  }

  default String getModelName() {
    return getModelType().getSimpleName().trim();
  }

  default <TargetDTO extends Serializable> TargetDTO mapToDto(T entity, Class<TargetDTO> targetDtoClass, Optional<USER> user) {
    return getModelToDtoMapper().orElseThrow(() -> new TechnicalException("No TargetMapper<{}> implementation found !", getModelName()))
        .toTargetDto(entity, targetDtoClass, user);
  }

  /**
   * Method called to check the user rights to read the entity
   */
  default void checkUserReadAccess(@NotNull T entity, @NotNull USER user) throws FunctionalException {
  }

  @Transactional(readOnly = true)
  default Optional<T> find(ID id, Optional<USER> user) throws FunctionalException {
    Optional<T> entityOpt = getBaseRepository().findById(id);
    if (user.isPresent() && entityOpt.isPresent()) {
      checkUserReadAccess(entityOpt.get(), user.get());
    }
    return entityOpt;
  }

  @Transactional(readOnly = true)
  default T get(ID id, Optional<USER> user) throws FunctionalException {
    return find(id, user).orElseThrow(() -> new ResourceNotFoundException(getModelName(), id.toString()));
  }

  @Transactional(readOnly = true)
  default <TargetDTO extends Serializable> TargetDTO get(@NotNull ID id, Class<TargetDTO> targetDtoClass, @NotNull Optional<USER> user)
      throws FunctionalException {
    return mapToDto(get(id, user), targetDtoClass, user);
  }

}
