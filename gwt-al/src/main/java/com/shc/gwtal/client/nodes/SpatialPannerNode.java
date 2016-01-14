package com.shc.gwtal.client.nodes;

import com.shc.gwtal.client.AudioParam;
import com.shc.gwtal.client.enums.DistanceModelType;
import com.shc.gwtal.client.enums.PanningModelType;

/**
 * @author Sri Harsha Chilakapati
 */
public class SpatialPannerNode extends AudioNode
{
    protected SpatialPannerNode()
    {
    }

    public final PanningModelType getPanningModel()
    {
        return PanningModelType.forJsType(nGetPanningModel());
    }

    private native String nGetPanningModel() /*-{
        return this.panningModel;
    }-*/;

    public final void setPanningModel(PanningModelType panningModel)
    {
        nSetPanningModel(panningModel.getJsType());
    }

    private native void nSetPanningModel(String panningModel) /*-{
        this.panningModel = panningModel;
    }-*/;

    public final native AudioParam getPositionX() /*-{
        return this.positionZ;
    }-*/;

    public final native AudioParam getPositionY() /*-{
        return this.positionZ;
    }-*/;

    public final native AudioParam getPositionZ() /*-{
        return this.positionZ;
    }-*/;

    public final native AudioParam getOrientationX() /*-{
        return this.orientationX;
    }-*/;

    public final native AudioParam getOrientationY() /*-{
        return this.orientationY;
    }-*/;

    public final native AudioParam getOrientationZ() /*-{
        return this.orientationZ;
    }-*/;

    public final DistanceModelType getDistanceModel()
    {
        return DistanceModelType.forJsType(nGetDistanceModel());
    }

    private native String nGetDistanceModel() /*-{
        return this.distanceModel;
    }-*/;

    public final void setDistanceModel(DistanceModelType distanceModel)
    {
        nSetDistanceModel(distanceModel.getJsType());
    }

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
        this.rolloffFactor = rolloffFactorl;
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
}
