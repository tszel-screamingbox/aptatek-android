package com.aptatek.pkulab.view.test;

public enum TestScreens {

    PREP_TEST_KIT(true, 1),
    PREP_BUFFER(true, 2),
    UNSCREW_CAP(true, 3),
    PREP_BLUE_CAP(true,4),
    CLEAN_FINGERTIP(true, 5),
    POKE_FINGERTIP(true, 6),
    COLLECT_BLOOD(true, 7),
    ADD_SAMPLE(true, 8),
    MIX_SAMPLE(true, 9),
    WETTING(false, 10),
    TURN_READER_ON(false, -1),
    PREPARE_CASSETTE(true, 11),
    ATTACH_CHAMBER(false, 12),
    CONNECT_IT_ALL(false, 13),
    TESTING(false, 14),
    TEST_COMPLETE(false, 15),
    CANCEL(false, -1);

    TestScreens(final boolean showTestStep, final int testStep) {
        this.showTestStep = showTestStep;
        this.testStep = testStep;
    }

    public final boolean showTestStep;
    public final int testStep;

}
