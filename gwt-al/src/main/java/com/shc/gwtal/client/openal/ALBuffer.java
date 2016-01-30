package com.shc.gwtal.client.openal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.xml.client.DOMException;
import com.shc.gwtal.client.webaudio.AudioBuffer;
import com.shc.gwtal.client.webaudio.AudioContext;
import com.shc.gwtal.client.webaudio.Promise;

/**
 * @author Sri Harsha Chilakapati
 */
class ALBuffer
{
    private AudioBuffer audioBuffer;

    public boolean isReady()
    {
        return audioBuffer != null;
    }

    public Promise<AudioBuffer> bufferData(ArrayBuffer arrayBuffer)
    {
        return AL.getCurrentContext().getWebAudioContext().decodeAudioData(arrayBuffer,
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
