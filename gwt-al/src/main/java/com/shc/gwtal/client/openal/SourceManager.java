package com.shc.gwtal.client.openal;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sri Harsha Chilakapati
 */
final class SourceManager
{
    private static Map<Integer, SourceManager> sourceManagers = new HashMap<>();

    private static int nextSourceID = 1;

    public static int createSource()
    {
        sourceManagers.put(nextSourceID, new SourceManager());
        return nextSourceID++;
    }

    public static SourceManager forSource(int sourceID)
    {
        return sourceManagers.get(sourceID);
    }

    public static boolean isValid(int sourceID)
    {
        return forSource(sourceID) != null;
    }

    public static void deleteSource(int sourceID)
    {
        sourceManagers.remove(sourceID);
    }
}
