package fr.vvlabs.easycrud.processor.service;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec.Builder;
import fr.vvlabs.easycrud.core.mapper.TargetMapper;
import fr.vvlabs.easycrud.core.repository.BaseRepository;
import fr.vvlabs.easycrud.core.service.BaseReadService;
import fr.vvlabs.easycrud.core.service.EasyReadService;
import fr.vvlabs.easycrud.processor.utils.AbstractEasyCrudProcessor;
import java.io.IOException;
import java.util.Optional;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@SupportedAnnotationTypes(value = {"fr.vvlabs.easycrud.core.service.EasyReadService"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(EasyReadServiceProcessor.class)
public class EasyReadServiceProcessor extends AbstractEasyCrudProcessor<EasyReadService> {

  @Override
  protected boolean processElement(Element element, EasyReadService annotation) {
    TypeElement entity = getTypeElement((TypeElement) element, EasyReadService.class, "entity");
    TypeElement id = getTypeElement((TypeElement) element, EasyReadService.class, "id");
    TypeElement user = getTypeElement((TypeElement) element, EasyReadService.class, "user");

    // create type BaseRepository<entity,id>
    ParameterizedTypeName baseRepositoryType = ParameterizedTypeName.get(
        ClassName.get(BaseRepository.class),
        ClassName.get(entity),
        ClassName.get(id)
    );
    // add "@Autowired protected BaseRepository<entity,id> baseRepository"
    FieldSpec baseRepositoryField = FieldSpec.builder(baseRepositoryType, "baseRepository", Modifier.PROTECTED)
        .addAnnotation(Autowired.class)
        .build();
    // add "@Override public BaseRepository<entity,id> getBaseRepository()"
    MethodSpec baseRepositoryGetter = MethodSpec.methodBuilder("getBaseRepository")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .returns(baseRepositoryType)
        .addStatement("return baseRepository")
        .build();

    // create type TargetMapper<entity, user>
    ParameterizedTypeName targetMapperType = ParameterizedTypeName.get(
        ClassName.get(TargetMapper.class),
        ClassName.get(entity),
        ClassName.get(user)
    );
    // create type Optional<TargetMapper<entity, user>>
    ParameterizedTypeName optionalTargetMapperType = ParameterizedTypeName.get(
        ClassName.get(Optional.class),
        targetMapperType
    );
    // add "@Autowired protected Optional<TargetMapper<entity, user>> modelToDtoMapper"
    FieldSpec optionalTargetMapperField = FieldSpec.builder(optionalTargetMapperType, "modelToDtoMapper", Modifier.PROTECTED)
        .addAnnotation(Autowired.class)
        .build();
    // add "@Override public Optional<TargetMapper<entity, user>> getModelToDtoMapper()"
    MethodSpec optionalTargetMapperGetter = MethodSpec.methodBuilder("getModelToDtoMapper")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .returns(optionalTargetMapperType)
        .addStatement("return modelToDtoMapper")
        .build();

    // create type BaseReadService<entity,dto,id,user>
    ParameterizedTypeName serviceType = ParameterizedTypeName.get(
        ClassName.get(BaseReadService.class),
        ClassName.get(entity),
        ClassName.get(id),
        ClassName.get(user)
    );

    // Generate "xxxImpl implements BaseReadService<entity,dto,id,user>
    Builder controllerClassBuilder = implClassBuilder(element)
        .addSuperinterface(serviceType)
        .addAnnotation(Service.class)
        .addAnnotation(Validated.class)
        .addAnnotation(Slf4j.class)
        .addField(baseRepositoryField)
        .addMethod(baseRepositoryGetter)
        .addField(optionalTargetMapperField)
        .addMethod(optionalTargetMapperGetter);

    JavaFile javaFile = JavaFile.builder(getPackagePath(element), controllerClassBuilder.build()).build();
    try {
      javaFile.writeTo(processingEnv.getFiler());
    } catch (IOException e) {
      e.printStackTrace();
      return true;
    }
    return false;
  }
}
