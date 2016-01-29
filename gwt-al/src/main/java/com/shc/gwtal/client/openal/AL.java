package com.shc.gwtal.client.openal;

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

    public static ALContext getContext()
    {
        return context;
    }

    public static void setCurrentContext(ALContext alContext)
    {
        AL.context = alContext;
    }
}
