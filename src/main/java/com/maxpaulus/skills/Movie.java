package com.maxpaulus.skills;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Movie {
    @JsonProperty("title")
    String title;
    @JsonProperty("overview")
    String overview;

    public String getTitle() {
        return title;
    }
}
