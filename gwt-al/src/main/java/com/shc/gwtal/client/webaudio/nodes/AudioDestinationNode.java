package com.shc.gwtal.client.webaudio.nodes;

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
