package com.shc.gwtal.client.openal;

import com.shc.gwtal.client.webaudio.AudioListener;

import static com.shc.gwtal.client.openal.ALUtils.*;

/**
 * @author Sri Harsha Chilakapati
 */
class ALListener
{
    public float posX, posY, posZ;
    public float velX, velY, velZ;

    public float gain;

    public float orientationAtX, orientationAtY, orientationAtZ;
    public float orientationUpX, orientationUpY, orientationUpZ;

    /**
     * Called by the implementation to update the listener.
     */
    @SuppressWarnings("deprecation")
    public void update()
    {
        AudioListener listener = getStateManager().context.getAudioListener();

        listener.setPosition(posX, posY, posZ);
        listener.setOrientation(orientationAtX, orientationAtY, orientationAtZ,
                orientationUpX, orientationUpY, orientationUpZ);
    }
}
