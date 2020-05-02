package fr.vvlabs.easycrud.processor.utils;

import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.TypeSpec.Builder;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

public abstract class AbstractEasyCrudProcessor<T extends Annotation> extends AbstractProcessor {

  protected Class<T> annotationClass;

  public AbstractEasyCrudProcessor() {
    annotationClass = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    processingEnv.getMessager().printMessage(Kind.NOTE, "Starting processor " + getClass().getSimpleName());
    for (TypeElement te : annotations) {
      for (Element element : roundEnv.getElementsAnnotatedWith(te)) {
        processingEnv.getMessager().printMessage(Kind.NOTE, "Processing " + element.getSimpleName(), element);
        T annotation = element.getAnnotation(annotationClass);
        if (annotation != null) {
          if (!processElement(element, annotation)) {
            return false;
          }
        }
      }
    }
    processingEnv.getMessager().printMessage(Kind.NOTE, "Ending processor " + getClass().getSimpleName());
    return true;
  }

  protected abstract boolean processElement(Element element, T annotation);

  protected AnnotationMirror getAnnotationMirror(TypeElement typeElement, Class<?> clazz) {
    String clazzName = clazz.getName();
    for (AnnotationMirror m : typeElement.getAnnotationMirrors()) {
      if (m.getAnnotationType().toString().equals(clazzName)) {
        return m;
      }
    }
    return null;
  }

  protected AnnotationValue getAnnotationValue(AnnotationMirror annotationMirror, String key) {
    for (Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
      if (entry.getKey().getSimpleName().toString().equals(key)) {
        return entry.getValue();
      }
    }
    return null;
  }

  protected TypeElement getTypeElement(TypeElement foo, Class<?> clazz, String key) {
    AnnotationMirror am = getAnnotationMirror(foo, clazz);
    if (am == null) {
      return null;
    }
    AnnotationValue av = getAnnotationValue(am, key);
    if (av == null) {
      return null;
    } else {
      return getTypeElement((TypeMirror) av.getValue());
    }
  }

  protected TypeElement getTypeElement(TypeMirror typeMirror) {
    Types TypeUtils = this.processingEnv.getTypeUtils();
    return (TypeElement) TypeUtils.asElement(typeMirror);
  }

  protected Builder interfaceBuilder(Element element) {
    Builder builder = TypeSpec.interfaceBuilder("I" + element.getSimpleName().toString())
        .addModifiers(Modifier.PUBLIC);
    return addSuperType(element, builder);
  }

  protected Builder abstractClassBuilder(Element element) {
    Builder builder = TypeSpec.classBuilder("Abstract" + element.getSimpleName().toString() + "Impl")
        .addModifiers(Modifier.PUBLIC)
        .addModifiers(Modifier.ABSTRACT);
    return addSuperType(element, builder);
  }

  protected Builder implClassBuilder(Element element) {
    Builder builder = TypeSpec.classBuilder(element.getSimpleName().toString() + "Impl")
        .addModifiers(Modifier.PUBLIC);
    return addSuperType(element, builder);
  }

  private Builder addSuperType(Element element, Builder builder) {
    if (isInterface(element)) {
      builder.addSuperinterface(element.asType());
    } else if (isAbstractClass(element)) {
      builder.superclass(element.asType());
    } else {
      processingEnv.getMessager()
          .printMessage(Kind.ERROR, getClassName(element) + " must be an interface or abstract class",
              element);
    }
    return builder;
  }

  protected boolean isInterface(Element element) {
    return ElementKind.INTERFACE.equals(element.getKind());
  }

  protected boolean isAbstractClass(Element element) {
    return ElementKind.CLASS.equals(element.getKind()) && element.getModifiers().contains(Modifier.ABSTRACT);
  }

  protected String getPackagePath(Element element) {
    return processingEnv.getElementUtils().getPackageOf(element).getQualifiedName().toString();
  }

  private String getClassName(Element element) {
    return getPackagePath(element) + "." + element.getSimpleName();
  }
}
