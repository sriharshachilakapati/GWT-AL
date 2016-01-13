package com.shc.gwtal.client.nodes;

import com.shc.gwtal.client.AudioParam;

/**
 * @author Sri Harsha Chilakapati
 */
public class GainNode extends AudioNode
{
    protected GainNode()
    {
    }

    public final native AudioParam getGain() /*-{
        return this.gain;
    }-*/;
}
