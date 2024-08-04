package com.example.GithubRestClient.repositories;

import org.springframework.http.HttpStatus;

import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.StandardCharsets;

/**
 * An Exception which is thrown when the GitHub username does not exist.
 */
public class RepositoriesUserNotFound extends HttpClientErrorException {
    /**
     * Default constructor
     */
    public RepositoriesUserNotFound(){
        super(HttpStatus.NOT_FOUND, ": User does not exist", StandardCharsets.ISO_8859_1.encode("{\n" +
                "\t\"status\": " + HttpStatus.NOT_FOUND.value() +
                "\n\t\"message\": User does not exist\n" +
                "}").array(), StandardCharsets.ISO_8859_1);
    }
}
