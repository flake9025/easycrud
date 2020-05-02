package fr.vvlabs.easycrud.processor.repository;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec.Builder;
import fr.vvlabs.easycrud.core.repository.BaseRepository;
import fr.vvlabs.easycrud.core.repository.EasyRepository;
import fr.vvlabs.easycrud.processor.utils.AbstractEasyCrudProcessor;
import java.io.IOException;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.springframework.stereotype.Repository;

@SupportedAnnotationTypes(value = {"fr.vvlabs.easycrud.core.repository.EasyRepository"})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(EasyRepositoryProcessor.class)
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
        .addAnnotation(Repository.class);

    JavaFile javaFile = JavaFile.builder(getPackagePath(element), repositoryBuilder.build()).build();
    try {
      javaFile.writeTo(processingEnv.getFiler());
    } catch (IOException e) {
      e.printStackTrace();
      return true;
    }
    return false;
  }
}
