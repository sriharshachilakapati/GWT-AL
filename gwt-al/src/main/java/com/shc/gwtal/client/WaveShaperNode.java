package com.shc.gwtal.client;

import com.google.gwt.typedarrays.shared.Float32Array;

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

    public enum OverSampleType
    {
        NONE("none"),
        TWO_X("2x"),
        FOUR_X("4x");

        private String jsType;

        OverSampleType(String jsType)
        {
            this.jsType = jsType;
        }

        public String getJsType()
        {
            return jsType;
        }

        public static OverSampleType forJsType(String jsType)
        {
            for (OverSampleType type : values())
                if (type.getJsType().equalsIgnoreCase(jsType))
                    return type;

            return NONE;
        }
    }
}
