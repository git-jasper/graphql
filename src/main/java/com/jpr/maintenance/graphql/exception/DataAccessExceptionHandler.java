package com.jpr.maintenance.graphql.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.execution.DataFetcherExceptionHandler;
import graphql.execution.DataFetcherExceptionHandlerParameters;
import graphql.execution.DataFetcherExceptionHandlerResult;
import graphql.execution.SimpleDataFetcherExceptionHandler;
import org.springframework.dao.DataAccessException;

import java.util.concurrent.CompletionException;

import static com.jpr.maintenance.validation.errors.InputValidationError.DATA_ACCESS_ERROR;

@SuppressWarnings("deprecation")
public class DataAccessExceptionHandler implements DataFetcherExceptionHandler {

    private final DataFetcherExceptionHandler parent = new SimpleDataFetcherExceptionHandler();

    @Override
    public DataFetcherExceptionHandlerResult onException(DataFetcherExceptionHandlerParameters handlerParameters) {
        Throwable exception = this.unwrap(handlerParameters.getException());
        if (exception instanceof DataAccessException) {
            GraphQLError error = GraphqlErrorBuilder
                .newError(handlerParameters.getDataFetchingEnvironment())
                .message(DATA_ACCESS_ERROR.getErrorMessage(""))
                .errorType(DATA_ACCESS_ERROR)
                .build();
            return DataFetcherExceptionHandlerResult.newResult().error(error).build();
        }
        return parent.onException(handlerParameters);
    }

    protected Throwable unwrap(Throwable exception) {
        return exception.getCause() != null && exception instanceof CompletionException ? exception.getCause() : exception;
    }
}
