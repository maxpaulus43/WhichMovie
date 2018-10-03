package com.maxpaulus.skills.whichMovie.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieResponse {
    @JsonProperty("page")
    int page;
    @JsonProperty("results")
    List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }
}
