package fr.vvlabs.easycrud.core.controller;

import fr.vvlabs.easycrud.core.entity.BaseUser;
import fr.vvlabs.easycrud.core.exception.FunctionalException;
import fr.vvlabs.easycrud.core.entity.EasyEntity;
import fr.vvlabs.easycrud.core.security.BaseSecurityConfig;
import fr.vvlabs.easycrud.core.service.BaseReadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotNull;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.Serializable;
import java.util.Optional;
import org.springframework.core.ResolvableType;

public interface BaseReadController<T extends EasyEntity, DTO extends Serializable, ID extends Serializable, USER extends BaseUser> {

  String PARAM_ID = "id";

  BaseReadService<T, ID, USER> getReadService();

  // ===========================================================
  // Default Methods
  // ===========================================================

  @SuppressWarnings("unchecked")
  default Class<? extends DTO> getReturnDtoClass(Optional<USER> user) {
    ResolvableType type = ResolvableType.forClass(getClass()).as(BaseReadController.class);
    Class<?> resolved = type.getGeneric(1).resolve();
    if (resolved == null) {
      throw new IllegalStateException("Cannot resolve DTO generic type for " + getClass().getName());
    }
    return (Class<DTO>) resolved;
  }

  @GetMapping("/{" + PARAM_ID + "}")
  @Operation(summary = "Renvoie l'objet par ID")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "L'objet")})
  default DTO get(@PathVariable(PARAM_ID) @NotNull ID id) throws FunctionalException {
    Optional<USER> user = BaseSecurityConfig.findConnectedUser();
    return getReadService().get(id, getReturnDtoClass(user), user);
  }

  @GetMapping
  @Operation(summary = "Renvoie la liste des objets paginée")
  @ApiResponses({@ApiResponse(responseCode = "200", description = "La liste des objets paginée")})
  default Page<DTO> findAll(@ParameterObject Pageable pageable) throws FunctionalException {
    Optional<USER> user = BaseSecurityConfig.findConnectedUser();
    return getReadService().findAll(pageable, (Class<DTO>) getReturnDtoClass(user), user);
  }
}
