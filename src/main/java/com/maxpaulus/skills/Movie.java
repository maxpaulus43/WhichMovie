package com.maxpaulus.skills;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {
    @JsonProperty("title")
    String title;
    @JsonProperty("overview")
    String overview;
    @JsonProperty("id")
    int id;

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }
}
