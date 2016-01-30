package com.shc.gwtal.client.openal;

/**
 * @author Sri Harsha Chilakapati
 */
final class ALUtils
{
    private ALUtils()
    {
    }

    public static StateManager getStateManager()
    {
        return StateManager.forContext(AL.getCurrentContext());
    }

    public static BufferManager getBufferManager()
    {
        return getStateManager().bufferManager;
    }

    public static SourceManager getSourceManager()
    {
        return getStateManager().sourceManager;
    }
}
