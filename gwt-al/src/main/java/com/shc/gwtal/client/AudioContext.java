package com.shc.gwtal.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Sri Harsha Chilakapati
 */
public final class AudioContext extends JavaScriptObject
{
    protected AudioContext()
    {
    }

    public static AudioContext create() throws AudioContextException
    {
        AudioContext context = nCreate();

        if (context == null)
            throw new AudioContextException("Error creating a context");

        return context;
    }

    private static native AudioContext nCreate() /*-{
        var contextClass = (
            $wnd.AudioContext
            || $wnd.webkitAudioContext
            || $wnd.mozAudioContext
            || $wnd.oAudioContext
            || $wnd.msAudioContext
        );

        if (contextClass)
            return new contextClass();
        else
            return null;
    }-*/;

    public native float getSampleRate() /*-{
        return this.sampleRate;
    }-*/;

    public native double getCurrentTime() /*-{
        return this.currentTime;
    }-*/;

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

    public static class Options
    {
        public PlaybackCategory playbackCategory = PlaybackCategory.INTERACTIVE;
    }
}
