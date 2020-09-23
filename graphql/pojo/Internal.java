package kz.spring.workflow.graphql.pojo;


import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLName;
import lombok.Data;

import java.util.Date;


@GraphQLName("internal")
@Data
public class Internal {
    @GraphQLField
    private String id;

    @GraphQLField
    private String subject;

    @GraphQLField
    private Integer typeAgreement;

    @GraphQLField
    private Date creationDate;

    @GraphQLField
    private String recipient;

    @GraphQLField
    private Boolean draft;
}
