package com.socialnetwork.socialnetworkjavaspring.services.users;

import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserParticipantDTO;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.services.IGeneralService;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface IUserService extends IGeneralService<User, String>, UserDetailsService {
    List<UserParticipantDTO> getUserFriends(String userId);
}
