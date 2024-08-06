package com.example.GithubRestClient.repositories;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

/**
 * An Exception which is thrown when the GitHub username does not exist.
 */

public class RepositoriesUserNotFoundException extends HttpClientErrorException {
    private Integer responseCode = HttpStatus.NOT_FOUND.value();
    private String message = "User does not exist";
    /**
     * Default constructor
     */
    public RepositoriesUserNotFoundException(){
        super(HttpStatus.NOT_FOUND);
    }

    @Override
    public String getMessage() {
        return message;
    }
}