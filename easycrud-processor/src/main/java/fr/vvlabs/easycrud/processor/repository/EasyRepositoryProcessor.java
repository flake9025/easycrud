package fr.vvlabs.easycrud.processor.repository;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec.Builder;
import fr.vvlabs.easycrud.core.repository.BaseRepository;
import fr.vvlabs.easycrud.core.repository.EasyRepository;
import fr.vvlabs.easycrud.processor.utils.AbstractEasyCrudProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.IOException;

@SupportedAnnotationTypes(value = {"fr.vvlabs.easycrud.core.repository.EasyRepository"})
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@AutoService(Processor.class)
public class EasyRepositoryProcessor extends AbstractEasyCrudProcessor<EasyRepository> {

  @Override
  protected boolean processElement(Element element, EasyRepository annotation) {
    TypeElement entity = getTypeElement((TypeElement) element, EasyRepository.class, "entity");
    TypeElement id = getTypeElement((TypeElement) element, EasyRepository.class, "id");

    // create type BaseRepository<entity,dto,user>
    ParameterizedTypeName repositoryType = ParameterizedTypeName.get(
        ClassName.get(BaseRepository.class),
        ClassName.get(entity),
        ClassName.get(id)
    );
    // Generate "Ixxx extends BaseRepository<entity,id>
    Builder repositoryBuilder = interfaceBuilder(element)
        .addSuperinterface(repositoryType)
        .addAnnotation(ClassName.get("org.springframework.stereotype", "Repository"));

    JavaFile javaFile = JavaFile.builder(getPackagePath(element), repositoryBuilder.build()).build();
    try {
      javaFile.writeTo(processingEnv.getFiler());
    } catch (IOException e) {
      processingEnv.getMessager().printMessage(
          Diagnostic.Kind.ERROR,
          "Failed to write generated repository for " + element.getSimpleName() + ": " + e.getMessage(),
          element);
      return false;
    }
    return true;
  }
}
