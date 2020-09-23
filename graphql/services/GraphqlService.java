package kz.spring.workflow.graphql.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import kz.spring.workflow.repository.Impl.InternalDALImpl;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Log4j2
@Service
public class GraphqlService {
    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private GraphQL graphQL;
//
//    @Async
//    public CompletableFuture<ExecutionResult> executeQuery(String query) {
//        log.info("CompletableFuture");
//        InputQuery queryObject = new InputQuery(query);
//        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
//                .query(queryObject.query)
//                .context(appContext)
//                //.variables(queryObject.variables)
//                //.root(InternalMutation)
//                .build();
//        //GraphQL graphQL = appContext.getBean(GraphQL.class);
//        return CompletableFuture.completedFuture(graphQL.execute(executionInput));
//    }


    @ToString
    private class InputQuery {
        String query;
        Map<String, Object> variables = new HashMap<>();

        InputQuery(String query) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                Map<String, Object> jsonBody = mapper.readValue(query, new TypeReference<Map<String, Object>>() {
                });
                this.query = (String) jsonBody.get("query");
                this.variables = (Map<String, Object>) jsonBody.get("variables");
            } catch (IOException ignored) {
                this.query = query;
            }
        }
    }
}
