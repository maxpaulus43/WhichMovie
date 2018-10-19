package com.maxpaulus.skills.whichMovie.model;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum Genre {

    ACTION(28),
    ADVENTURE(12),
    ANIMATION(16),
    COMEDY(35),
    CRIME(80),
    DOCUMENTARY(99),
    DRAMA(18),
    FAMILY(10751),
    FANTASY(14),
    HISTORY(36),
    HORROR(27),
    MUSIC(10402),
    MYSTERY(9648),
    ROMANCE(10749),
    SCIENCE_FICTION(878),
    TV_MOVIE(10770),
    THRILLER(53),
    WAR(10752),
    WESTERN(37);

    public static final  Map<String, Genre> NAME_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(Genre::toString, g -> g));

    public final int id;

    Genre(int id) { this.id = id; }

    public static Genre fromString(String genreString) {
        return genreString == null || genreString.isEmpty() ? null : NAME_MAP.get(genreString.toUpperCase());
    }

    @Override
    public String toString() {
        return super.toString().replace("_", " ");
    }
}
