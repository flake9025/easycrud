package fr.vvlabs.easycrud.core.exception;

import java.io.Serializable;

public class ResourceNotFoundException extends FunctionalException {

  public ResourceNotFoundException(Object errorType, Serializable... parameters) {
  }
}
