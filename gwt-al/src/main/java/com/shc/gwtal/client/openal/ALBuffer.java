package com.shc.gwtal.client.openal;

import com.shc.gwtal.client.webaudio.AudioBuffer;

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

    public void setAudioBuffer(AudioBuffer audioBuffer)
    {
        this.audioBuffer = audioBuffer;
    }
}
