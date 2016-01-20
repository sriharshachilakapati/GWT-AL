package com.shc.gwtal.client.webaudio;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Sri Harsha Chilakapati
 */
class DictionaryJSO extends JavaScriptObject
{
    protected DictionaryJSO()
    {
    }

    public static native DictionaryJSO create() /*-{
        return {};
    }-*/;

    public final native void define(String name, String property) /*-{
        this[name] = property;
    }-*/;

    public final native void define(String name, boolean property) /*-{
        this[name] = property;
    }-*/;
}
