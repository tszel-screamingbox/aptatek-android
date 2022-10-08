package com.aptatek.pkulab.view.test;

import java.util.Arrays;
import java.util.List;

import ix.Ix;

public enum TestScreens {

    PREP_TEST_KIT(true),
    PREP_BUFFER(true),
    UNSCREW_CAP(true),
    PREP_BLUE_CAP(true),
    CLEAN_FINGERTIP(true),
    POKE_FINGERTIP(true),
    COLLECT_BLOOD(true),
    ADD_SAMPLE(true),
    MIX_SAMPLE(true),
    WETTING(true),
    TURN_READER_ON(true),
    PREPARE_CASSETTE(true),
    ATTACH_CHAMBER(true),
    CONNECT_IT_ALL(true),
    TESTING(false),
    TEST_COMPLETE(false),
    CANCEL(false);

    TestScreens(final boolean showAsDot) {
        this.showAsDot = showAsDot;
    }

    final boolean showAsDot;

    public static List<TestScreens> showDotFor() {
        return Ix.from(Arrays.asList(values())).filter(item -> item.showAsDot).toList();
    }

}
