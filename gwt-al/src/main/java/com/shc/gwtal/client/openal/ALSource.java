package com.shc.gwtal.client.openal;

import com.shc.gwtal.client.webaudio.AudioContext;
import com.shc.gwtal.client.webaudio.nodes.AudioBufferSourceNode;
import com.shc.gwtal.client.webaudio.nodes.GainNode;
import com.shc.gwtal.client.webaudio.nodes.PannerNode;

import static com.shc.gwtal.client.openal.AL10.*;
import static com.shc.gwtal.client.openal.AL11.*;
import static com.shc.gwtal.client.openal.ALUtils.*;

/**
 * @author Sri Harsha Chilakapati
 */
class ALSource
{
    public float posX, posY, posZ;
    public float velX, velY, velZ;

    public float gain;

    public int sourceRelative = AL_FALSE;
    public int sourceState    = AL_STOPPED;
    public int sourceType     = AL_UNDETERMINED;
    public int looping        = AL_FALSE;
    public int buffer         = AL_NONE;

    public float minGain = 0.0f;
    public float maxGain = 1.0f;

    public float referralDistance = 1.0f;
    public float rolloffFactor = 1.0f;
    public float maxDistance = Float.MAX_VALUE;
    public float pitch = 1.0f;

    public int buffersQueued = 0;

    public AudioBufferSourceNode sourceNode;
    public PannerNode            pannerNode;
    public GainNode              outputNode;

    public ALSource()
    {
        AudioContext ctx = getStateManager().context;

        pannerNode = ctx.createPanner();
        outputNode = ctx.createGain();

        pannerNode.connect(outputNode);
        outputNode.connect(getStateManager().inputNode);

        update();
    }

    /**
     * Called by the implementation to update the source
     */
    public void update()
    {
        if (sourceNode == null)
            return;

        sourceNode.connect(pannerNode);
        sourceNode.setLoop(looping == AL_TRUE);

        ALListener listener = getStateManager().listener;

        if (sourceRelative == AL_FALSE)
            pannerNode.setPosition(posX - listener.posX, posY - listener.posY, posZ - listener.posZ);
        else
            pannerNode.setPosition(posX, posY, posZ);

        pannerNode.setRefDistance(Math.max(0, referralDistance));
        pannerNode.setRolloffFactor(Math.max(0, rolloffFactor));
        pannerNode.setMaxDistance(Math.max(0, maxDistance));

        pannerNode.setVelocity(velX, velY, velZ);

        outputNode.getGain().setValue(Math.min(maxGain, Math.max(minGain, gain)));
    }
}
