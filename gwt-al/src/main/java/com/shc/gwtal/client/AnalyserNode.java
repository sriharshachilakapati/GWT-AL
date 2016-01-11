package com.shc.gwtal.client;

import com.google.gwt.typedarrays.shared.Float32Array;
import com.google.gwt.typedarrays.shared.Uint8Array;

/**
 * @author Sri Harsha Chilakapati
 */
public class AnalyserNode extends AudioNode
{
    protected AnalyserNode()
    {
    }

    public final native int getFftSize() /*-{
        return this.fftSize;
    }-*/;

    public final native void setFftSize(int fftSize) /*-{
        this.fftSize = fftSize;
    }-*/;

    public final native int getFrequencyBinCount() /*-{
        return this.frequencyBinCount;
    }-*/;

    public final native float getMinDecibels() /*-{
        return this.minDecibels;
    }-*/;

    public final native void setMinDecibels(float minDecibels) /*-{
        this.minDecibels = minDecibels;
    }-*/;

    public final native float getMaxDecibels() /*-{
        return this.maxDecibels;
    }-*/;

    public final native void setMaxDecibels(float maxDecibels) /*-{
        this.maxDecibels = maxDecibels;
    }-*/;

    public final native float getSmoothingTimeConstant() /*-{
        return this.smoothingTimeConstant;
    }-*/;

    public final native void setSmoothingTimeConstant(float smoothingTimeConstant) /*-{
        this.smoothingTimeConstant = smoothingTimeConstant;
    }-*/;

    public final native void getFloatFrequencyData(Float32Array array) /*-{
        this.getFloatFrequencyData(array);
    }-*/;

    public final native void getByteFrequencyData(Uint8Array array) /*-{
        this.getByteFrequencyData(array);
    }-*/;

    public final native void getFloatTimeDomainData(Float32Array array) /*-{
        this.getFloatTimeDomainData(array);
    }-*/;

    public final native void getByteTimeDomainData(Uint8Array array) /*-{
        this.getByteTimeDomainData(array);
    }-*/;
}