package com.shc.gwtal.client.openal;

import com.google.gwt.core.client.GWT;
import com.shc.gwtal.client.webaudio.AudioContext;
import com.shc.gwtal.client.webaudio.AudioEventHandler;
import com.shc.gwtal.client.webaudio.nodes.AudioBufferSourceNode;
import com.shc.gwtal.client.webaudio.nodes.GainNode;
import com.shc.gwtal.client.webaudio.nodes.PannerNode;

import java.util.LinkedList;
import java.util.Queue;

import static com.shc.gwtal.client.openal.AL10.*;
import static com.shc.gwtal.client.openal.AL11.*;
import static com.shc.gwtal.client.openal.ALUtils.*;

/**
 * @author Sri Harsha Chilakapati
 */
class ALSource
{
    float posX, posY, posZ;
    float velX, velY, velZ;
    float dirX, dirY, dirZ;
    float coneInnerAngle = 360f;
    float coneOuterAngle = 360f;
    float coneOuterGain  = 0.0f;
    float gain           = 1.0f;
    int sourceRelative = AL_FALSE;
    int sourceState    = AL_INITIAL;
    int sourceType     = AL_UNDETERMINED;
    int looping        = AL_FALSE;
    int buffer         = AL_NONE;
    float minGain          = 0.0f;
    float maxGain          = 1.0f;
    float referralDistance = 1.0f;
    float rolloffFactor    = 1.0f;
    float maxDistance      = Float.MAX_VALUE;
    float pitch            = 1.0f;
    int buffersQueued    = 0;
    int buffersProcessed = 0;
    private AudioBufferSourceNode sourceNode;
    private PannerNode            pannerNode;
    private GainNode              outputNode;
    private double bufferPosition = 0;
    private double startTime;

    private Queue<Integer> queue;

    ALSource()
    {
        queue = new LinkedList<>();

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
    void update()
    {
        if (sourceNode == null)
            return;

        sourceNode.setLoop(looping == AL_TRUE);

        ALListener listener = getStateManager().listener;

        if (sourceRelative == AL_FALSE)
            pannerNode.setPosition(posX - listener.posX, posY - listener.posY, posZ - listener.posZ);
        else
            pannerNode.setPosition(posX, posY, posZ);

        pannerNode.setRefDistance(Math.max(0, referralDistance));
        pannerNode.setRolloffFactor(Math.max(0, rolloffFactor));
        pannerNode.setMaxDistance(Math.max(0, maxDistance));

        pannerNode.setOrientation(dirX, dirY, dirZ);

        pannerNode.setConeInnerAngle(coneInnerAngle);
        pannerNode.setConeOuterAngle(coneOuterAngle);
        pannerNode.setConeOuterGain(Math.min(Math.max(coneOuterGain, 0.0f), 1.0f));

        outputNode.getGain().setValue(Math.min(maxGain, Math.max(minGain, gain)));

        applyDoppler();
    }

    void applyDoppler()
    {
        if (sourceNode == null)
            return;

        // Pages 28 and 29, section 3.5.2 of specification
        // Thanks to KittyCat in #openal on Freenode for kind explanation of the algorithm to me

        StateManager stateManager = getStateManager();
        ALListener listener = stateManager.listener;

        float sourceToListenerX = posX - listener.posX;
        float sourceToListenerY = posY - listener.posY;
        float sourceToListenerZ = posZ - listener.posZ;
        final float sourceToListenerM = (float) Math.sqrt(sourceToListenerX * sourceToListenerX
                                                          + sourceToListenerY * sourceToListenerY
                                                          + sourceToListenerZ * sourceToListenerZ);

        if (sourceToListenerM != 0 && sourceToListenerM != 1)
        {
            sourceToListenerX /= sourceToListenerM;
            sourceToListenerY /= sourceToListenerM;
            sourceToListenerZ /= sourceToListenerM;
        }

        final float dotSourceToListenerNListenerVelocity = sourceToListenerX * listener.velX
                                                           + sourceToListenerY * listener.velY
                                                           + sourceToListenerZ * listener.velZ;

        final float dotSourceToListenerNSourceVelocity = sourceToListenerX * velX
                                                         + sourceToListenerY * velY
                                                         + sourceToListenerZ * velZ;

        final float speedOfSound = stateManager.speedOfSound / stateManager.dopplerFactor;

        final float sourceVelocityScalar = Math.min(dotSourceToListenerNSourceVelocity / sourceToListenerM, speedOfSound);
        final float listenerVelocityScalar = Math.min(dotSourceToListenerNListenerVelocity / sourceToListenerM, speedOfSound);

        // We don't multiply by the sampleRate, because it is already done by WebAudio API
        float dopplerShift = 1 /*getBufferManager().getBuffer(buffer).getSampleRate()*/
                             * (stateManager.speedOfSound - stateManager.dopplerFactor * listenerVelocityScalar)
                             / (stateManager.speedOfSound - stateManager.dopplerFactor * sourceVelocityScalar);

        if (Float.isNaN(dopplerShift))
            dopplerShift = 1;

        GWT.log("Doppler shift: " + dopplerShift);

        sourceNode.getPlaybackRate().setValue(pitch * dopplerShift);
    }

    void setSourceState(int state)
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

                    // Streaming sources have special cases
                    if (sourceType == AL_STREAMING)
                    {
                        if (buffersProcessed != buffersQueued)
                        {
                            // Queued buffers are still available, go play next
                            setSourceState(AL_PLAYING);
                        }
                        else if (looping == AL_FALSE)
                            setSourceState(AL_STOPPED);
                    }
                }
            });
            sourceNode.connect(pannerNode);

            if (sourceType == AL_STREAMING)
            {
                if (buffersProcessed == buffersQueued)
                {
                    setSourceState(AL_STOPPED);
                    return;
                }

                buffer = queue.poll();
                buffersProcessed++;

                // Insert this buffer at the end of the queue
                queue.add(buffer);
            }

            ALBuffer alBuffer = getBufferManager().getBuffer(buffer);

            if (alBuffer.isReady())
                sourceNode.setBuffer(alBuffer.audioBuffer);

            update(); // Update the source, and set the properties

            startTime = context.getCurrentTime();

            if (oldState == AL_PAUSED)
                sourceNode.start(0, bufferPosition % buffer == AL_NONE ? 0 :
                                    getBufferManager().getBuffer(buffer).audioBuffer.getDuration());
            else
                sourceNode.start(0, 0);
        }
        else if (state == AL_STOPPED)
        {
            sourceState = AL_STOPPED;
            buffersProcessed = 0;
            bufferPosition = 0;

            if (sourceNode == null)
                return;

            sourceNode.stop(0);
            sourceNode.disconnect();
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
            sourceNode.disconnect();
            sourceNode = null;
        }
        else if (state == AL_INITIAL)
            sourceState = AL_INITIAL;
    }
}
