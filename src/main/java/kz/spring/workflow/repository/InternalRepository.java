package kz.spring.workflow.repository;

import kz.spring.workflow.domain.internal.Internal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface InternalRepository extends MongoRepository<Internal, Long> {

    @Query("{AllReaders : { $all : ['?0'] }}")
    Page<Internal> getAllByAllReadersContaining(String profileId, Pageable pageable);

}
