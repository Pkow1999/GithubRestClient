package com.example.GithubRestClient.repositories;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

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
     * @throws JsonProcessingException An Exception which is thrown when Jackson found a problem when processing Json file.
     */
    public Optional<List<Repo>> findByUsername(String username) throws JsonProcessingException {
        final ArrayNode finalRepo = objectMapper.createArrayNode();
        try{
            ArrayNode userRepos = client.get()
                    .uri("/users/{username}/repos",username)
                    .retrieve()
                    .body(ArrayNode.class);

            assert userRepos != null;

            for(JsonNode userRepo : userRepos){
                if(userRepo.get("fork").isBoolean() && userRepo.get("fork").booleanValue())
                {
                    continue;
                }
                String full_name = userRepo.get("full_name").asText();

                JsonNode branchInfo = client.get()
                        .uri("repos/" + full_name + "/branches")
                        .retrieve()
                        .body(JsonNode.class);

                ((ObjectNode) userRepo).putIfAbsent("branches",branchInfo);
                finalRepo.add(userRepo);
            }
        } catch (HttpClientErrorException exception){
            throw new RepositoriesUserNotFound();
        }

        return Optional.ofNullable(objectMapper.readValue(finalRepo.toString(), new TypeReference<List<Repo>>() {}));
    }
}
