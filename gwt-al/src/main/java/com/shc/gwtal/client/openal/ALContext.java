package com.shc.gwtal.client.openal;

import com.shc.gwtal.client.webaudio.AudioContext;
import com.shc.gwtal.client.webaudio.AudioContextException;

/**
 * @author Sri Harsha Chilakapati
 */
public class ALContext
{
    private AudioContext context;
    private ALCCapabilities capabilities;

    private ALContext(AudioContext context)
    {
        this.context = context;
        capabilities = new ALCCapabilities();
    }

    public static ALContext create() throws AudioContextException
    {
        return new ALContext(AudioContext.create());
    }

    public boolean isValid()
    {
        return context != null;
    }

    public AudioContext getWebAudioContext()
    {
        return context;
    }

    public ALCCapabilities getCapabilities()
    {
        return capabilities;
    }
}
