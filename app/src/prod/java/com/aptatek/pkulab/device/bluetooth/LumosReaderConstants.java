package com.aptatek.pkulab.device.bluetooth;

public final class LumosReaderConstants {

    private LumosReaderConstants() {

    }

    public static final String DEVICE_NAME = "Lumos Camera";
    public static final int MTU_SIZE = 240;

    public static final String READER_SERVICE = "84a91000-24c5-4726-9860-0847bb1d01e7";
    public static final String READER_CHAR_WORKFLOW_STATE = "84a91001-24c5-4726-9860-0847bb1d01E7";
    public static final String READER_CHAR_RESULT = "84a92001-24c5-4726-9860-0847bb1d01E7";
    public static final String READER_CHAR_NUM_RESULTS = "84a92002-24c5-4726-9860-0847bb1d01E7";
    public static final String READER_CHAR_REQUEST_RESULT = "84a92003-24c5-4726-9860-0847bb1d01E7";
    public static final String READER_CHAR_CARTRIDGE_ID = "84a93001-24c5-4726-9860-0847bb1d01E7";
    public static final String READER_CHAR_ERROR = "84a94001-24c5-4726-9860-0847bb1d01E7";
    public static final String READER_CHAR_UPDATE_TIME = "84a96001-24c5-4726-9860-0847bb1d01E7";
    public static final String READER_CHAR_RESULT_SYNC_REQUEST = "84a97001-24c5-4726-9860-0847bb1d01e7";
    public static final String READER_CHAR_RESULT_SYNC_RESPONSE = "84a98001-24c5-4726-9860-0847bb1d01e7";
    public static final String READER_CHAR_TEST_PROGRESS = "84a99001-24c5-4726-9860-0847bb1d01e7";

    public static final String BATTERY_SERVICE = "0000180F-0000-1000-8000-00805f9b34fb";
    public static final String BATTERY_CHAR_LEVEL = "00002A19-0000-1000-8000-00805f9b34fb";

    public static final int ERROR_MTU_CHANGE_FAILED = 688;
}
