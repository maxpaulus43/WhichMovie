package com.maxpaulus.skills.whichMovie.handlers;

import com.amazon.ask.model.ResponseEnvelope;
import com.fasterxml.jackson.databind.ObjectMapper;
import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import junitparams.mappers.DataMapper;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.util.stream.Collectors;

@RunWith(JUnitParamsRunner.class)
public class SkillHandlerTest {

    private final SkillHandler handler = new SkillHandler();

    @Test
    @FileParameters(value = "src/test/resources/test_requests/ask_which_movie_is_good.json", mapper = FileToStringMapper.class)
    public void testWhichMovieIsGood(String input) throws IOException {
        handler.handleRequest(new ByteArrayInputStream(input.getBytes()), System.out, null);
    }

    @Test
    @FileParameters(value = "src/test/resources/test_requests/action_genre.json", mapper = FileToStringMapper.class)
    public void testActionGenre(String input) throws IOException {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        handler.handleRequest(new ByteArrayInputStream(input.getBytes()), o, null);
        new ObjectMapper().readValue(o.toString(), ResponseEnvelope.class);
    }

    public static final class FileToStringMapper implements DataMapper {
        @Override
        public Object[] map(Reader reader) {
            return new Object[][]{{new BufferedReader(reader).lines().collect(Collectors.joining("\n"))}};
        }
    }
}