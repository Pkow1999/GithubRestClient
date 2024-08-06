package com.example.GithubRestClient.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.junit.jupiter.api.Assertions.*;

@RestClientTest(RepositoriesRestClient.class)
class RepositoriesRestClientTest {

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private RepositoriesRestClient restClient;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void foundUserWithRepos() throws JsonProcessingException {
        Repo repo = new Repo(
                "AOC2023",
                new ArrayList<>(Collections.singleton(new Branch("main", new Commit("f2140ada0bdba6c4dbf95615e6a230b744ab229a")))),
                new Owner("Pkow1999"),
                false
                );

        this.server.expect(requestTo("https://api.github.com/users/Pkow1999/repos"))
                .andRespond(withSuccess(mapper.writeValueAsString(repo), MediaType.APPLICATION_JSON));

        this.server.expect(requestTo("https://api.github.com/repos/Pkow1999/AOC2023/branches"))
                .andRespond(withSuccess(mapper.writeValueAsString(repo),MediaType.APPLICATION_JSON));

       List<Repo> trueRepos = restClient.findByUsername("Pkow1999");
       assertEquals(repo, trueRepos.getFirst());
    }
    @Test
    void userDoesNotExist(){

        this.server.expect(requestTo("https://api.github.com/users/repos"))
                .andRespond(withResourceNotFound());

        RepositoriesUserNotFoundException notFoundException = assertThrows(
                RepositoriesUserNotFoundException.class,
                () -> restClient.findByUsername("")
        );
        assertEquals(404,notFoundException.getStatusCode().value());
        assertEquals("User does not exist",notFoundException.getMessage());
    }
}