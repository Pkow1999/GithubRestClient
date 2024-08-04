package com.example.GithubRestClient.repositories;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Record that holds information about Owner of specific Repository.
 * @param login GitHub login of the Owner of specific Repository.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record Owner(String login) {
}
