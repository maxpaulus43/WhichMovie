package com.maxpaulus.skills.whichMovie.handlers;

import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;
import junitparams.mappers.DataMapper;
import org.junit.Assert;
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
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        handler.handleRequest(new ByteArrayInputStream(input.getBytes()), o, null);
        System.out.println(o.toString());
        Assert.assertFalse(o.toString().isEmpty());
    }

    @Test
    @FileParameters(value = "src/test/resources/test_requests/action_genre.json", mapper = FileToStringMapper.class)
    public void testActionGenre(String input) throws IOException {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        handler.handleRequest(new ByteArrayInputStream(input.getBytes()), o, null);
        System.out.println(o.toString());
        Assert.assertFalse(o.toString().isEmpty());

    }

    @Test
    @FileParameters(value = "src/test/resources/test_requests/launch_request.json", mapper = FileToStringMapper.class)
    public void testLaunchRequest(String input) throws IOException {
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        handler.handleRequest(new ByteArrayInputStream(input.getBytes()), o, null);
        System.out.println(o.toString());
        Assert.assertFalse(o.toString().isEmpty());

    }

    public static final class FileToStringMapper implements DataMapper {
        @Override
        public Object[] map(Reader reader) {
            return new Object[][]{{new BufferedReader(reader).lines().collect(Collectors.joining("\n"))}};
        }
    }
}