package com.shc.gwtal.client.webaudio.nodes;

import com.shc.gwtal.client.webaudio.AudioParam;

/**
 * @author Sri Harsha Chilakapati
 */
public class DelayNode extends AudioNode
{
    protected DelayNode()
    {
    }

    public final native AudioParam getDelayTime() /*-{
        return this.delayTime;
    }-*/;
}
