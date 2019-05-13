package com.aptatek.pkulab.view.test;

import java.util.Arrays;
import java.util.List;

import ix.Ix;

public enum TestScreens {

    TURN_READER_ON(true),
    BREAK_FOIL(true),
    POKE_FINGERTIP(true),
    COLLECT_BLOOD(true),
    MIX_SAMPLE(true),
    WETTING(true),
    CONNECT_IT_ALL(true),
    TESTING(false),
    CANCEL(false);

    TestScreens(final boolean showAsDot) {
        this.showAsDot = showAsDot;
    }

    final boolean showAsDot;

    public static List<TestScreens> showDotFor() {
        return Ix.from(Arrays.asList(values())).filter(item -> item.showAsDot).toList();
    }

}
