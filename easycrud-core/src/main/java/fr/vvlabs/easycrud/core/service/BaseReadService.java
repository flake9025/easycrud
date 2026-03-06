package fr.vvlabs.easycrud.core.service;

import fr.vvlabs.easycrud.core.entity.BaseUser;
import fr.vvlabs.easycrud.core.entity.EasyEntity;
import fr.vvlabs.easycrud.core.exception.FunctionalException;
import fr.vvlabs.easycrud.core.exception.ResourceNotFoundException;
import fr.vvlabs.easycrud.core.exception.TechnicalException;
import fr.vvlabs.easycrud.core.mapper.TargetMapper;
import fr.vvlabs.easycrud.core.repository.BaseRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Optional;
import org.springframework.core.ResolvableType;

public interface BaseReadService<T extends EasyEntity, ID extends Serializable, USER extends BaseUser> {

    // ===========================================================
    // Interface Methods
    // ===========================================================

    BaseRepository<T, ID> getBaseRepository();

    Optional<TargetMapper<T, USER>> getModelToDtoMapper();

    // ===========================================================
    // Default Methods
    // ===========================================================

    @SuppressWarnings("unchecked")
    default Class<T> getModelType() {
        ResolvableType type = ResolvableType.forClass(getClass()).as(BaseReadService.class);
        Class<?> resolved = type.getGeneric(0).resolve();
        if (resolved == null) {
            throw new TechnicalException("Cannot resolve entity generic type for " + getClass().getName());
        }
        return (Class<T>) resolved;
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

    @Transactional(readOnly = true)
    default <TargetDTO extends Serializable> Page<TargetDTO> findAll(Pageable pageable, Class<TargetDTO> targetDtoClass, Optional<USER> user) {
        return getBaseRepository().findAll(pageable).map(entity -> mapToDto(entity, targetDtoClass, user));
    }
}
