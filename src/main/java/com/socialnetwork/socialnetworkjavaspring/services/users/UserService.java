package com.socialnetwork.socialnetworkjavaspring.services.users;

import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserParticipantDTO;
import com.socialnetwork.socialnetworkjavaspring.models.CustomUserDetails;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.repositories.IUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService implements IUserService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public Optional<User> save(User object) {
        return Optional.of(userRepository.save(object));
    }

    @Override
    public Optional<User> delete(User object) {
        try {
            userRepository.delete(object);
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return new CustomUserDetails(user.get());
        }
        return null;
    }

    @Transactional
    public UserDetails loadUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + userId)
        );

        return new CustomUserDetails(user);
    }

    @Override
    public List<UserParticipantDTO> getUserFriends(String userId) {
        List<User> userFriends = userRepository.getUserFriends(userId);
        List<UserParticipantDTO> userFriendDTOs = new ArrayList<>();
        for (User u : userFriends) {
            userFriendDTOs.add(new UserParticipantDTO(
                    u.getUserId(),
                    u.getFullName(),
                    u.getAvatar()
            ));
        }

        return userFriendDTOs;
    }
}
