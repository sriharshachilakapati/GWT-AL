package com.shc.gwtal.client.openal;

import com.shc.gwtal.client.webaudio.AudioBuffer;

/**
 * @author Sri Harsha Chilakapati
 */
class ALBuffer
{
    AudioBuffer audioBuffer;

    boolean isReady()
    {
        return audioBuffer != null;
    }
}
