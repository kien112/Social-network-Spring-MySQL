package com.socialnetwork.socialnetworkjavaspring.services.relations;


import com.socialnetwork.socialnetworkjavaspring.DTOs.relations.RelationResponseObjectDTO;
import com.socialnetwork.socialnetworkjavaspring.models.Relation;
import com.socialnetwork.socialnetworkjavaspring.models.User;
import com.socialnetwork.socialnetworkjavaspring.models.enums.RelationType;
import com.socialnetwork.socialnetworkjavaspring.services.IGeneralService;

import java.util.List;

public interface IRelationService extends IGeneralService<Relation, Long> {

    List<Relation> findByUserIdAndUserTargetId(String userId, String userTargetId);

    void removeAllByUserIdAndUserTargetId(String userId, String userTargetId);

    List<Relation> findByUserTargetIdAndType(String userId, RelationType type);

    Long countMutualFriends(String userId, String userTargetId);

    List<RelationResponseObjectDTO> findRelationDTOWithMutualFriendCount(String userId, List<Relation> relations);

    Relation findByUserIdAndUserTargetIdAndType(String userId, String userTargetId, RelationType type) throws Exception;

    void acceptFriend(User userRequest, User user) throws Exception;
}
