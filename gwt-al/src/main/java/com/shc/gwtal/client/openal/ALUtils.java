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

    public static boolean isFinite(float val)
    {
        return !Float.isInfinite(val) && !Float.isNaN(val);
    }

    public static boolean areAllFinite(float... val)
    {
        for (float v : val)
            if (!isFinite(v))
                return false;

        return true;
    }
}
