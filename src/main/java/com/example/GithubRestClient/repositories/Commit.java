package com.example.GithubRestClient.repositories;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Record which holds SHA of the last commit on the specific branch.
 * @param sha SHA of the last commit.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Commit(
        String sha
) {
}
