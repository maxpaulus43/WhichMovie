package com.maxpaulus.skills.whichMovie.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class WhichGenreHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("which_movie").and(this::hasNoGenre));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return input.getResponseBuilder()
                .addDelegateDirective(((IntentRequest) input.getRequestEnvelope().getRequest()).getIntent())
                .build();
    }

    private boolean hasNoGenre(HandlerInput i) {
        return i.getRequestEnvelope().getRequest() instanceof IntentRequest
                && ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots() != null
                && (!((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots().containsKey("genre")
                || null == ((IntentRequest) i.getRequestEnvelope().getRequest()).getIntent().getSlots().get("genre").getValue());
    }
}
