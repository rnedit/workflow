package kz.spring.workflow.repository.Impl;

import kz.spring.workflow.domain.configuration.Numerator;
import kz.spring.workflow.repository.DAL.NumeratorDAL;
import org.bson.types.ObjectId;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class NumeratorDALImpl implements NumeratorDAL {

    private final MongoTemplate mongoTemplate;

    public NumeratorDALImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Numerator getNumeratorById(String id) {

        if (id == null) {
            throw new IllegalArgumentException("Numerator id cannot be null");
        }

        Query query = new Query();
        query.addCriteria(Criteria
                .where("id").is(new ObjectId(id))
        );

        final Numerator numerator = mongoTemplate.findOne(query, Numerator.class);

        if (numerator == null) {
            throw new ObjectNotFoundException("Numerator Id " + id + " not found!", Numerator.class.getName());
        }

        final Numerator nData = Numerator.setNewNumerator(numerator);

        return nData;
    }

    @Override
    public Numerator getNumeratorBySuffix(String profileSuffix) {
        if (profileSuffix == null) {
            throw new IllegalArgumentException("Numerator profileSuffix cannot be null");
        }

        Query query = new Query();
        query.addCriteria(Criteria
                .where("key").is(profileSuffix)
        );

        Numerator numerator = mongoTemplate.findOne(query, Numerator.class);

        if (numerator == null) {
            query.addCriteria(Criteria
                    .where("key").is("")
            );
            numerator = mongoTemplate.findOne(query, Numerator.class);

            if (numerator==null)
                numerator = new Numerator();

            numerator.setName("Создан автоматически. " + (profileSuffix.isBlank()?"У пользователя нет профайла":"Суффикс " + profileSuffix));
        }

        final Numerator nData = Numerator.setNewNumerator(numerator);

        return nData;
    }

    @Transactional
    @Override
    public Numerator saveNumerator(Numerator numerator) {
        if (numerator == null) {
            throw new IllegalArgumentException("Numerator numerator cannot be null");
        }
        mongoTemplate.save(numerator);
        return numerator;
    }
}
