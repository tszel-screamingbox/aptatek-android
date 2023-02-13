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
    WETTING(true, 10),
    TURN_READER_ON(false, -1),
    PREPARE_CASSETTE(true, 13),
    ATTACH_CHAMBER(true, 14),
    CONNECT_IT_ALL(true, 15),
    TESTING(true, 16),
    TEST_COMPLETE(true, 17),
    CANCEL(false, -1);

    TestScreens(final boolean showTestStep, final int testStep) {
        this.showTestStep = showTestStep;
        this.testStep = testStep;
    }

    public final boolean showTestStep;
    public final int testStep;

}
