package com.example.GithubRestClient.repositories;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * A Record which holds all information of the Repository.
 * @param name Name of GitHub Repository.
 * @param branches List of all the branches of the Repository.
 * @param owner Username of the Owner.
 */


@JsonIgnoreProperties(ignoreUnknown = true)
public record Repo(
        String name,
        List<Branch> branches,
        Owner owner
) {
}
