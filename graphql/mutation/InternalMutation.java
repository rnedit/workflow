package kz.spring.workflow.graphql.mutation;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLName;
import graphql.annotations.annotationTypes.GraphQLRelayMutation;
import graphql.schema.DataFetchingEnvironment;
import kz.spring.workflow.repository.Impl.InternalDALImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@GraphQLName("Mutation")
public class InternalMutation {

    @Autowired
    InternalDALImpl internalDAL;

    @GraphQLRelayMutation
    @GraphQLField
    public static String createInternal(
            DataFetchingEnvironment env,
            @NotNull @GraphQLName("name") String name,
            @NotNull @GraphQLName("email") String email) {

        return "Ё Типо создали";
    }
}
