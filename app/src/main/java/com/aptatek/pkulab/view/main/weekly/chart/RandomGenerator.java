package com.aptatek.pkulab.view.main.weekly.chart;

import java.util.Random;

import javax.inject.Inject;

public class RandomGenerator {

    @Inject
    public RandomGenerator() {

    }

    public boolean maybe() {
        return new Random().nextBoolean();
    }
}
