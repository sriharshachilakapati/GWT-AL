package com.shc.gwtal.client.openal;

import java.util.HashMap;
import java.util.Map;

import static com.shc.gwtal.client.openal.AL10.*;

/**
 * @author Sri Harsha Chilakapati
 */
final class BufferManager
{
    private Map<Integer, ALBuffer> buffers = new HashMap<>();

    BufferManager()
    {
        buffers.put(AL_NONE, new ALBuffer()); // AL_NONE is a valid buffer
    }

    private int nextBufferID = 1;

    public ALBuffer getBuffer(int bufferID)
    {
        return buffers.get(bufferID);
    }

    public int createBuffer()
    {
        buffers.put(nextBufferID, new ALBuffer());
        return nextBufferID++;
    }

    public boolean isValid(int bufferID)
    {
        return getBuffer(bufferID) != null;
    }

    public void deleteBuffer(int bufferID)
    {
        buffers.remove(bufferID);
    }
}
