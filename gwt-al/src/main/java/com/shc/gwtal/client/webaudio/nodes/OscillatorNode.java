package com.shc.gwtal.client.webaudio.nodes;

import com.shc.gwtal.client.webaudio.AudioEventHandler;
import com.shc.gwtal.client.webaudio.AudioParam;
import com.shc.gwtal.client.webaudio.PeriodicWave;
import com.shc.gwtal.client.webaudio.enums.OscillatorType;

/**
 * @author Sri Harsha Chilakapati
 */
public class OscillatorNode extends AudioNode
{
    protected OscillatorNode()
    {
    }

    public final OscillatorType getType()
    {
        return OscillatorType.forJsType(nGetType());
    }

    private native String nGetType() /*-{
        return this.type;
    }-*/;

    public final void setType(OscillatorType type)
    {
        nSetType(type.getJsType());
    }

    private native void nSetType(String jsType) /*-{
        this.type = jsType;
    }-*/;

    public final native AudioParam getFrequency() /*-{
        return this.frequency;
    }-*/;

    public final native AudioParam getDetune() /*-{
        return this.detune;
    }-*/;

    public final native void setOnEnded(AudioEventHandler eventHandler) /*-{
        this.onended = function ()
        {
            if (eventHandler)
                eventHandler.@com.shc.gwtal.client.webaudio.AudioEventHandler::invoke()();
        };
    }-*/;

    public final void start()
    {
        start(0);
    }

    public final native void start(double when) /*-{
        this.start(when);
    }-*/;

    public final void stop()
    {
        stop(0);
    }

    public final native void stop(double when) /*-{
        this.stop(when);
    }-*/;

    public final native void setPeriodicWave(PeriodicWave periodicWave) /*-{
        this.setPeriodicWave(periodicWave);
    }-*/;
}
