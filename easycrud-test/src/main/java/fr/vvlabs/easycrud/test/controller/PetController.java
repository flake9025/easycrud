package fr.vvlabs.easycrud.test.controller;

import fr.vvlabs.easycrud.core.controller.EasyReadController;
import fr.vvlabs.easycrud.test.dto.PetDto;
import fr.vvlabs.easycrud.test.model.Pet;
import fr.vvlabs.easycrud.test.model.User;

@EasyReadController(entity = Pet.class, dto = PetDto.class, id = Long.class, user = User.class)
public interface PetController {

}
