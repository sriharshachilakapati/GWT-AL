package com.shc.gwtal.client.webaudio.nodes;

import com.shc.gwtal.client.webaudio.AudioParam;

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
