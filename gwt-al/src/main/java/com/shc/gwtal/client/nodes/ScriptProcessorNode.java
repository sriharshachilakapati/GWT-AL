package com.shc.gwtal.client.nodes;

import com.shc.gwtal.client.AudioEventHandler;

/**
 * @author Sri Harsha Chilakapati
 */
@Deprecated
public class ScriptProcessorNode extends AudioNode
{
    protected ScriptProcessorNode()
    {
    }

    @Deprecated
    public final native int getBufferSize() /*-{
        return this.bufferSize;
    }-*/;

    @Deprecated
    public final native void setOnAudioProcess(AudioEventHandler eventHandler) /*-{
        this.onaudioprocess = function()
        {
            if (eventHandler)
                eventHandler.@com.shc.gwtal.client.AudioEventHandler::invoke()();
        };
    }-*/;
}
