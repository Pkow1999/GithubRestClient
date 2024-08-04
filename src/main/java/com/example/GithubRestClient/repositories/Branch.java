package com.example.GithubRestClient.repositories;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Record which hold information about the specific branch of the Repo.
 * @param name Name of the branch.
 * @param commit Information of the last commit on the branch.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public record Branch(
        String name,
        Commit commit
) {
}
