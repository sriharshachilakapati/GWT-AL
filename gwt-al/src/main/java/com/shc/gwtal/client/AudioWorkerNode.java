package com.shc.gwtal.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * @author Sri Harsha Chilakapati
 */
public class AudioWorkerNode extends AudioNode
{
    protected AudioWorkerNode()
    {
    }

    public final native <T> void postMessage(T message) /*-{
        this.postMessage(message);
    }-*/;

    public final native <T> void postMessage(T message, JsArray<JavaScriptObject> transfer) /*-{
        this.postMessage(message, transfer);
    }-*/;

    public final native void setOnMessage(AudioEventHandler eventHandler) /*-{
        this.onmessage = function()
        {
            if (eventHandler)
                eventHandler.@com.shc.gwtal.client.AudioEventHandler::invoke()();
        };
    }-*/;
}
