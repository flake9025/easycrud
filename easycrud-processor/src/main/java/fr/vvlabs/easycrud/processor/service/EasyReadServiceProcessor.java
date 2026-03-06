package fr.vvlabs.easycrud.processor.service;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;
import com.squareup.javapoet.TypeSpec.Builder;
import fr.vvlabs.easycrud.core.mapper.TargetMapper;
import fr.vvlabs.easycrud.core.repository.BaseRepository;
import fr.vvlabs.easycrud.core.service.BaseReadService;
import fr.vvlabs.easycrud.core.service.EasyReadService;
import fr.vvlabs.easycrud.processor.utils.AbstractEasyCrudProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;
import java.util.Optional;

@SupportedAnnotationTypes(value = {"fr.vvlabs.easycrud.core.service.EasyReadService"})
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@AutoService(Processor.class)
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
        .addAnnotation(ClassName.get("org.springframework.beans.factory.annotation", "Autowired"))
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
        .addAnnotation(ClassName.get("org.springframework.beans.factory.annotation", "Autowired"))
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
    ClassName implClassName = ClassName.get(getPackagePath(element), element.getSimpleName() + "Impl");
    FieldSpec loggerField = FieldSpec.builder(ClassName.get(Logger.class), "log", Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL)
        .initializer("$T.getLogger($T.class)", ClassName.get(LoggerFactory.class), implClassName)
        .build();

    // Generate "xxxImpl implements BaseReadService<entity,dto,id,user>
    Builder controllerClassBuilder = implClassBuilder(element)
        .addSuperinterface(serviceType)
        .addAnnotation(ClassName.get("org.springframework.stereotype", "Service"))
        .addAnnotation(ClassName.get("org.springframework.validation.annotation", "Validated"))
        .addField(loggerField)
        .addField(baseRepositoryField)
        .addMethod(baseRepositoryGetter)
        .addField(optionalTargetMapperField)
        .addMethod(optionalTargetMapperGetter);

    JavaFile javaFile = JavaFile.builder(getPackagePath(element), controllerClassBuilder.build()).build();
    try {
      javaFile.writeTo(processingEnv.getFiler());
    } catch (IOException e) {
      processingEnv.getMessager().printMessage(
          Diagnostic.Kind.ERROR,
          "Failed to write generated service for " + element.getSimpleName() + ": " + e.getMessage(),
          element);
      return false;
    }
    return true;
  }
}
