package com.shc.gwtal.client.openal;

import com.shc.gwtal.client.webaudio.AudioContext;
import com.shc.gwtal.client.webaudio.AudioEventHandler;
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
    public float dirX, dirY, dirZ;

    public float coneInnerAngle = 360f;
    public float coneOuterAngle = 360f;
    public float coneOuterGain  = 0.0f;

    public float gain = 1.0f;

    public int sourceRelative = AL_FALSE;
    public int sourceState    = AL_INITIAL;
    public int sourceType     = AL_UNDETERMINED;
    public int looping        = AL_FALSE;
    public int buffer         = AL_NONE;

    public float minGain = 0.0f;
    public float maxGain = 1.0f;

    public float referralDistance = 1.0f;
    public float rolloffFactor    = 1.0f;
    public float maxDistance      = Float.MAX_VALUE;
    public float pitch            = 1.0f;

    public int buffersQueued = 0;

    public AudioBufferSourceNode sourceNode;
    public PannerNode            pannerNode;
    public GainNode              outputNode;

    private double bufferPosition = 0;
    private double startTime;

    public int buffersProcessed = 0;

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

        sourceNode.getPlaybackRate().setValue(pitch);
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
        pannerNode.setOrientation(dirX, dirY, dirZ);

        pannerNode.setConeInnerAngle(coneInnerAngle);
        pannerNode.setConeOuterAngle(coneOuterAngle);
        pannerNode.setConeOuterGain(Math.min(Math.max(coneOuterGain, 0.0f), 1.0f));

        outputNode.getGain().setValue(Math.min(maxGain, Math.max(minGain, gain)));
    }

    public void setSourceState(int state)
    {
        if (buffer == AL_NONE)
        {
            sourceState = AL_STOPPED;
            bufferPosition = 0;
            return;
        }

        StateManager stateManager = getStateManager();
        AudioContext context = stateManager.context;

        if (sourceNode != null)
            sourceNode.disconnect();

        if (state == AL_PLAYING)
        {
            if (sourceState != AL_PAUSED)
            {
                buffersProcessed = 0;
                bufferPosition = 0;
            }

            int oldState = sourceState;

            sourceState = AL_PLAYING;
            sourceNode = context.createBufferSource();
            sourceNode.setOnEnded(new AudioEventHandler()
            {
                @Override
                public void invoke()
                {
                    if (looping == AL_FALSE)
                    {
                        // Set to stopped upon ended.
                        setSourceState(AL_STOPPED);
                    }
                }
            });

            ALBuffer alBuffer = getBufferManager().getBuffer(buffer);

            if (alBuffer.isReady())
                sourceNode.setBuffer(alBuffer.audioBuffer);

            update(); // Update the source, and set the properties

            startTime = context.getCurrentTime();

            if (oldState == AL_PAUSED)
                sourceNode.start(0, bufferPosition % getBufferDuration());
            else
                sourceNode.start(0, 0);
        }
        else if (state == AL_STOPPED)
        {
            sourceState = AL_STOPPED;
            bufferPosition = 0;

            if (sourceNode == null)
                return;

            sourceNode.stop(0);
            sourceNode = null;
        }
        else if (state == AL_PAUSED)
        {
            if (sourceNode == null)
                return;

            if (sourceState == AL_PLAYING)
                bufferPosition += context.getCurrentTime() - startTime;

            sourceState = AL_PAUSED;

            sourceNode.stop(0);
            sourceNode = null;
        }
        else if (state == AL_INITIAL)
            sourceState = AL_INITIAL;
    }

    private double getBufferDuration()
    {
        return buffer == AL_NONE ? 0 : getBufferManager().getBuffer(buffer).audioBuffer.getDuration();
    }
}
