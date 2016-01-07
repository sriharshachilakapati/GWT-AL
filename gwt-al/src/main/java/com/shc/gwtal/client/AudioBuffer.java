package com.shc.gwtal.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.typedarrays.shared.Float32Array;

/**
 * @author Sri Harsha Chilakapati
 */
public class AudioBuffer extends JavaScriptObject
{
    protected AudioBuffer()
    {
    }

    public final native float getSampleRate() /*-{
        return this.sampleRate;
    }-*/;

    public final native long getLength() /*-{
        return this.length;
    }-*/;

    public final native double getDuration() /*-{
        return this.duration;
    }-*/;

    public final native long getNumberOfChannels() /*-{
        return this.numberOfChannels;
    }-*/;

    public final native Float32Array getChannelData(long channel) /*-{
        return this.getChannelData(channel);
    }-*/;

    public final void copyFromChannel(Float32Array destination, long channelNumber)
    {
        copyFromChannel(destination, channelNumber, 0);
    }

    public final native void copyFromChannel(Float32Array destination, long channelNumber, long startInChannel) /*-{
        this.copyFromChannel(destination, channelNumber, startInChannel);
    }-*/;

    public final void copyToChannel(Float32Array source, long channelNumber)
    {
        copyToChannel(source, channelNumber, 0);
    }

    public final native void copyToChannel(Float32Array source, long channelNumber, long startInChannel) /*-{
        this.copyToChannel(source, channelNumber, startInChannel);
    }-*/;
}
