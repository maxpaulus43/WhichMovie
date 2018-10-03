package com.maxpaulus.skills.whichMovie;

import com.amazon.ask.Skill;
import com.amazon.ask.builder.SkillConfiguration;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.Arrays;

public class WhichMovieSkill extends Skill {

    public WhichMovieSkill() {
        super(config());
    }

    private static SkillConfiguration config() {
        return SkillConfiguration.builder()
                .withSkillId("which_movie")
                .withPersistenceAdapter(/* todo */ null)
                .withRequestMappers(Arrays.asList( // todo

                ))
                .build(); // todo configure
    }
}
