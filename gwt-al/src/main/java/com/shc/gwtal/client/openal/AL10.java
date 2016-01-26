package com.shc.gwtal.client.openal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.shc.gwtal.client.webaudio.AudioBuffer;
import com.shc.gwtal.client.webaudio.Promise;
import com.shc.gwtal.client.webaudio.enums.ChannelCountMode;
import com.shc.gwtal.client.webaudio.enums.DistanceModelType;

import static com.shc.gwtal.client.openal.AL11.*;

/**
 * @author Sri Harsha Chilakapati
 */
public final class AL10
{
    /**
     * General tokens.
     */
    public static final int
            AL_INVALID = 0xFFFFFFFF,
            AL_NONE    = 0x0,
            AL_FALSE   = 0x0,
            AL_TRUE    = 0x1;

    /**
     * Error conditions.
     */
    public static final int
            AL_NO_ERROR          = 0x0,
            AL_INVALID_NAME      = 0xA001,
            AL_INVALID_ENUM      = 0xA002,
            AL_INVALID_VALUE     = 0xA003,
            AL_INVALID_OPERATION = 0xA004,
            AL_OUT_OF_MEMORY     = 0xA005;

    /**
     * Numerical queries.
     */
    public static final int
            AL_DOPPLER_FACTOR = 0xC000,
            AL_DISTANCE_MODEL = 0xD000;

    /**
     * String queries.
     */
    public static final int
            AL_VENDOR     = 0xB001,
            AL_VERSION    = 0xB002,
            AL_RENDERER   = 0xB003,
            AL_EXTENSIONS = 0xB004;

    /**
     * Distance attenuation models.
     */
    public static final int
            AL_INVERSE_DISTANCE         = 0xD001,
            AL_INVERSE_DISTANCE_CLAMPED = 0xD002;

    /**
     * Source types.
     */
    public static final int
            AL_SOURCE_ABSOLUTE = 0x201,
            AL_SOURCE_RELATIVE = 0x202;

    /**
     * Listener and Source attributes.
     */
    public static final int
            AL_POSITION = 0x1004,
            AL_VELOCITY = 0x1006,
            AL_GAIN     = 0x100A;

    /**
     * Source attributes.
     */
    public static final int
            AL_CONE_INNER_ANGLE = 0x1001,
            AL_CONE_OUTER_ANGLE = 0x1002,
            AL_PITCH            = 0x1003,
            AL_DIRECTION        = 0x1005,
            AL_LOOPING          = 0x1007,
            AL_BUFFER           = 0x1009,
            AL_SOURCE_STATE     = 0x1010,
            AL_CONE_OUTER_GAIN  = 0x1022,
            AL_SOURCE_TYPE      = 0x1027;

    /**
     * Source state.
     */
    public static final int
            AL_INITIAL = 0x1011,
            AL_PLAYING = 0x1012,
            AL_PAUSED  = 0x1013,
            AL_STOPPED = 0x1014;

    /**
     * Listener attributes.
     */
    public static final int AL_ORIENTATION = 0x100F;

    /**
     * Queue state.
     */
    public static final int
            AL_BUFFERS_QUEUED    = 0x1015,
            AL_BUFFERS_PROCESSED = 0x1016;

    /**
     * Gain bounds.
     */
    public static final int
            AL_MIN_GAIN = 0x100D,
            AL_MAX_GAIN = 0x100E;

    /**
     * Distance model attributes,
     */
    public static final int
            AL_REFERENCE_DISTANCE = 0x1020,
            AL_ROLLOFF_FACTOR     = 0x1021,
            AL_MAX_DISTANCE       = 0x1023;

    /**
     * Buffer attributes,
     */
    public static final int
            AL_FREQUENCY = 0x2001,
            AL_BITS      = 0x2002,
            AL_CHANNELS  = 0x2003,
            AL_SIZE      = 0x2004;

    /**
     * Buffer formats.
     */
    public static final int
            AL_FORMAT_MONO8    = 0x1100,
            AL_FORMAT_MONO16   = 0x1101,
            AL_FORMAT_STEREO8  = 0x1102,
            AL_FORMAT_STEREO16 = 0x1103;

    /**
     * Buffer state.
     */
    public static final int
            AL_UNUSED    = 0x2010,
            AL_PENDING   = 0x2011,
            AL_PROCESSED = 0x2012;

    private static int alError = AL_NO_ERROR;

    // Prevent instantiation
    private AL10()
    {
    }

    public static int alGetError()
    {
        int error = alError;
        alError = AL_NO_ERROR;
        return error;
    }

    public static int alGenBuffers()
    {
        return BufferManager.createBuffer();
    }

    public static int[] alGenBuffers(int n)
    {
        int[] buffers = new int[n];

        for (int i = 0; i < buffers.length; i++)
            buffers[i] = BufferManager.createBuffer();

        return buffers;
    }

    public static void alBufferData(int buffer, @UnusedParam int format, ArrayBuffer data, @UnusedParam int freq)
    {
        if (!BufferManager.isValid(buffer))
        {
            alError = AL_INVALID_VALUE;
            return;
        }

        Promise<AudioBuffer> promise = BufferManager.forBuffer(buffer).bufferData(data);
        promise.onError(new Promise.OnRejected()
        {
            @Override
            public void invoke(Object reason)
            {
                GWT.log(reason.toString());
                alError = AL_INVALID_VALUE;
            }
        });
    }

    public static void alDeleteBuffers(int... bufferIDs)
    {
        for (int bufferID : bufferIDs)
            if (BufferManager.isValid(bufferID))
                BufferManager.deleteBuffer(bufferID);
    }

    public static int alIsBuffer(int bufferID)
    {
        return BufferManager.isValid(bufferID) ? AL_TRUE : AL_FALSE;
    }

    public static int alGenSources()
    {
        return SourceManager.createSource();
    }

    public static int[] alGenSources(int n)
    {
        int[] sources = new int[n];

        for (int i = 0; i < n; i++)
            sources[i] = BufferManager.createBuffer();

        return sources;
    }

    public static void alDeleteSources(int... sourceIDs)
    {
        for (int sourceID : sourceIDs)
            if (SourceManager.isValid(sourceID))
                SourceManager.deleteSource(sourceID);
    }

    public static int alIsSource(int sourceID)
    {
        return SourceManager.isValid(sourceID) ? AL_TRUE : AL_FALSE;
    }

    public static String alGetString(int paramName)
    {
        switch (paramName)
        {
            case AL_VERSION:
                return "1.1 GWT-AL";

            case AL_RENDERER:
                return "WebAudioAPI based GWT-AL renderer";

            case AL_VENDOR:
                return "GWT-AL";

            case AL_EXTENSIONS:
                return "";
        }

        alError = AL_INVALID_ENUM;
        return null;
    }

    public static void alDistanceModel(int modelName)
    {
        StateManager stateManager = StateManager.forContext(AL.getContext());

        switch (modelName)
        {
            case AL_NONE:
                stateManager.pannerEnabled = false;
                stateManager.updatePipeline();
                return;

            case AL_INVERSE_DISTANCE:
                stateManager.pannerEnabled = true;
                stateManager.pannerNode.setDistanceModel(DistanceModelType.INVERSE);
                stateManager.pannerNode.setChannelCountMode(ChannelCountMode.MAX);
                stateManager.updatePipeline();
                return;

            case AL_INVERSE_DISTANCE_CLAMPED:
                stateManager.pannerEnabled = true;
                stateManager.pannerNode.setDistanceModel(DistanceModelType.INVERSE);
                stateManager.pannerNode.setChannelCountMode(ChannelCountMode.CLAMPED_MAX);
                stateManager.updatePipeline();
                return;

            case AL_LINEAR_DISTANCE:
                stateManager.pannerEnabled = true;
                stateManager.pannerNode.setDistanceModel(DistanceModelType.LINEAR);
                stateManager.pannerNode.setChannelCountMode(ChannelCountMode.MAX);
                stateManager.updatePipeline();
                return;

            case AL_LINEAR_DISTANCE_CLAMPED:
                stateManager.pannerEnabled = true;
                stateManager.pannerNode.setDistanceModel(DistanceModelType.LINEAR);
                stateManager.pannerNode.setChannelCountMode(ChannelCountMode.CLAMPED_MAX);
                stateManager.updatePipeline();
                return;

            case AL_EXPONENT_DISTANCE:
                stateManager.pannerEnabled = true;
                stateManager.pannerNode.setDistanceModel(DistanceModelType.EXPONENTIAL);
                stateManager.pannerNode.setChannelCountMode(ChannelCountMode.MAX);
                stateManager.updatePipeline();
                return;

            case AL_EXPONENT_DISTANCE_CLAMPED:
                stateManager.pannerEnabled = true;
                stateManager.pannerNode.setDistanceModel(DistanceModelType.EXPONENTIAL);
                stateManager.pannerNode.setChannelCountMode(ChannelCountMode.CLAMPED_MAX);
                stateManager.updatePipeline();
                return;
        }

        alError = AL_INVALID_VALUE;
    }
}
