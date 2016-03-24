package com.shc.gwtal.client.openal;

import com.google.gwt.typedarrays.shared.DataView;

/**
 * @author Sri Harsha Chilakapati
 */
final class ALUtils
{
    static final int SIZEOF_FLOAT = Float.SIZE / Byte.SIZE;
    static final int SIZEOF_INT   = Integer.SIZE / Byte.SIZE;

    private ALUtils()
    {
    }

    static StateManager getStateManager()
    {
        return StateManager.forContext(AL.getCurrentContext());
    }

    static BufferManager getBufferManager()
    {
        return getStateManager().bufferManager;
    }

    static SourceManager getSourceManager()
    {
        return getStateManager().sourceManager;
    }

    static boolean isFinite(float val)
    {
        return !Float.isInfinite(val) && !Float.isNaN(val);
    }

    static boolean areAllFinite(float... val)
    {
        for (float v : val)
            if (!isFinite(v))
                return false;

        return true;
    }

    static boolean hasEnoughBytes(DataView dataView, int bytes)
    {
        return dataView.byteLength() >= bytes;
    }

    static boolean checkSetError(boolean error, int alError)
    {
        if (error)
            getStateManager().setError(alError);

        return error;
    }
}
