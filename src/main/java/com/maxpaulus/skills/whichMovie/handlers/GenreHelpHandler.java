package com.maxpaulus.skills.whichMovie.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.maxpaulus.skills.whichMovie.model.Genre;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static com.amazon.ask.request.Predicates.intentName;

public class GenreHelpHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.HelpIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return input.getResponseBuilder()
                .withSpeech(String.format("Try saying \"Ask which movie for a %s recommendation\".", randomSelectionFrom(Genre.values())))
                .withShouldEndSession(false)
                .build();
    }

    private <T> T randomSelectionFrom(T[] list) {
        if (list == null || list.length == 0) {
            return null;
        }

        return list[ThreadLocalRandom.current().nextInt(list.length)];
    }
}
