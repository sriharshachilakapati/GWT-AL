package com.shc.gwtal.client.openal;

import com.shc.gwtal.client.webaudio.AudioBuffer;

/**
 * @author Sri Harsha Chilakapati
 */
class ALBuffer
{
    public AudioBuffer audioBuffer;

    public boolean isReady()
    {
        return audioBuffer != null;
    }
}
