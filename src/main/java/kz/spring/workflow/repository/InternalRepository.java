package kz.spring.workflow.repository;

import kz.spring.workflow.domain.internal.Internal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternalRepository extends MongoRepository<Internal, Long> {

    @Query("{AllReaders : { $all : ['?0'] }}")
    Page<Internal> getAllByAllReadersContaining(String profileId, Pageable pageable);

    @Query("{subject : { $regex : /?0/ }}")
    List<Internal> getAllBySubjectRegexssss(String regex);


    List<Internal> findAllByNumberRegexOrSubjectRegex(String number, String subject);

}
