package com.example.rcpc;

public enum Requests {

    NONE(0x0),

    //Volume Control
    VOLUME_MUTE(0x01),
    VOLUME_UNMUTE(0x02),
    VOLUME_UP(0x03),
    VOLUME_DOWN(0x04),
    VOLUME_SET(0x05),
    VOLUME_GET(0x06),
    AUDIO_SESSIONS(0x07),

    // PC Control
    PC_Shutdown(0x10),
    PC_Restart(0x11),
    PC_Get_Processes(0x12);

    private int code;
    Requests(int code) { this.code = code; }
    public String getValue() { return code + ""; }
}
