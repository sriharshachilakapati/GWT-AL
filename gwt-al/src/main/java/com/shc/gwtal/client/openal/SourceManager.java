package com.shc.gwtal.client.openal;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sri Harsha Chilakapati
 */
final class SourceManager
{
    private Map<Integer, ALSource> sources = new HashMap<>();

    private int nextSourceID = 1;

    public int createSource()
    {
        sources.put(nextSourceID, new ALSource());
        return nextSourceID++;
    }

    public ALSource getSource(int sourceID)
    {
        return sources.get(sourceID);
    }

    public boolean isValid(int sourceID)
    {
        return getSource(sourceID) != null;
    }

    public void deleteSource(int sourceID)
    {
        sources.remove(sourceID);
    }
}
