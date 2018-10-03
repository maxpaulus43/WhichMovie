package com.maxpaulus.skills.whichMovie.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.services.Serializer;
import com.amazon.ask.util.JacksonSerializer;
import com.maxpaulus.skills.whichMovie.model.Genre;
import com.maxpaulus.skills.whichMovie.model.Movie;
import com.maxpaulus.skills.whichMovie.model.MovieResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static com.amazon.ask.request.Predicates.intentName;

public class WhichMovieHandler implements RequestHandler {

    private static final String TMDB_URL = "https://api.themoviedb.org";
    private static final String TMDB_API_KEY = System.getenv("TMDB_API_KEY");
    private static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault();
    private static final Serializer serializer = new JacksonSerializer();

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("which_movie").and(this::hasSlots));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        Slot genreSlot = ((IntentRequest) (input.getRequestEnvelope().getRequest()))
                .getIntent().getSlots().get("genre");

        Movie movie = getRandomMovie(genreSlot.getValue());

        return input.getResponseBuilder()
                .withSpeech(movie == null
                        ? "I can't find any movies, please try again"
                        : "I recommend " + movie.getTitle())
                .withShouldEndSession(true)
                .build();
    }

    private boolean hasSlots(HandlerInput input) {
        Map<String, Slot> slots =
                ((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent().getSlots();
        return slots != null && !slots.isEmpty();
    }

    private Movie getRandomMovie(String... genres) {
        List<Movie> movies = getMovies(genres);

        if (movies == null || movies.isEmpty()) {
            return null;
        }

        int i = new Random().nextInt(movies.size());
        return movies.get(i);
    }

    private List<Movie> getMovies(String... genres) {
        if (genres.length == 0) {
            genres = new String[]{""}; // todo find a better default genre
        }

        List<Movie> movies = new ArrayList<>();

        try {
            URI uri = new URIBuilder(TMDB_URL)
                    .setPath("/3/discover/movie")
                    .addParameter("language", "en-US")
                    .addParameter("sort_by", "popularity.desc")
                    .addParameter("api_key", TMDB_API_KEY)
                    .addParameter("with_genres", Arrays.stream(genres)
                            .map(Genre::fromString)
                            .filter(Objects::nonNull)
                            .map(g -> g.id)
                            .map(String::valueOf)
                            .collect(Collectors.joining(",")))
                    .addParameter("page", String.valueOf(new Random().nextInt(10))) // top 10 pages
                    .build();

            CloseableHttpResponse response = HTTP_CLIENT.execute(new HttpGet(uri));

            InputStream in = response.getEntity().getContent();
            movies = serializer.deserialize(in, MovieResponse.class).getMovies();
            in.close();

        } catch (URISyntaxException e) {
            System.err.println("couldn't create url");
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            System.err.println("client protocol problem");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("couldn't make request");
            e.printStackTrace();
        }

        return movies;
    }

}
