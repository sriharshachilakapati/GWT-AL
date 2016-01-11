package com.shc.gwtal.client;

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
