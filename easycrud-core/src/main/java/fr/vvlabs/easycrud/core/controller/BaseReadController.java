package fr.vvlabs.easycrud.core.controller;

import fr.vvlabs.easycrud.core.entity.BaseUser;
import fr.vvlabs.easycrud.core.exception.FunctionalException;
import fr.vvlabs.easycrud.core.security.BaseSecurityConfig;
import fr.vvlabs.easycrud.core.service.BaseReadService;
import fr.vvlabs.easycrud.core.utils.SwaggerPageable;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface BaseReadController<T, DTO extends Serializable, ID extends Serializable, USER extends BaseUser> {

  // ===========================================================
  // Constants
  // ===========================================================

  String PARAM_ID = "id";
  String PARAM_FIELD_FILTERS = "fieldFilters";
  String FILTER_RESOURCE = "/filter";
  String COUNT_RESOURCE = "/count";

  // ===========================================================
  // Interface Methods
  // ===========================================================

  BaseReadService<T, ID, USER> getReadService();

  // ===========================================================
  // Default Methods
  // ===========================================================

  default Class<? extends DTO> getReturnDtoClass(Optional<USER> user) {
    return ((Class<DTO>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1]);
  }

  @GetMapping("/{" + PARAM_ID + "}")
  @ApiOperation("Renvoie l'objet par ID")
  @ApiResponses({@ApiResponse(code = 200, message = "L'objet")})
  default DTO get(@PathVariable(PARAM_ID) @NotNull ID id) throws FunctionalException {
    Optional<USER> user = BaseSecurityConfig.findConnectedUser();
    return getReadService().get(id, getReturnDtoClass(user), user);
  }


  @GetMapping
  @ApiOperation("Renvoie la liste des objets paginée")
  @ApiResponses({@ApiResponse(code = 200, message = "La liste des objets paginée")})
  @SwaggerPageable
  default Page<DTO> findAll(Pageable pageable) throws FunctionalException {
    Optional<USER> user = BaseSecurityConfig.findConnectedUser();
    //return getReadService().findAll(pageable, (Class<DTO>) getReturnDtoClass(user), user);
    return null;
  }
}
