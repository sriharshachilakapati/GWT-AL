package com.shc.gwtal.client.openal;

import com.google.gwt.typedarrays.shared.DataView;

/**
 * @author Sri Harsha Chilakapati
 */
final class ALUtils
{
    public static final int SIZEOF_FLOAT = Float.SIZE / Byte.SIZE;
    public static final int SIZEOF_INT   = Integer.SIZE / Byte.SIZE;

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

    public static boolean hasEnoughBytes(DataView dataView, int bytes)
    {
        return dataView.byteLength() >= bytes;
    }

    public static boolean checkSetError(boolean error, int alError)
    {
        if (error)
            getStateManager().setError(alError);

        return error;
    }
}
