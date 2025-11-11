package snb.projects.persistence.mappers.users;

import snb.projects.persistence.entities.AdminsEntity;
import snb.projects.persistence.entities.MeufsEntity;
import com.templates.persistence.entities.UsersEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import snb.projects.domain.models.commands.users.CreateUserCommand;
import snb.projects.domain.models.users.User;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface UsersEntityMapper {
    MeufsEntity fromCreateUserToClient(CreateUserCommand createUserCommand);
	AdminsEntity fromCreateUserToAdmin(CreateUserCommand createUserCommand);
    User fromEntityToUser(UsersEntity usersEntity);
	User fromClientToUser(MeufsEntity meufsEntity);
	User fromAdminToUser(AdminsEntity adminsEntity);
}