package com.shc.gwtal.client.enums;

/**
 * @author Sri Harsha Chilakapati
 */
public enum AudioContextState
{
    SUSPENDED("suspended"),
    RUNNING("running"),
    CLOSED("closed");

    private String jsState;

    AudioContextState(String jsState)
    {
        this.jsState = jsState;
    }

    public String getJsState()
    {
        return jsState;
    }
}
