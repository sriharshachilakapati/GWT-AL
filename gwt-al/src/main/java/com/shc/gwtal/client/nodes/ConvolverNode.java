package com.shc.gwtal.client.nodes;

import com.shc.gwtal.client.AudioBuffer;

/**
 * @author Sri Harsha Chilakapati
 */
public class ConvolverNode extends AudioNode
{
    protected ConvolverNode()
    {
    }

    public final native AudioBuffer getBuffer() /*-{
        return this.buffer;
    }-*/;

    public final native void setBuffer(AudioBuffer buffer) /*-{
        this.buffer = buffer;
    }-*/;

    public final native boolean getNormalize() /*-{
        return this.normalize;
    }-*/;

    public final native void setNormalize(boolean normalize) /*-{
        this.normalize = normalize;
    }-*/;
}
