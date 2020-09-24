package kz.spring.workflow.graphql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.schema.*;
import kz.spring.workflow.request.files.InfoFile;
import kz.spring.workflow.request.internal.InternalTableRequest;
import org.springframework.stereotype.Component;

//@Component
public class InternalTableRequestScalarType  extends GraphQLScalarType {

    public InternalTableRequestScalarType() {
        super("InternalTableRequest", "Описание запроса пользователя", new Coercing() {
            @Override
            public Object serialize(Object o) throws CoercingSerializeException {
                return o!=null?o.toString():null;
            }

            @Override
            public Object parseValue(Object o) throws CoercingParseValueException {
                ObjectMapper objectMapper = new ObjectMapper();
                InternalTableRequest internalTableRequest = new InternalTableRequest();
                try {
                    InternalTableRequest map = objectMapper.readValue(o.toString(), InternalTableRequest.class);
                    internalTableRequest.setUserId(map.getUserId());
                    internalTableRequest.setPage(map.getPage());
                    internalTableRequest.setPageSize(map.getPageSize());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                return internalTableRequest;
            }

            @Override
            public Object parseLiteral(Object o) throws CoercingParseLiteralException {
                ObjectMapper objectMapper = new ObjectMapper();
                InternalTableRequest internalTableRequest = new InternalTableRequest();
                try {
                    InternalTableRequest map = objectMapper.readValue(o.toString(), InternalTableRequest.class);
                    internalTableRequest.setUserId(map.getUserId());
                    internalTableRequest.setPage(map.getPage());
                    internalTableRequest.setPageSize(map.getPageSize());
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                return internalTableRequest;
            }
        });
    }
}
