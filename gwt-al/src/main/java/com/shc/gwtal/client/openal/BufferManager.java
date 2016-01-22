package com.shc.gwtal.client.openal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.xml.client.DOMException;
import com.shc.gwtal.client.webaudio.AudioBuffer;
import com.shc.gwtal.client.webaudio.AudioContext;
import com.shc.gwtal.client.webaudio.Promise;

import java.util.HashMap;

/**
 * @author Sri Harsha Chilakapati
 */
class BufferManager
{
    private static HashMap<Integer, BufferManager> managers = new HashMap<>();

    private static int nextBufferID = 1;

    private AudioBuffer audioBuffer;

    public static BufferManager forBuffer(int bufferID)
    {
        return managers.get(bufferID);
    }

    public static int createBuffer()
    {
        managers.put(nextBufferID, new BufferManager());
        return nextBufferID++;
    }

    public static boolean isValid(int bufferID)
    {
        return forBuffer(bufferID) != null;
    }

    public boolean isReady()
    {
        return audioBuffer != null;
    }

    public Promise<AudioBuffer> bufferData(ArrayBuffer arrayBuffer)
    {
        return AL.getContext().getWebAudioContext().decodeAudioData(arrayBuffer,
                new AudioContext.DecodeSuccessCallback()
                {
                    @Override
                    public void invoke(AudioBuffer decodedData)
                    {
                        audioBuffer = decodedData;
                    }
                },

                new AudioContext.DecodeErrorCallback()
                {
                    @Override
                    public void invoke(DOMException error)
                    {
                        GWT.log(error.getMessage());
                        audioBuffer = null;
                    }
                }
        );
    }
}
