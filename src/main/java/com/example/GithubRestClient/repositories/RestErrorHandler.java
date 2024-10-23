package com.example.GithubRestClient.repositories;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler(RepositoriesUserNotFoundException.class)
    public @ResponseBody RepositoriesUserNotFoundRecord processUsernameNotFoundError(RepositoriesUserNotFoundException exception){
        return new RepositoriesUserNotFoundRecord (exception.getStatusCode().value(), exception.getMessage());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public @ResponseBody ResponseEntity<Object> processClientError(HttpClientErrorException exception)
    {
        String bodyString = "{ \"status\":" + exception.getStatusCode().value() + "," + exception.getResponseBodyAsString().replaceFirst("\\{", "");
        return ResponseEntity
            .status(exception.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body(bodyString);
    }
}