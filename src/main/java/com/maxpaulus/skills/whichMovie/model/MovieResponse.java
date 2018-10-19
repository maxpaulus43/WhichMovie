package com.maxpaulus.skills.whichMovie.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieResponse {
    private int page;
    private List<Movie> movies;

    public List<Movie> getMovies() {
        return movies;
    }

    public int getPage() {
        return page;
    }

    @JsonProperty("page")
    public void setPage(int page) {
        this.page = page;
    }

    @JsonProperty("results")
    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
