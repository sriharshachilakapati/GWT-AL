package com.shc.gwtal.client.nodes;

import com.shc.gwtal.client.AudioParam;

/**
 * @author Sri Harsha Chilakapati
 */
public class DynamicsCompressorNode extends AudioNode
{
    protected DynamicsCompressorNode()
    {
    }

    public final native AudioParam getThreshold() /*-{
        return this.threshold;
    }-*/;

    public final native AudioParam getKnee() /*-{
        return this.knee;
    }-*/;

    public final native AudioParam getRatio() /*-{
        return this.ratio;
    }-*/;

    public final native float getReduction() /*-{
        return this.reduction;
    }-*/;

    public final native AudioParam getAttack() /*-{
        return this.attack;
    }-*/;

    public final native AudioParam getRelease() /*-{
        return this.release;
    }-*/;
}
