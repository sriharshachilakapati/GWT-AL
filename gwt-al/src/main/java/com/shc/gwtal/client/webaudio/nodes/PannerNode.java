package com.shc.gwtal.client.webaudio.nodes;

import com.shc.gwtal.client.webaudio.enums.DistanceModelType;
import com.shc.gwtal.client.webaudio.enums.PanningModelType;

/**
 * @author Sri Harsha Chilakapati
 */
public class PannerNode extends AudioNode
{
    protected PannerNode()
    {
    }

    public final PanningModelType getPanningModel()
    {
        return PanningModelType.forJsType(nGetPanningModel());
    }

    public final void setPanningModel(PanningModelType panningModel)
    {
        nSetPanningModel(panningModel.getJsType());
    }

    private native String nGetPanningModel() /*-{
        return this.panningModel;
    }-*/;

    private native void nSetPanningModel(String jsType) /*-{
        this.panningModel = jsType;
    }-*/;

    public final DistanceModelType getDistanceModel()
    {
        return DistanceModelType.forJsType(nGetDistanceModel());
    }

    public final void setDistanceModel(DistanceModelType distanceModel)
    {
        nSetDistanceModel(distanceModel.getJsType());
    }

    private native String nGetDistanceModel() /*-{
        return this.distanceModel;
    }-*/;

    private native void nSetDistanceModel(String distanceModel) /*-{
        this.distanceModel = distanceModel;
    }-*/;

    public final native float getRefDistance() /*-{
        return this.refDistance;
    }-*/;

    public final native void setRefDistance(float refDistance) /*-{
        this.refDistance = refDistance;
    }-*/;

    public final native float getMaxDistance() /*-{
        return this.maxDistance;
    }-*/;

    public final native void setMaxDistance(float maxDistance) /*-{
        this.maxDistance = maxDistance;
    }-*/;

    public final native float getRolloffFactor() /*-{
        return this.rolloffFactor;
    }-*/;

    public final native void setRolloffFactor(float rolloffFactor) /*-{
        this.rolloffFactor = rolloffFactor;
    }-*/;

    public final native float getConeInnerAngle() /*-{
        return this.coneInnerAngle;
    }-*/;

    public final native void setConeInnerAngle(float coneInnerAngle) /*-{
        this.coneInnerAngle = coneInnerAngle;
    }-*/;

    public final native float getConeOuterAngle() /*-{
        return this.coneOuterAngle;
    }-*/;

    public final native void setConeOuterAngle(float coneOuterAngle) /*-{
        this.coneOuterAngle - coneOuterAngle;
    }-*/;

    public final native float getConeOuterGain() /*-{
        return this.coneOuterGain;
    }-*/;

    public final native void setConeOuterGain(float coneOuterGain) /*-{
        this.coneOuterGain = coneOuterGain;
    }-*/;

    public final native void setPosition(float x, float y, float z) /*-{
        this.setPosition(x, y, z);
    }-*/;

    public final native void setOrientation(float x, float y, float z) /*-{
        this.setOrientation(x, y, z);
    }-*/;

    public final native void setVelocity(float x, float y, float z) /*-{
        this.setVelocity(x, y, z);
    }-*/;
}
