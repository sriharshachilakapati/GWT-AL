package com.shc.gwtal.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Sri Harsha Chilakapati
 */
public class AudioContext extends JavaScriptObject
{
    protected AudioContext()
    {
    }

    public enum State
    {
        SUSPENDED("suspended"),
        RUNNING("running"),
        CLOSED("closed");

        private String jsState;

        State(String jsState)
        {
            this.jsState = jsState;
        }

        public String getJsState()
        {
            return jsState;
        }
    }

    public enum PlaybackCategory
    {
        BALANCED("balanced"),
        INTERACTIVE("interactive"),
        PLAYBACK("playback");

        private String jsState;

        PlaybackCategory(String jsState)
        {
            this.jsState = jsState;
        }

        public String getJsState()
        {
            return jsState;
        }
    }
}
