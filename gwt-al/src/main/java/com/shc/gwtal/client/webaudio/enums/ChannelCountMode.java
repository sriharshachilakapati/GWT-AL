package com.shc.gwtal.client.webaudio.enums;

/**
 * @author Sri Harsha Chilakapati
 */
public enum ChannelCountMode
{
    MAX("max"),
    CLAMPED_MAX("clamped-max"),
    EXPLICIT("explicit");

    private String jsState;

    ChannelCountMode(String jsState)
    {
        this.jsState = jsState;
    }

    public static ChannelCountMode forJsState(String jsState)
    {
        for (ChannelCountMode mode : values())
        {
            if (mode.getJsState().equalsIgnoreCase(jsState))
                return mode;
        }

        return null;
    }

    public String getJsState()
    {
        return jsState;
    }
}
