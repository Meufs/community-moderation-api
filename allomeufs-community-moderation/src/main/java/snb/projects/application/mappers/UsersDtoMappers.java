package snb.projects.application.mappers;

import snb.projects.application.dto.requests.CreateAdminRequest;
import snb.projects.application.dto.requests.CreateUserRequest;
import snb.projects.application.dto.responses.CreateUserResponse;
import snb.projects.application.dto.responses.UserLoginResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import snb.projects.domain.models.commands.users.CreateUserCommand;
import snb.projects.domain.models.users.UserBasicInformations;
import snb.projects.domain.models.users.UserCreated;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI)
public interface UsersDtoMappers {
    @Mapping(target = "reference", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "verificationCode", ignore = true)
    @Mapping(target = "verificationCodeTimestamp", ignore = true)
	CreateUserCommand fromCreationRequest(CreateUserRequest createUserRequest);

    @Mapping(target = "reference", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "verificationCode", ignore = true)
    @Mapping(target = "verificationCodeTimestamp", ignore = true)
	CreateUserCommand fromCreationRequest(CreateAdminRequest createAdminRequest);
	
	CreateUserResponse toCreationResponse(UserBasicInformations userBasicInformations);
	UserLoginResponse toLoginResponse(UserCreated user);

}