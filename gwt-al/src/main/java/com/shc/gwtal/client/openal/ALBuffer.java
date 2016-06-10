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

    float getSampleRate()
    {
        return audioBuffer == null ? 0 : audioBuffer.getSampleRate();
    }
}
