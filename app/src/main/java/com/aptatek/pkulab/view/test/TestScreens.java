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
    MIX_SAMPLE(true),
    WETTING(true),
    TURN_READER_ON(true),
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


//    PREPARE_TEST_KIT,
//    PREPARE_BUFFER_CHAMBER,
//    UNSCREW_CAP,
//    PREPARE_BLUE_COLLECTION_CAP,
//    CLEAN_FINGERTIP,
//    POKE_FINGERTIP,
//    COLLECT_BLOOD,
//    ADD_SAMPLE,
//    MIX_SAMPLE,
//    WETTING,
//    WETTING_DONE,
//    TURN_READER_ON,
//    SELF_CHECK,
//    PREPARE_CASSETTE,
//    ATTACH_CHAMBER,
//    INSERT_CASSETTE,
//    TESTING,
//    TEST_COMPLETE,
//    CANCEL;

}
