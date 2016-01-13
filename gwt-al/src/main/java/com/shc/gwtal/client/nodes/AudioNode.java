package com.shc.gwtal.client.nodes;

import com.google.gwt.dom.client.EventTarget;
import com.shc.gwtal.client.AudioContext;
import com.shc.gwtal.client.AudioParam;

/**
 * @author Sri Harsha Chilakapati
 */
public class AudioNode extends EventTarget
{
    protected AudioNode()
    {
    }

    public final AudioNode connect(AudioNode destination)
    {
        return connect(destination, 0);
    }

    public final AudioNode connect(AudioNode destination, int output)
    {
        return connect(destination, output, 0);
    }

    public final native AudioNode connect(AudioNode destination, int output, int input) /*-{
        return this.connect(destination, output, input);
    }-*/;

    public final void connect(AudioParam destination)
    {
        connect(destination, 0);
    }

    public final native void connect(AudioParam destination, int output) /*-{
        this.connect(destination, output);
    }-*/;

    public final native void disconnect() /*-{
        this.disconnect();
    }-*/;

    public final native void disconnect(int output) /*-{
        this.disconnect(output);
    }-*/;

    public final native void disconnect(AudioNode destination) /*-{
        this.disconnect(destination);
    }-*/;

    public final native void disconnect(AudioNode destination, int output) /*-{
        this.disconnect(destination, output);
    }-*/;

    public final native void disconnect(AudioNode destination, int output, int input) /*-{
        this.disconnect(destination, output, input);
    }-*/;

    public final native void disconnect(AudioParam destination) /*-{
        this.disconnect(destination);
    }-*/;

    public final native void disconnect(AudioParam destination, int output) /*-{
        this.disconnect(destination, output);
    }-*/;

    public final native AudioContext getContext() /*-{
        return this.context;
    }-*/;

    public final native int getNumberOfInputs() /*-{
        return this.numberOfInputs;
    }-*/;

    public final native int getNumberOfOutputs() /*-{
        return this.numberOfOutputs;
    }-*/;

    public final native int getChannelCount() /*-{
        return this.channelCount;
    }-*/;

    public final native void setChannelCount(int channelCount) /*-{
        this.channelCount = channelCount;
    }-*/;

    public final ChannelCountMode getChannelCountMode()
    {
        return ChannelCountMode.forJsState(nGetChannelCountMode());
    }

    public final void setChannelCountMode(ChannelCountMode channelCountMode)
    {
        nSetChannelCountMode(channelCountMode.getJsState());
    }

    private native String nGetChannelCountMode() /*-{
        return this.channelCountMode;
    }-*/;

    private native void nSetChannelCountMode(String channelCountMode) /*-{
        this.channelCountMode = channelCountMode;
    }-*/;

    public final ChannelInterpretation getChannelInterpretation()
    {
        return ChannelInterpretation.forJsState(nGetChannelInterpretation());
    }

    public final void setChannelInterpretation(ChannelInterpretation channelInterpretation)
    {
        nSetChannelInterpretation(channelInterpretation.getJsState());
    }

    private native String nGetChannelInterpretation() /*-{
        return this.channelInterpretation;
    }-*/;

    private native void nSetChannelInterpretation(String channelInterpretation) /*-{
        this.channelInterpretation = channelInterpretation;
    }-*/;

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
}
