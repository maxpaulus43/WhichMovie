package com.maxpaulus.skills.whichMovie;

import com.amazon.ask.Skill;
import com.amazon.ask.builder.SkillConfiguration;
import com.amazon.ask.dispatcher.request.handler.impl.DefaultHandlerAdapter;
import com.amazon.ask.dispatcher.request.handler.impl.DefaultRequestHandlerChain;
import com.amazon.ask.dispatcher.request.mapper.impl.DefaultRequestMapper;
import com.maxpaulus.skills.whichMovie.handlers.ErrorHandler;
import com.maxpaulus.skills.whichMovie.handlers.WhichGenreHandler;
import com.maxpaulus.skills.whichMovie.handlers.WhichMovieHandler;

public final class WhichMovieSkill extends Skill {

    private static final String SKILL_ID = "amzn1.ask.skill.35dcc72a-673f-4d47-9172-2a37ceb7d6ba";

    public WhichMovieSkill() {
        super(config());
    }

    private static SkillConfiguration config() {
        return SkillConfiguration.builder()
                .withSkillId(SKILL_ID)
                .addHandlerAdapter(new DefaultHandlerAdapter())
                .addRequestMapper(DefaultRequestMapper.builder()
                        .addRequestHandlerChain(DefaultRequestHandlerChain.builder()
                                .withRequestHandler(new WhichGenreHandler())
                                .addExceptionHandler(new ErrorHandler())
                                .build())
                        .addRequestHandlerChain(DefaultRequestHandlerChain.builder()
                                .withRequestHandler(new WhichMovieHandler())
                                .addExceptionHandler(new ErrorHandler())
                                .build())
                        .build())
                .build();
    }
}