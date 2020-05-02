package fr.vvlabs.easycrud.processor.mapper;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec.Builder;
import fr.vvlabs.easycrud.core.mapper.BaseReadMapper;
import fr.vvlabs.easycrud.core.mapper.EasyReadMapper;
import fr.vvlabs.easycrud.processor.utils.AbstractEasyCrudProcessor;
import java.io.IOException;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes(value = {"fr.vvlabs.easycrud.core.mapper.EasyReadMapper"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(EasyReadMapperProcessor.class)
public class EasyReadMapperProcessor extends AbstractEasyCrudProcessor<EasyReadMapper> {

  @Override
  protected boolean processElement(Element element, EasyReadMapper annotation) {
    TypeElement entity = getTypeElement((TypeElement) element, EasyReadMapper.class, "entity");
    TypeElement dto = getTypeElement((TypeElement) element, EasyReadMapper.class, "dto");
    TypeElement user = getTypeElement((TypeElement) element, EasyReadMapper.class, "user");

    // create type BaseReadMapper<entity,dto,user>
    ParameterizedTypeName mapperType = ParameterizedTypeName.get(
        ClassName.get(BaseReadMapper.class),
        ClassName.get(entity),
        ClassName.get(dto),
        ClassName.get(user)
    );
    // Generate "abstract xxxImpl implements BaseReadMapper<entity,dto,user>
    Builder mapperClassBuilder = abstractClassBuilder(element)
        .addSuperinterface(mapperType);

    JavaFile javaFile = JavaFile.builder(getPackagePath(element), mapperClassBuilder.build()).build();
    try {
      javaFile.writeTo(processingEnv.getFiler());
    } catch (IOException e) {
      e.printStackTrace();
      return true;
    }
    return false;
  }
}
