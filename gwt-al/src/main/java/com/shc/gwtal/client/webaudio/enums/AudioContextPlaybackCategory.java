package com.shc.gwtal.client.webaudio.enums;

/**
 * @author Sri Harsha Chilakapati
 */
public enum AudioContextPlaybackCategory
{
    BALANCED("balanced"),
    INTERACTIVE("interactive"),
    PLAYBACK("playback");

    private String jsState;

    AudioContextPlaybackCategory(String jsState)
    {
        this.jsState = jsState;
    }

    public String getJsState()
    {
        return jsState;
    }
}