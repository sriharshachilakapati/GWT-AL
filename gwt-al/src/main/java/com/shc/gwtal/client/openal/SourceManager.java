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

    void applyDopplerForAllSources()
    {
        for (ALSource source : sources.values())
            source.applyDoppler();
    }

    int createSource()
    {
        sources.put(nextSourceID, new ALSource());
        return nextSourceID++;
    }

    ALSource getSource(int sourceID)
    {
        return sources.get(sourceID);
    }

    boolean isValid(int sourceID)
    {
        return getSource(sourceID) != null;
    }

    void deleteSource(int sourceID)
    {
        sources.remove(sourceID);
    }
}
