package com.maxpaulus.skills.whichMovie.handlers;

import com.amazon.ask.SkillStreamHandler;
import com.maxpaulus.skills.whichMovie.WhichMovieSkill;

public class WhichMovieSkillStreamHandler extends SkillStreamHandler {
    public WhichMovieSkillStreamHandler() {
        super(new WhichMovieSkill());
    }
}
