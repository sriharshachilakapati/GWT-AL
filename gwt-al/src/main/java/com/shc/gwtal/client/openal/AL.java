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

    private static ALContext currentContext;

    public static ALContext getCurrentContext()
    {
        return currentContext;
    }

    public static void setCurrentContext(ALContext alContext)
    {
        AL.currentContext = alContext;
    }
}
