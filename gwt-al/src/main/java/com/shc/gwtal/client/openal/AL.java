package com.shc.gwtal.client.openal;

import com.shc.gwtal.client.webaudio.AudioContextException;

/**
 * @author Sri Harsha Chilakapati
 */
public final class AL
{
    // Prevent instantiation
    private AL()
    {
    }

    private static ALContext context;

    public static void create() throws AudioContextException
    {
        context = ALContext.create();
    }

    public static ALContext getContext()
    {
        return context;
    }

    public static void destroy()
    {
        context.getWebAudioContext().close();
        context = null;
    }
}
