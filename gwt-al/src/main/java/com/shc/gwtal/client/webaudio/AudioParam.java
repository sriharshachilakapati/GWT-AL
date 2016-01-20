package com.shc.gwtal.client.webaudio;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.typedarrays.shared.Float32Array;

/**
 * @author Sri Harsha Chilakapati
 */
public class AudioParam extends JavaScriptObject
{
    protected AudioParam()
    {
    }

    public final native float getValue() /*-{
        return this.value;
    }-*/;

    public final native void setValue(float value) /*-{
        this.value = value;
    }-*/;

    public final native float getDefaultValue() /*-{
        return this.defaultValue;
    }-*/;

    public final native AudioParam setValueAtTime(float value, double startTime) /*-{
        return this.setValueAtTime(value, startTime);
    }-*/;

    public final native AudioParam linearRampToValueAtTime(float value, double endTime) /*-{
        return this.linearRampToValueAtTime(value, endTime);
    }-*/;

    public final native AudioParam exponentialRampToValueAtTime(float value, double endTime) /*-{
        return this.exponentialRampToValueAtTime(value, endTime);
    }-*/;

    public final native AudioParam setTargetAtTime(float target, double startTime, float timeConstant) /*-{
        return this.setTargetAtTime(target, startTime, timeConstant);
    }-*/;

    public final native AudioParam setValueCurveAtTime(Float32Array values, double startTime, double duration) /*-{
        return this.setValueCurveAtTime(values, startTime, duration);
    }-*/;

    public final native AudioParam cancelScheduledValues(double startTime) /*-{
        return this.cancelScheduledValues(startTime);
    }-*/;
}
