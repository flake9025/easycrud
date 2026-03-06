package fr.vvlabs.easycrud.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fr.vvlabs.easycrud.core.controller.BaseReadController;
import fr.vvlabs.easycrud.core.repository.BaseRepository;
import fr.vvlabs.easycrud.core.service.BaseReadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GeneratedClassesTest {

  @Test
  @DisplayName("Generated controller implements BaseReadController")
  void controllerSignature() throws Exception {
    Class<?> controllerImpl = Class.forName("fr.vvlabs.easycrud.test.controller.PetControllerImpl");
    assertTrue(BaseReadController.class.isAssignableFrom(controllerImpl));
    assertTrue(
        java.util.Arrays.stream(controllerImpl.getGenericInterfaces())
            .anyMatch(type -> type.getTypeName().contains("BaseReadController")),
        "Should implement BaseReadController with generics");
  }

  @Test
  @DisplayName("Generated service implements BaseReadService")
  void serviceSignature() throws Exception {
    Class<?> serviceImpl = Class.forName("fr.vvlabs.easycrud.test.service.PetServiceImpl");
    assertTrue(BaseReadService.class.isAssignableFrom(serviceImpl));
  }

  @Test
  @DisplayName("Generated repository extends BaseRepository")
  void repositorySignature() throws Exception {
    Class<?> repo = Class.forName("fr.vvlabs.easycrud.test.repository.IPetRepository");
    assertTrue(BaseRepository.class.isAssignableFrom(repo));
  }
}
