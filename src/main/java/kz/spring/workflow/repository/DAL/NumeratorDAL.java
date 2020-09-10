package kz.spring.workflow.repository.DAL;

import kz.spring.workflow.domain.configuration.Numerator;

public interface NumeratorDAL {
    Numerator getNumeratorById(String id);
    Numerator getNumeratorBySuffix(String profileSuffix);
    Numerator saveNumerator(Numerator numerator);
}
