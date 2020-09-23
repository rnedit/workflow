package kz.spring.workflow.graphql.query;


import graphql.annotations.annotationTypes.GraphQLDescription;
import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLName;
import graphql.schema.DataFetchingEnvironment;
import kz.spring.workflow.domain.internal.Internal;
import kz.spring.workflow.repository.Impl.InternalDALImpl;
import org.springframework.context.ApplicationContext;

import javax.validation.constraints.NotNull;
import java.util.List;

@GraphQLName("query")
public class InternalQuery {

//    static
//    private
//    InternalDALImpl internalDAL;
//
//    public InternalQuery(InternalDALImpl internalDAL) {
//        this.internalDAL = internalDAL;
//    }


    @GraphQLName("retrieveInternal")
    @GraphQLField
    public static Internal retrieveInternal(
            DataFetchingEnvironment env,
            @NotNull @GraphQLName("id") String id) {

        ApplicationContext context = env.getContext();
        InternalDALImpl internalDAL = context.getBean(InternalDALImpl.class);
       return internalDAL.getInternal(id);
    }

    @GraphQLName("listInternals")
    @GraphQLDescription("ADASDAD")
    @GraphQLField
    public static List<Internal> listInternals(DataFetchingEnvironment env) {
        ApplicationContext context = env.getContext();
        InternalDALImpl internalDAL = context.getBean(InternalDALImpl.class);
        return internalDAL.getAllInternals();
    }
}
