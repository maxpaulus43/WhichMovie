package com.maxpaulus.skills.whichMovie.handlers;

import com.amazon.ask.dispatcher.exception.ExceptionHandler;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.interfaces.system.ExceptionEncounteredRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;

public class ErrorHandler implements ExceptionHandler {
    @Override
    public boolean canHandle(HandlerInput input, Throwable throwable) {
        return input.matches(requestType(ExceptionEncounteredRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input, Throwable throwable) {

        Map<String, Object> errors = new HashMap<>();
        errors.put("throwable", throwable);

        input.getAttributesManager().setRequestAttributes(errors);

        return input.getResponseBuilder()
                .withSpeech("I'm sorry. I've encountered an error. Please try again.")
                .withShouldEndSession(true)
                .build();
    }
}
