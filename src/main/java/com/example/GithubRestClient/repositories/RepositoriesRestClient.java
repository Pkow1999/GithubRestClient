package com.example.GithubRestClient.repositories;


import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Spring Boot Component Class in which we implement REST client to deal with our task.
 */

@Component
public class RepositoriesRestClient {
    /**
     * Spring Boot RestClient which handles our API Requests.
     */
    private final RestClient client;
    /**
     * Jackson ObjectMapper which handles JSON format.
     */
    private final ObjectMapper objectMapper;

    /**
     * Constructor which initialize RestClient using JDKClientHttpRequestFactory and ObjectMapper.
     * @param builder Builder of the RestClient.
     * @param objectMapper Jackson ObjectMapper.
     */
    public RepositoriesRestClient(RestClient.Builder builder, ObjectMapper objectMapper){
        this.client = builder.baseUrl("https://api.github.com/")
                .requestFactory(new JdkClientHttpRequestFactory())
                .defaultHeader("Accept","application/json")
                .build();
        this.objectMapper = objectMapper;
    }

    /**
     * Method that uses GitHub API to request repositories information given username.
     * @param username GitHub username.
     * @return An Optional with List of all given username repositories, or empty Optional when the user does not exist.
     */
    public List<Repo> findByUsername(String username){
        List<Repo> repos;

        try{
            repos = client.get()
                    .uri("/users/{username}/repos",username)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
            assert repos != null;
            for(final Repo repo : repos){
                List<Branch> branches = client.get()
                        .uri("repos/" + repo.owner().login() + "/" + repo.name() + "/branches")
                        .retrieve()
                        .body(new ParameterizedTypeReference<>() {});
                assert branches != null;
                branches.stream().collect(Collectors.toCollection(repo::branches));
            }
        }catch (HttpClientErrorException ex){
            if(ex.getStatusCode().equals(HttpStatus.NOT_FOUND))
                throw new RepositoriesUserNotFoundException();
            else throw ex;
        }

        return repos.stream().filter(repo -> !repo.fork()).collect(Collectors.toList());
    }
}
