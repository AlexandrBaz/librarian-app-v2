package ru.bazhenov.librarianapp.service;

import org.springframework.stereotype.Service;
import ru.bazhenov.librarianapp.dto.PersonDto;
import ru.bazhenov.librarianapp.models.PersonRole;

import java.util.List;

@Service
public class AdminServiceImpl extends AbstractUserService implements AdminService{

    @Override
    public List<PersonDto> getAllManagers() {
        return getPersonRepositoryService().getAllUser(PersonRole.MANAGER).stream()
                .map(getPersonMapper()::toDTO)
                .toList();
    }

    @Override
    public void registerNewUser(PersonDto personDto) {
        getPersonRepositoryService().registerNewUser(personDto, PersonRole.MANAGER);
    }
}
