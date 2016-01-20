package com.shc.gwtal.client.webaudio.nodes;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Sri Harsha Chilakapati
 */
public class MediaStreamAudioDestinationNode extends AudioNode
{
    protected MediaStreamAudioDestinationNode()
    {
    }

    public final native JavaScriptObject getStream() /*-{
        return this.stream;
    }-*/;
}
