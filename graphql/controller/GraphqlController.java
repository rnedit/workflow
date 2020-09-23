package kz.spring.workflow.graphql.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import kz.spring.workflow.graphql.services.GraphqlService;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Log4j2
//@RestController
public class GraphqlController {
    @Autowired
    private GraphqlService graphqlService;
    @Autowired
    private GraphQL graphQL;
    @Autowired
    private ApplicationContext appContext;

    @CrossOrigin
    @RequestMapping(path = "/graphql", method = RequestMethod.POST)
    public ResponseEntity<?> getTransaction(@RequestBody String query) {
        log.info(query);
       // CompletableFuture<?> respond = graphqlService.executeQuery(query);
       // return respond.thenApply(r -> new ResponseEntity<>(r, HttpStatus.OK));
        InputQuery queryObject = new InputQuery(query);
        //log.info("queryObject.query="+queryObject.query);
        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                .query(queryObject.query)
                .context(appContext)
                //.variables(queryObject.variables)
                //.root(InternalMutation)
                .build();
        log.info("ServerQ="+executionInput.getQuery());
        ExecutionResult executeRes = graphQL.execute(executionInput);
        return new ResponseEntity(executeRes, HttpStatus.OK);
    }
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
