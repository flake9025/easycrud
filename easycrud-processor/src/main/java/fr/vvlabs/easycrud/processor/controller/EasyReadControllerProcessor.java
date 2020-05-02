package fr.vvlabs.easycrud.processor.controller;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec.Builder;
import fr.vvlabs.easycrud.core.controller.BaseReadController;
import fr.vvlabs.easycrud.core.controller.EasyReadController;
import fr.vvlabs.easycrud.core.service.BaseReadService;
import fr.vvlabs.easycrud.processor.utils.AbstractEasyCrudProcessor;
import java.io.IOException;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@SupportedAnnotationTypes({"fr.vvlabs.easycrud.core.controller.EasyReadController"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(EasyReadControllerProcessor.class)
public class EasyReadControllerProcessor extends AbstractEasyCrudProcessor<EasyReadController> {

  @Override
  protected boolean processElement(Element element, EasyReadController annotation) {

    TypeElement entity = getTypeElement((TypeElement) element, EasyReadController.class, "entity");
    TypeElement dto = getTypeElement((TypeElement) element, EasyReadController.class, "dto");
    TypeElement id = getTypeElement((TypeElement) element, EasyReadController.class, "id");
    TypeElement user = getTypeElement((TypeElement) element, EasyReadController.class, "user");

    // create type BaseReadService<entity,id,user>
    ParameterizedTypeName serviceType = ParameterizedTypeName.get(
        ClassName.get(BaseReadService.class),
        ClassName.get(entity),
        ClassName.get(id),
        ClassName.get(user)
    );
    // add "@Autowired private BaseReadService<entity,id,user> readService"
    FieldSpec readServiceField = FieldSpec.builder(serviceType, "readService", Modifier.PRIVATE)
        .addAnnotation(Autowired.class)
        .build();
    // add "@Override public BaseReadService<entity,id,user> getReadService()"
    MethodSpec readServiceGetter = MethodSpec.methodBuilder("getReadService")
        .addAnnotation(Override.class)
        .addModifiers(Modifier.PUBLIC)
        .returns(serviceType)
        .addStatement("return readService")
        .build();

    // create type BaseReadController<entity,dto,id,user>
    ParameterizedTypeName controllerType = ParameterizedTypeName.get(
        ClassName.get(BaseReadController.class),
        ClassName.get(entity),
        ClassName.get(dto),
        ClassName.get(id),
        ClassName.get(user)
    );
    // Generate "xxxImpl implements BaseReadController<entity,dto,id,user>
    Builder controllerClassBuilder = implClassBuilder(element)
        .addSuperinterface(controllerType)
        .addAnnotation(RestController.class)
        .addAnnotation(Validated.class)
        .addAnnotation(Slf4j.class)
        .addField(readServiceField)
        .addMethod(readServiceGetter);

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
