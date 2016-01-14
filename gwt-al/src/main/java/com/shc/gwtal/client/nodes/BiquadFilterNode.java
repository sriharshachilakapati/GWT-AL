package com.shc.gwtal.client.nodes;

import com.google.gwt.typedarrays.shared.Float32Array;
import com.shc.gwtal.client.AudioParam;
import com.shc.gwtal.client.enums.BiquadFilterType;

/**
 * @author Sri Harsha Chilakapati
 */
public class BiquadFilterNode extends AudioNode
{
    protected BiquadFilterNode()
    {
    }

    public final BiquadFilterType getType()
    {
        return BiquadFilterType.forJsType(nGetType());
    }

    public final void setType(BiquadFilterType type)
    {
        nSetType(type.getJsType());
    }

    private native String nGetType() /*-{
        return this.type;
    }-*/;

    private native void nSetType(String jsType) /*-{
        this.type = jsType;
    }-*/;

    public final native AudioParam getFrequency() /*-{
        return this.frequency;
    }-*/;

    public final native AudioParam getDetune() /*-{
        return this.detune;
    }-*/;

    public final native AudioParam getQ() /*-{
        return this.Q;
    }-*/;

    public final native AudioParam getGain() /*-{
        return this.gain;
    }-*/;

    public final native void getFrequencyResponse(Float32Array frequencyHz, Float32Array magResponse,
                                                  Float32Array phaseResponse) /*-{
        return this.getFrequencyResponse(frequencyHz, magResponse, phaseResponse);
    }-*/;
}
