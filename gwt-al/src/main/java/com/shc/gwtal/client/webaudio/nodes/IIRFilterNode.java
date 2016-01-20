package com.shc.gwtal.client.webaudio.nodes;

import com.google.gwt.typedarrays.shared.Float32Array;

/**
 * @author Sri Harsha Chilakapati
 */
public class IIRFilterNode extends AudioNode
{
    protected IIRFilterNode()
    {
    }

    public final native void getFrequencyResponse(Float32Array frequencyHz, Float32Array magResponse,
                                                  Float32Array phaseResponse) /*-{
        this.getFrequencyResponse(frequencyHz, magResponse, phaseResponse);
    }-*/;
}
