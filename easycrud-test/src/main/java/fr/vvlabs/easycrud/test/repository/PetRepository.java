package fr.vvlabs.easycrud.test.repository;

import fr.vvlabs.easycrud.core.repository.EasyRepository;
import fr.vvlabs.easycrud.test.model.Pet;

@EasyRepository(entity = Pet.class, id = Long.class)
public interface PetRepository {

}
