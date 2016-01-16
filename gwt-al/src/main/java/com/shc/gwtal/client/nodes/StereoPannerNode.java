package com.shc.gwtal.client.nodes;

import com.shc.gwtal.client.AudioParam;

/**
 * @author Sri Harsha Chilakapati
 */
public class StereoPannerNode extends AudioNode
{
    protected StereoPannerNode()
    {
    }

    public final native AudioParam getPan() /*-{
        return this.pan;
    }-*/;
}
