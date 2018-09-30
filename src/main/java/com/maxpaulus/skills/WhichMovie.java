package com.maxpaulus.skills;

import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.ResponseEnvelope;
import com.amazon.ask.model.ui.PlainTextOutputSpeech;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class WhichMovie implements RequestStreamHandler {
    private final MySerializer SERIALIZER = new MySerializer();
    private final String TMDB_URL = "https://api.themoviedb.org";
    private final String TMDB_API_KEY = System.getenv("TMDB_API_KEY");

    private static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault();

    private static final ResponseEnvelope EMPTY_RESPONSE = ResponseEnvelope.builder()
            .withResponse(Response.builder()
                    .withOutputSpeech(PlainTextOutputSpeech.builder()
                            .withText("No movies are available at this time")
                            .build())
                    .withShouldEndSession(true)
                    .build())
            .build();

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        //RequestEnvelope request = SERIALIZER.deserialize(input, RequestEnvelope.class); todo

        Movie movie = getRandomMovie();

        ResponseEnvelope response = EMPTY_RESPONSE;

        if (movie != null) {
            response = ResponseEnvelope.builder()
                    .putSessionAttributesItem("movie_id", movie.getId())
                    .withResponse(Response.builder()
                            .withOutputSpeech(PlainTextOutputSpeech.builder()
                                    .withText("I recommend " + movie.getTitle())
                                    .build())
                            .withShouldEndSession(false)
                            .build())
                    .build();
        }

        SERIALIZER.serialize(response, output);
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
            movies = SERIALIZER.deserialize(in, MovieResponse.class).getMovies();
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
