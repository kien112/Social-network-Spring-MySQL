package com.socialnetwork.socialnetworkjavaspring.services.users;

import com.socialnetwork.socialnetworkjavaspring.DTOs.people.SearchPeopleRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.people.SearchPeopleResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.users.ChangeStatusOrRoleUserRequestDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.users.FriendResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.DTOs.users.UserResponseDTO;
import com.socialnetwork.socialnetworkjavaspring.models.CustomUserDetails;
import com.socialnetwork.socialnetworkjavaspring.models.Relation;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.RelationType;
import com.socialnetwork.socialnetworkjavaspring.models.enums.RoleUser;
import com.socialnetwork.socialnetworkjavaspring.models.enums.UserStatus;
import com.socialnetwork.socialnetworkjavaspring.repositories.IUserRepository;
import com.socialnetwork.socialnetworkjavaspring.services.relations.IRelationService;
import com.socialnetwork.socialnetworkjavaspring.utils.Constants;
import com.socialnetwork.socialnetworkjavaspring.utils.ConvertUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Autowired
    private IRelationService relationService;

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

    public SearchPeopleResponseDTO findByFullNameLikeIgnoreCaseAndAccents(SearchPeopleRequestDTO request, String userId) {
        request.validateInput();
        Page<User> users = userRepository
                .findByFullNameLikeIgnoreCaseAndAccents(request.getFullName(), userId,
                        PageRequest.of(request.getPageIndex(), request.getPageSize()));

        List<FriendResponseDTO> friendResponseDTOs = ConvertUtils.convertList(users.getContent(), FriendResponseDTO.class);
        friendResponseDTOs.forEach(friend -> {
            friend.setIsFollowing(false);
            friend.setIsFriend(false);
            friend.setIsRequested(false);
            Long mutualFriend = relationService.countMutualFriends(userId, friend.getUserId());
            friend.setMutualFriendCount(mutualFriend);
            List<Relation> relations = relationService.findByUserIdAndUserTargetId(userId, friend.getUserId());
            for (Relation relation : relations) {
                if (relation.getType().equals(RelationType.FOLLOW)) {
                    friend.setIsFollowing(true);
                }
                if (relation.getType().equals(RelationType.FRIEND)) {
                    friend.setIsFriend(true);
                }
                if (relation.getType().equals(RelationType.REQUEST)) {
                    friend.setIsRequested(true);
                }
            }
        });


        return new SearchPeopleResponseDTO(request.getPageIndex(), request.getPageSize(), users.getTotalElements(), friendResponseDTOs);
    }

    @Override
    public List<User> findUsersByRelationType(String userId, RelationType type) {
        return userRepository.findUsersByRelationType(userId, type.toString());
    }

    @Override
    public User findById(String userId) throws Exception {
        return userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
    }

    @Override
    public void changeStatusUser(ChangeStatusOrRoleUserRequestDTO request) {
        try {
            User user = findById(request.getUserId());
            UserStatus userStatus = UserStatus.valueOf(request.getStatusOrRole().toUpperCase());
            user.setStatus(userStatus);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void changeRoleUser(ChangeStatusOrRoleUserRequestDTO request) {
        try {
            User user = findById(request.getUserId());
            RoleUser roleUser = RoleUser.valueOf(request.getStatusOrRole().toUpperCase());
            user.setUserRole(roleUser);
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<User> findAllUserBlocked(String userId) {
        return userRepository.findBlockedUsers(userId);
    }

    private Boolean isFriendWithCurrentUser(List<Relation> relations, String userId) {
        if (relations != Constants.NULL_OBJECT && relations.size() > Constants.NUMBER_ZERO) {
            for (Relation relation : relations) {
                if (relation.getUserTarget().getUserId().equals(userId)
                        && relation.getType().equals(RelationType.FRIEND))
                    return true;
            }
        }
        return false;
    }
}
