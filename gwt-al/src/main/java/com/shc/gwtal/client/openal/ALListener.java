package com.shc.gwtal.client.openal;

import com.shc.gwtal.client.webaudio.AudioContext;
import com.shc.gwtal.client.webaudio.AudioListener;
import com.shc.gwtal.client.webaudio.enums.DistanceModelType;
import com.shc.gwtal.client.webaudio.nodes.GainNode;
import com.shc.gwtal.client.webaudio.nodes.PannerNode;

/**
 * @author Sri Harsha Chilakapati
 */
class ALListener
{
    public float posX, posY, posZ;
    public float velX, velY, velZ;

    public float gain = 1f;

    public float orientationAtX, orientationAtY, orientationAtZ;
    public float orientationUpX, orientationUpY, orientationUpZ;

    public PannerNode inputNode;
    public GainNode   outputNode;

    private AudioContext context;

    public ALListener(AudioContext context)
    {
        this.context = context;

        inputNode = context.createPanner();
        outputNode = context.createGain();

        orientationAtX = 0f;
        orientationAtY = 0f;
        orientationAtZ = -1f;

        orientationUpX = 0f;
        orientationUpY = 1f;
        orientationUpZ = 0f;

        inputNode.connect(outputNode);

        // Set the distance model as inverse since this applies to the listener.
        inputNode.setDistanceModel(DistanceModelType.INVERSE);

        update();
    }

    /**
     * Called by the implementation to update the listener.
     */
    @SuppressWarnings("deprecation")
    public void update()
    {
        AudioListener listener = context.getAudioListener();

        listener.setPosition(posX, posY, posZ);
        listener.setOrientation(orientationAtX, orientationAtY, orientationAtZ,
                orientationUpX, orientationUpY, orientationUpZ);

        inputNode.setVelocity(velX, velY, velZ);
        outputNode.getGain().setValue(Math.min(1.0f, Math.max(0.0f, gain)));
    }
}
