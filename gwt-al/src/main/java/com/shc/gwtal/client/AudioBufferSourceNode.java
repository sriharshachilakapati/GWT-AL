package com.shc.gwtal.client;

/**
 * @author Sri Harsha Chilakapati
 */
public class AudioBufferSourceNode extends AudioNode
{
    protected AudioBufferSourceNode()
    {
    }

    public final native AudioBuffer getBuffer() /*-{
        return this.buffer;
    }-*/;

    public final native void setBuffer(AudioBuffer buffer) /*-{
        this.buffer = buffer;
    }-*/;

    public final native AudioParam getPlaybackRate() /*-{
        return this.playbackRate;
    }-*/;

    public final native AudioParam getDetune() /*-{
        return this.detune;
    }-*/;

    public final native boolean getLoop() /*-{
        return this.loop;
    }-*/;

    public final native void setLoop(boolean loop) /*-{
        this.loop = loop;
    }-*/;

    public final native double getLoopStart() /*-{
        return this.loopStart;
    }-*/;

    public final native void setLoopStart(double loopStart) /*-{
        this.loopStart = loopStart;
    }-*/;

    public final native double getLoopEnd() /*-{
        return this.loopEnd;
    }-*/;

    public final native void setLoopEnd(double loopEnd) /*-{
        this.loopEnd = loopEnd;
    }-*/;

    public final void start(double when)
    {
        start(when, 0);
    }

    public final void start()
    {
        start(0, 0);
    }

    public final native void start(double when, double offset) /*-{
        this.start(when, offset);
    }-*/;

    public final native void start(double when, double offset, double duration) /*-{
        this.start(when, offset, duration);
    }-*/;

    public final void stop()
    {
        stop(0);
    }

    public final native void stop(double when) /*-{
        this.stop(when);
    }-*/;

    public final native void setOnEnded(AudioEventHandler eventHandler) /*-{
        this.onended = function()
        {
            if (eventHandler)
                eventHandler.@com.shc.gwtal.client.AudioEventHandler::invoke()();
        };
    }-*/;
}
