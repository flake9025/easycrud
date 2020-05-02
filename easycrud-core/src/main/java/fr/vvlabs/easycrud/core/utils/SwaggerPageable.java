package fr.vvlabs.easycrud.core.utils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fix swagger bug https://stackoverflow.com/questions/35404329/swagger-documentation-for-spring-pageable-interface
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiImplicitParams({
    @ApiImplicitParam(name = "page", dataType = "int", paramType = "query", value = "Results page you want to retrieve (0..N)", defaultValue = "0", example = "0"),
    @ApiImplicitParam(name = "size", dataType = "int", paramType = "query", value = "Number of records per page.", defaultValue = "50", example = "50"),
    @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query", value = "Sorting criteria in the format 'property(,asc|,desc)', e.g.: 'name,asc'. Default sort order is ascending. Multiple sort criteria are supported.")})
public @interface SwaggerPageable {

}