package kz.spring.workflow.graphql;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import kz.spring.workflow.domain.internal.Internal;
import kz.spring.workflow.facades.internal.impl.InternalFacadeImpl;
import kz.spring.workflow.request.internal.InternalSaveRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class InternalMutation implements GraphQLMutationResolver {

    private final InternalFacadeImpl internalFacade;

    @Autowired
    public InternalMutation(InternalFacadeImpl internalFacade) {
        this.internalFacade = internalFacade;

    }

    public Internal addInternal(InternalSaveRequest internalSaveRequest){
        return  internalFacade.saveInternal(internalSaveRequest);
    }
}
