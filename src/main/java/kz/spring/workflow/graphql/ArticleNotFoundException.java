package kz.spring.workflow.graphql;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ArticleNotFoundException extends RuntimeException implements GraphQLError {
    private Long articleId;

    public ArticleNotFoundException(Long articleId) {
        this.articleId = articleId;
    }

    @Override
    public String getMessage() {
        return "Article with ID " + articleId + " could not be found";
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.ValidationError;
    }

    @Override
    public Map<String, Object> getExtensions() {
        return Collections.singletonMap("articleId", articleId);
    }
}
