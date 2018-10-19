package com.maxpaulus.skills.whichMovie.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.services.Serializer;
import com.amazon.ask.util.JacksonSerializer;
import com.maxpaulus.skills.whichMovie.model.Genre;
import com.maxpaulus.skills.whichMovie.model.Movie;
import com.maxpaulus.skills.whichMovie.model.MovieResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.amazon.ask.request.Predicates.intentName;
import static com.amazon.ask.request.Predicates.requestType;

public class WhichMovieHandler implements RequestHandler {

    private static final String TMDB_URL = "https://api.themoviedb.org";
    private static final String TMDB_API_KEY = System.getenv("TMDB_API_KEY");
    private static final int PAGE_LIMIT = 10;
    private static final Serializer serializer = new JacksonSerializer();

    private final ThreadLocalRandom random;
    private final HttpClient httpClient;

    public WhichMovieHandler(HttpClient httpClient) {
        this.httpClient = httpClient;
        this.random = ThreadLocalRandom.current();
    }

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class)
                .or(intentName("which_movie")));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

        if (!hasGenres(input)) {
            return input.getResponseBuilder()
                    .addDelegateDirective(((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent())
                    .build();
        }

        String[] genres = ((IntentRequest) (input.getRequestEnvelope().getRequest()))
                .getIntent()
                .getSlots()
                .get("genre")
                .getValue()
                .split(" ");

        Movie movie = getRandomMovie(genres);

        return input.getResponseBuilder()
                .withSpeech(movie == null
                        ? "I can't find any movies at this time, please try again"
                        : "I recommend " + movie.getTitle() + ". Here's a brief summary. " + movie.getOverview())
                .withShouldEndSession(true)
                .build();
    }

    private boolean hasGenres(HandlerInput i) {
        return i.getRequestEnvelope().getRequest() instanceof IntentRequest
                && ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots() != null
                && ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots().containsKey("genre")
                && ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots().get("genre").getValue() != null;
    }

    private Movie getRandomMovie(String... genres) {
        List<Movie> movies = getMovies(genres);

        if (movies == null || movies.isEmpty()) {
            return null;
        }

        int i = random.nextInt(movies.size());
        return movies.get(i);
    }

    private List<Movie> getMovies(String... genres) {
        if (genres.length == 0) {
            genres = new String[]{""};
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
                            .map(g -> String.valueOf(g.id))
                            .collect(Collectors.joining(",")))
                    .addParameter("page", String.valueOf(random.nextInt(PAGE_LIMIT)))
                    .build();

            HttpResponse response = httpClient.execute(new HttpGet(uri));

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
