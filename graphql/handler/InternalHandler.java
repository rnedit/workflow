package kz.spring.workflow.graphql.handler;
//
//import graphql.ExecutionInput;
//import graphql.ExecutionResult;
//import graphql.GraphQL;
//
//import graphql.annotations.processor.GraphQLAnnotations;
//import graphql.schema.GraphQLObjectType;
//import graphql.schema.GraphQLSchema;
//import kz.spring.workflow.domain.internal.Internal;
//import kz.spring.workflow.graphql.utils.SchemaUtils;
//import lombok.extern.log4j.Log4j2;
//import ratpack.handling.Context;
//import ratpack.handling.Handler;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//import static graphql.schema.GraphQLSchema.newSchema;
//import static ratpack.jackson.Jackson.json;
//
//@Log4j2
//public class InternalHandler implements Handler {
//
//    @Override
//    public void handle(Context context) throws Exception {
//        GraphQLObjectType object = GraphQLAnnotations.object(Internal.class);
//        GraphQLSchema schema = newSchema().query(object).build();
//        GraphQL graphQL = GraphQL.newGraphQL(schema).build();
//log.info("enter");
//        context.parse(Map.class)
//                .then(payload -> {
//                    Map<String, Object> parameters = (Map<String, Object>)
//                            payload.get("parameters");
//                    ExecutionInput executionInput =
//                            ExecutionInput.newExecutionInput()
//                                    .query(SchemaUtils.QUERY.name())
//                                    .operationName(null)
//                                    .variables(parameters)
//                                    .context(this)
//                                    .build();
//
//                    ExecutionResult executionResult = graphQL
//                            .execute(executionInput);
////                            .execute(payload.get(SchemaUtils.QUERY)
////                                    .toString(), null, this, parameters);
//                    Map<String, Object> result = new LinkedHashMap<>();
//                    if (executionResult.getErrors().isEmpty()) {
//                        result.put(SchemaUtils.DATA.name(), executionResult.getData());
//                    } else {
//                        result.put(SchemaUtils.ERRORS.name(), executionResult.getErrors());
//                        log.warn("Errors: " + executionResult.getErrors());
//                    }
//                    context.render(json(result));
//                });
//    }
//}
