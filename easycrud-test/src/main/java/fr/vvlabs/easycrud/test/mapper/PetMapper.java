package fr.vvlabs.easycrud.test.mapper;

import fr.vvlabs.easycrud.core.mapper.EasyReadMapper;
import fr.vvlabs.easycrud.test.dto.PetDto;
import fr.vvlabs.easycrud.test.model.Pet;
import fr.vvlabs.easycrud.test.model.User;

@EasyReadMapper(entity = Pet.class, dto = PetDto.class, user = User.class)
public interface PetMapper {

}
