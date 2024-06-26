package com.socialnetwork.socialnetworkjavaspring.services.messages;

import com.socialnetwork.socialnetworkjavaspring.models.Message;
import com.socialnetwork.socialnetworkjavaspring.services.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface IMessageService{
    List<Message> findAllByConversationId(Long conversationId, String userId);

    Optional<Message> save(Message object);

    void deleteMessage(Long messageId);
}
