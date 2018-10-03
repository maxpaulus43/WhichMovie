package com.maxpaulus.skills.whichMovie.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ResponseEnvelope;
import com.amazon.ask.model.services.Serializer;
import com.amazon.ask.model.ui.PlainTextOutputSpeech;
import com.amazon.ask.request.Predicates;
import com.amazon.ask.util.JacksonSerializer;
import com.maxpaulus.skills.whichMovie.model.Genre;
import com.maxpaulus.skills.whichMovie.model.Movie;
import com.maxpaulus.skills.whichMovie.model.MovieRequest;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class WhichMovieHandler implements RequestHandler {
    private final String TMDB_URL = "https://api.themoviedb.org";
    private final String TMDB_API_KEY = System.getenv("TMDB_API_KEY");
    private static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault();
    private static final Serializer serializer = new JacksonSerializer();

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.intentName("which_movie"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return input.getResponseBuilder()
                .addElicitSlotDirective("genre", Intent.builder()
                        .build())
                .withShouldEndSession(false)
                .build();
    }

    private Movie getRandomMovie(Genre... genres) {
        List<Movie> movies = getMovies(genres);

        if (movies == null || movies.isEmpty()) {
            return null;
        }

        int i = new Random().nextInt(movies.size());
        return movies.get(i);
    }

    private List<Movie> getMovies(Genre... genres) {
        if (genres.length == 0) {
            genres = new Genre[]{Genre.ANY};
        }

        MovieRequest movieRequest = new MovieRequest();

        List<Movie> movies = new ArrayList<>();

        try {
            URI uri = new URIBuilder(TMDB_URL)
                    .setPath("/3/movie/popular")
                    .addParameter("api_key", TMDB_API_KEY)
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

    private ResponseEnvelope emptyResponseEnvelope() {
        return ResponseEnvelope.builder()
                .withResponse(Response.builder()
                        .withOutputSpeech(PlainTextOutputSpeech.builder()
                                .withText("No movies are available at this time")
                                .build())
                        .withShouldEndSession(true)
                        .build())
                .build();
    }
}
