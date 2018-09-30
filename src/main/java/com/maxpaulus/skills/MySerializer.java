package com.maxpaulus.skills;

import com.amazon.ask.exception.AskSdkException;
import com.amazon.ask.model.services.Serializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MySerializer implements Serializer {
    private static ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.registerModule(new JavaTimeModule());
    }

    /**
     * For testing purposes.
     */
    static void setMapper(ObjectMapper objectMapper) {
        mapper = objectMapper;
    }

    @Override
    public <T> String serialize(T t) {
        try {
            return mapper.writeValueAsString(t);
        } catch (IOException e) {
            throw new AskSdkException("Serialization error", e);
        }
    }

    @Override
    public <T> void serialize(T object, OutputStream outputStream) {
        try {
            mapper.writeValue(outputStream, object);
        } catch (IOException e) {
            throw new AskSdkException("Serialization error");
        }
    }

    @Override
    public <T> T deserialize(String s, Class<T> aClass) {
        try {
            return mapper.readValue(s, aClass);
        } catch (IOException e) {
            throw new AskSdkException("Deserialization error", e);
        }
    }

    @Override
    public <T> T deserialize(InputStream inputStream, Class<T> type) {
        try {
            return mapper.readValue(inputStream, type);
        } catch (IOException e) {
            throw new AskSdkException("Deserialization error", e);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> T deserialize(InputStream inputStream, TypeReference valueTypeRef) {
        try {
            return mapper.readValue(inputStream, valueTypeRef);
        } catch (IOException e) {
            throw new AskSdkException("Deserialization error", e);
        }
    }
}
