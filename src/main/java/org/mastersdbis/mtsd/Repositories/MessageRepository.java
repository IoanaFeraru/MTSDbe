package org.mastersdbis.mtsd.Repositories;

import org.mastersdbis.mtsd.Entities.Message.Message;
import org.mastersdbis.mtsd.Entities.Message.SenderType;
import org.mastersdbis.mtsd.Entities.User.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, Long> {

    List<Message> findBySenderTypeAndUser(SenderType type, User user);

    @Query("{ '$or': [ { 'user1': ?0, 'user2': ?1 } ] }")
    List<Message> findByUser1AndUser2(User user1, User user2);
}