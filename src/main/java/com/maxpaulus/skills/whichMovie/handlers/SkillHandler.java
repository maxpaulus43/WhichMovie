package com.maxpaulus.skills.whichMovie.handlers;

import com.amazon.ask.SkillStreamHandler;
import com.maxpaulus.skills.whichMovie.WhichMovieSkill;

public class SkillHandler extends SkillStreamHandler {
    public SkillHandler() {
        super(new WhichMovieSkill());
    }
}
