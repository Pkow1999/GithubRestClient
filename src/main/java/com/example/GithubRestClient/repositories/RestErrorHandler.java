package com.example.GithubRestClient.repositories;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public @ResponseBody RepositoriesErrorRecord processUsernameNotFoundError(HttpClientErrorException exception){
        return new RepositoriesErrorRecord(exception.getStatusCode().value(), exception.getMessage());
    }
}