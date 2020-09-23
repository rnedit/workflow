package kz.spring.workflow.graphql.some;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.processor.GraphQLAnnotations;
import graphql.schema.GraphQLObjectType;

public class SomeObject {
    @GraphQLField
    public String field;
}
