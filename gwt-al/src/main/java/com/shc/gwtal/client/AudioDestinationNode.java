package com.shc.gwtal.client;

/**
 * @author Sri Harsha Chilakapati
 */
public class AudioDestinationNode extends AudioNode
{
    protected AudioDestinationNode()
    {
    }

    public final native int getMaxChannelCount() /*-{
        return this.maxChannelCount;
    }-*/;
}
