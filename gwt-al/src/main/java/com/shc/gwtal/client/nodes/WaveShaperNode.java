package com.shc.gwtal.client.nodes;

import com.google.gwt.typedarrays.shared.Float32Array;
import com.shc.gwtal.client.enums.OverSampleType;

/**
 * @author Sri Harsha Chilakapati
 */
public class WaveShaperNode extends AudioNode
{
    protected WaveShaperNode()
    {
    }

    public final native Float32Array getCurve() /*-{
        return this.curve;
    }-*/;

    public final native void setCurve(Float32Array curve) /*-{
        this.curve = curve;
    }-*/;

    public final OverSampleType getOverSample()
    {
        return OverSampleType.forJsType(nGetOverSample());
    }

    private native String nGetOverSample() /*-{
        return this.oversample;
    }-*/;

    public final void setOverSample(OverSampleType overSample)
    {
        nSetOverSample(overSample.getJsType());
    }

    private native void nSetOverSample(String oversample) /*-{
        this.oversample = oversample;
    }-*/;
}
