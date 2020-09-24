package kz.spring.workflow.graphql;

import graphql.schema.*;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Component
public class ObjectScalarType extends GraphQLScalarType {
    public ObjectScalarType() {
        super("ObjectRedux", "ObjectRedux", new Coercing() {
            @Override
            public Object serialize(Object o) throws CoercingSerializeException {
                return o.toString();
            }

            @Override
            public Object parseValue(Object o) throws CoercingParseValueException {
                return o;
            }

            @Override
            public Object parseLiteral(Object o) throws CoercingParseLiteralException {
                if (o==null){
                    return null;
                }
                return o.toString();
            }
        });
    }
}
