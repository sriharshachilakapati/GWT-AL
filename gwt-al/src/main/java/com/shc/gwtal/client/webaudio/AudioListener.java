package com.shc.gwtal.client.webaudio;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Sri Harsha Chilakapati
 */
@Deprecated
public class AudioListener extends JavaScriptObject
{
    protected AudioListener()
    {
    }

    @Deprecated
    public final native void setPosition(float x, float y, float z) /*-{
        this.setPosition(x, y, z);
    }-*/;

    @Deprecated
    public final native void setOrientation(float x, float y, float z, float xUp, float yUp, float zUp) /*-{
        this.setOrientation(x, y, z, xUp, yUp, zUp);
    }-*/;
}
