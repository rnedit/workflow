package kz.spring.workflow.repository;

import kz.spring.workflow.domain.eventqueue.EventQueue;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventQueueRepository extends MongoRepository<EventQueue, String> {

    EventQueue findEventQueueById(String id);
    List<EventQueue> findAllByExecuteSuccessIsNull();
    List<EventQueue> findAllByExecuteSuccessIsTrue();
    List<EventQueue> findAllByExecuteSuccessIsFalse();

}
