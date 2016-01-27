package com.shc.gwtal.client.openal;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sri Harsha Chilakapati
 */
final class BufferManager
{
    private Map<Integer, ALBuffer> managers = new HashMap<>();

    private int nextBufferID = 1;

    public ALBuffer getBuffer(int bufferID)
    {
        return managers.get(bufferID);
    }

    public int createBuffer()
    {
        managers.put(nextBufferID, new ALBuffer());
        return nextBufferID++;
    }

    public boolean isValid(int bufferID)
    {
        return getBuffer(bufferID) != null;
    }

    public void deleteBuffer(int bufferID)
    {
        managers.remove(bufferID);
    }
}
