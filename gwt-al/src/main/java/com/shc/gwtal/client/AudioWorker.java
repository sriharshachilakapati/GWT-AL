package com.shc.gwtal.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * @author Sri Harsha Chilakapati
 */
public class AudioWorker extends JavaScriptObject
{
    protected AudioWorker()
    {
    }

    public final native void terminate() /*-{
        this.terminate();
    }-*/;

    public final native <T> void postMessage(T message) /*-{
        this.postMessage(message);
    }-*/;

    public final native <T> void postMessage(T message, JsArray<JavaScriptObject> transfer) /*-{
        this.postMessage(message, transfer);
    }-*/;

    public final native JsArray<ParamDescriptor> getParameters() /*-{
        return this.parameters;
    }-*/;

    public final native void setOnMessage(AudioEventHandler eventHandler) /*-{
        this.onmessage = function()
        {
            if (eventHandler)
                eventHandler.@com.shc.gwtal.client.AudioEventHandler::invoke()();
        };
    }-*/;

    public final native void setOnLoaded(AudioEventHandler eventHandler) /*-{
        this.onloaded = function()
        {
            if (eventHandler)
                eventHandler.@com.shc.gwtal.client.AudioEventHandler::invoke()();
        };
    }-*/;

    public final native AudioWorkerNode createNode(int numberOfInputs, int numberOfOutputs) /*-{
        return this.createNode(numberOfInputs, numberOfOutputs);
    }-*/;

    public final native AudioParam addParameter(String name, float defaultValue) /*-{
        return this.addParameter(name, defaultValue);
    }-*/;

    public final native void removeParameter(String name) /*-{
        this.removeParameter(name);
    }-*/;

    public static final class ParamDescriptor extends JavaScriptObject
    {
        protected ParamDescriptor()
        {
        }

        public final native String getName() /*-{
            return this.name;
        }-*/;

        public final native float getDefaultValue() /*-{
            return this.defaultValue;
        }-*/;
    }
}
