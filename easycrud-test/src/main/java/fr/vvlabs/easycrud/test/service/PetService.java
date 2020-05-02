package fr.vvlabs.easycrud.test.service;

import fr.vvlabs.easycrud.core.service.EasyReadService;
import fr.vvlabs.easycrud.test.model.Pet;
import fr.vvlabs.easycrud.test.model.User;

@EasyReadService(entity = Pet.class, id = Long.class, user = User.class)
public abstract class PetService {

}
