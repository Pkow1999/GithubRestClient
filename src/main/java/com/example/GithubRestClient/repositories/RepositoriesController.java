package com.example.GithubRestClient.repositories;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("github")
public class RepositoriesController {
    private final RepositoriesRestClient restClient;

    RepositoriesController(RepositoriesRestClient restClient){
        this.restClient = restClient;
    }
    @GetMapping("/{username}")
    List<Repo> findRepos(@PathVariable String username) {
            return restClient.findByUsername(username);
    }
}
