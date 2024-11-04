package org.mastersdbis.mtsd.Repositories;

import org.mastersdbis.mtsd.Entities.Message.Message;
import org.mastersdbis.mtsd.Entities.Message.SenderType;
import org.mastersdbis.mtsd.Entities.User.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, Long> {

    List<Message> findBySenderTypeAndSentTo(SenderType type, Integer userId);

    @Query("{ '$or': [ { 'sentBy': ?0, 'sentTo': ?1 }, { 'sentBy': ?1, 'sentTo': ?0 } ] }")
    List<Message> findByUsersInvolved(Integer userId1, Integer userId2);
}