package com.shc.gwtal.client.webaudio.enums;

/**
 * @author Sri Harsha Chilakapati
 */
public enum ChannelInterpretation
{
    SPEAKERS("speakers"),
    DISCRETE("discrete");

    private String jsState;

    ChannelInterpretation(String jsState)
    {
        this.jsState = jsState;
    }

    public static ChannelInterpretation forJsState(String jsState)
    {
        for (ChannelInterpretation mode : values())
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
