package com.example.GithubRestClient.repositories;



import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

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
     * Constructor which initialize RestClient using JDKClientHttpRequestFactory and ObjectMapper.
     * @param builder Builder of the RestClient.
     * @param objectMapper Jackson ObjectMapper.
     */
    public RepositoriesRestClient(RestClient.Builder builder){
        this.client = builder.baseUrl("https://api.github.com/")
                .requestFactory(new JdkClientHttpRequestFactory())
                .defaultHeader("Accept","application/json")
                .build();
    }

    /**
     * Method that uses GitHub API to request repositories information given username.
     * @param username GitHub username.
     * @return An Optional with List of all given username repositories, or empty Optional when the user does not exist.
     */
    public List<Repo> findByUsername(String username){
        Optional<List<Repo>> repos;

        try{
            repos = Optional.ofNullable(client.get()
                    .uri("/users/{username}/repos",username)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {}));
            for(final Repo repo : repos.orElse(Collections.emptyList())){
                Optional<List<Branch>> branches = Optional.ofNullable(client.get()
                        .uri("repos/" + repo.owner().login() + "/" + repo.name() + "/branches")
                        .retrieve()
                        .body(new ParameterizedTypeReference<>() {}));
                branches.orElse(Collections.emptyList()).stream().collect(Collectors.toCollection(repo::branches));
            }
        }catch (HttpClientErrorException ex){
            if(ex.getStatusCode().equals(HttpStatus.NOT_FOUND))
                throw new RepositoriesUserNotFoundException();
            else throw ex;
        }

        return repos.orElse(Collections.emptyList()).stream().filter(repo -> !repo.fork()).collect(Collectors.toList());
    }
}
