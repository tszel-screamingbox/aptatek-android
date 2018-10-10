package com.aptatek.pkulab.device.bluetooth;

public final class LumosReaderConstants {

    private LumosReaderConstants() {

    }

    public static final String DEVICE_NAME = "Lumos Camera";

    public static final String READER_SERVICE = "84A91C5E-24C5-4726-9860-0847BB1D01E7";
    public static final String READER_CHAR_WORKFLOW_STATE = "00001001-0000-1000-8000-00805f9b34fb";
    public static final String READER_CHAR_RESULT = "00002001-0000-1000-8000-00805f9b34fb";
    public static final String READER_CHAR_NUM_RESULTS = "00002002-0000-1000-8000-00805f9b34fb";
    public static final String READER_CHAR_REQUEST_RESULT = "00002003-0000-1000-8000-00805f9b34fb";
    public static final String READER_CHAR_CARTRIDGE_ID = "00003001-0000-1000-8000-00805f9b34fb";
    public static final String READER_CHAR_ERROR = "00004001-0000-1000-8000-00805f9b34fb";
    public static final String READER_CHAR_UPDATE_ASSAY_DETAILS = "00005001-0000-1000-8000-00805f9b34fb";

    public static final String TIME_SERVICE = "00001805-0000-1000-8000-00805f9b34fb";
    public static final String TIME_SERVICE_CHAR_CURRENT_TIME = "00002a2b-0000-1000-8000-00805f9b34fb";
    public static final String TIME_SERVICE_CHAR_LOCAL_TIME_INFO = "00002a0f-0000-1000-8000-00805f9b34fb";

}
