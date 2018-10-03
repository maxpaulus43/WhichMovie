package com.maxpaulus.skills.whichMovie.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.Predicates;

import java.util.Optional;

public class WhichGenreHandler implements RequestHandler {
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(Predicates.intentName("which_genre"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return Optional.empty();
    }
}
