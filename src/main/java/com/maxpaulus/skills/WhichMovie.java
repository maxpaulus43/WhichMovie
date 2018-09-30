package com.maxpaulus.skills;

import com.amazon.ask.model.Response;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;

public class WhichMovie implements RequestHandler<Map<String, Object>, Response> {
    public Response handleRequest(Map<String, Object> input, Context context) {
        System.out.println(input);
        return Response.builder().build();
    }
}
