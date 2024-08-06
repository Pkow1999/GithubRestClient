package com.example.GithubRestClient.repositories;

public record RepositoriesErrorRecord(
        Integer status,
        String message
) {
}
