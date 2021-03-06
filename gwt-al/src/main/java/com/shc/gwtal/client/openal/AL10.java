package com.shc.gwtal.client.openal;

import com.google.gwt.typedarrays.client.DataViewNative;
import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.typedarrays.shared.DataView;
import com.google.gwt.typedarrays.shared.Float32Array;
import com.google.gwt.typedarrays.shared.Int32Array;
import com.shc.gwtal.client.webaudio.AudioBuffer;
import com.shc.gwtal.client.webaudio.AudioContext;
import com.shc.gwtal.client.webaudio.enums.ChannelCountMode;
import com.shc.gwtal.client.webaudio.enums.DistanceModelType;

import java.util.ArrayList;

import static com.shc.gwtal.client.openal.AL11.*;
import static com.shc.gwtal.client.openal.ALUtils.*;
import static com.shc.gwtal.client.openal.EXTFloat32.*;

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
     * Distance model attributes.
     */
    public static final int
            AL_REFERENCE_DISTANCE = 0x1020,
            AL_ROLLOFF_FACTOR     = 0x1021,
            AL_MAX_DISTANCE       = 0x1023;

    /**
     * Buffer attributes.
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

    // Prevent instantiation
    private AL10()
    {
    }

    public static int alGetError()
    {
        if (AL.getCurrentContext() == null)
            return AL_INVALID_OPERATION;

        return getStateManager().getError();
    }

    public static int alGenBuffers()
    {
        if (AL.getCurrentContext() == null)
            return 0;

        return getBufferManager().createBuffer();
    }

    public static int[] alGenBuffers(int n)
    {
        if (AL.getCurrentContext() == null)
            return new int[n];  // Int array will be 0 by default

        int[] buffers = new int[n];

        for (int i = 0; i < buffers.length; i++)
            buffers[i] = getBufferManager().createBuffer();

        return buffers;
    }

    public static void alBufferData(int buffer, int format, ArrayBuffer data, @UnusedParam int freq)
    {
        alBufferData(buffer, format, data, data.byteLength(), freq);
    }

    public static void alBufferData(int buffer, int format, ArrayBuffer data, int size, int freq)
    {
        final StateManager stateManager = getStateManager();

        if (!getBufferManager().isValid(buffer))
        {
            stateManager.setError(AL_INVALID_VALUE);
            return;
        }

        int bytes, channels;

        switch (format)
        {
            case AL_FORMAT_MONO8:
                bytes = 1;
                channels = 1;
                break;

            case AL_FORMAT_MONO16:
                bytes = 2;
                channels = 1;
                break;

            case AL_FORMAT_STEREO8:
                bytes = 1;
                channels = 2;
                break;

            case AL_FORMAT_STEREO16:
                bytes = 2;
                channels = 2;
                break;

            case AL_FORMAT_MONO_FLOAT32:
                bytes = 4;
                channels = 1;
                break;

            case AL_FORMAT_STEREO_FLOAT32:
                bytes = 4;
                channels = 2;
                break;

            default:
                stateManager.setError(AL_INVALID_ENUM);
                return;
        }

        BufferManager bufferManager = getBufferManager();
        AudioContext ctx = AL.getCurrentContext().getWebAudioContext();

        AudioBuffer audioBuffer;

        try
        {
            audioBuffer = ctx.createBuffer(channels, size / (bytes * channels), freq);
        }
        catch (Exception e)
        {
            stateManager.setError(AL_INVALID_VALUE);
            return;
        }

        ArrayList<Float32Array> buf = new ArrayList<>();

        for (int i = 0; i < channels; i++)
            buf.add(audioBuffer.getChannelData(i));

        DataView dataView = DataViewNative.create(data);

        for (int i = 0; i < size / (bytes * channels); ++i)
        {
            for (int j = 0; j < channels; ++j)
            {
                switch (bytes)
                {
                    case 1:
                        buf.get(j).set(i, -1.0f * dataView.getInt8(i * channels + j) * (2f / 256f));
                        break;

                    case 2:
                        buf.get(j).set(i, dataView.getInt16(2 * (i * channels + j)) / 32768f);
                        break;

                    case 4:
                        buf.get(j).set(i, dataView.getFloat32(4 * (i * channels + j)));
                        break;
                }
            }
        }

        bufferManager.getBuffer(buffer).audioBuffer = audioBuffer;
    }

    public static void alDeleteBuffers(int... bufferIDs)
    {
        BufferManager bufferManager = getBufferManager();

        for (int bufferID : bufferIDs)
            if (bufferManager.isValid(bufferID))
                bufferManager.deleteBuffer(bufferID);
    }

    public static int alIsBuffer(int bufferID)
    {
        return getBufferManager().isValid(bufferID) ? AL_TRUE : AL_FALSE;
    }

    public static int alGenSources()
    {
        return getSourceManager().createSource();
    }

    public static int[] alGenSources(int n)
    {
        int[] sources = new int[n];

        for (int i = 0; i < n; i++)
            sources[i] = getSourceManager().createSource();

        return sources;
    }

    public static void alDeleteSources(int... sourceIDs)
    {
        SourceManager sourceManager = getSourceManager();

        for (int sourceID : sourceIDs)
            if (sourceManager.isValid(sourceID))
                sourceManager.deleteSource(sourceID);
    }

    public static int alIsSource(int sourceID)
    {
        return getSourceManager().isValid(sourceID) ? AL_TRUE : AL_FALSE;
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

            case AL_NO_ERROR:
                return "There is no current error";

            case AL_INVALID_NAME:
                return "Invalid name parameter";

            case AL_INVALID_ENUM:
                return "Invalid parameter";

            case AL_INVALID_VALUE:
                return "Invalid enum parameter value";

            case AL_INVALID_OPERATION:
                return "Illegal call";

            case AL_OUT_OF_MEMORY:
                return "Unable to allocate memory";
        }

        getStateManager().setError(AL_INVALID_ENUM);
        return null;
    }

    public static void alDistanceModel(int modelName)
    {
        StateManager stateManager = getStateManager();

        switch (modelName)
        {
            case AL_NONE:
                stateManager.pannerEnabled = false;
                stateManager.updatePipeline();
                stateManager.distanceModel = modelName;
                return;

            case AL_INVERSE_DISTANCE:
                stateManager.pannerEnabled = true;
                stateManager.pannerNode.setDistanceModel(DistanceModelType.INVERSE);
                stateManager.pannerNode.setChannelCountMode(ChannelCountMode.MAX);
                stateManager.updatePipeline();
                stateManager.distanceModel = modelName;
                return;

            case AL_INVERSE_DISTANCE_CLAMPED:
                stateManager.pannerEnabled = true;
                stateManager.pannerNode.setDistanceModel(DistanceModelType.INVERSE);
                stateManager.pannerNode.setChannelCountMode(ChannelCountMode.CLAMPED_MAX);
                stateManager.updatePipeline();
                stateManager.distanceModel = modelName;
                return;

            case AL_LINEAR_DISTANCE:
                stateManager.pannerEnabled = true;
                stateManager.pannerNode.setDistanceModel(DistanceModelType.LINEAR);
                stateManager.pannerNode.setChannelCountMode(ChannelCountMode.MAX);
                stateManager.updatePipeline();
                stateManager.distanceModel = modelName;
                return;

            case AL_LINEAR_DISTANCE_CLAMPED:
                stateManager.pannerEnabled = true;
                stateManager.pannerNode.setDistanceModel(DistanceModelType.LINEAR);
                stateManager.pannerNode.setChannelCountMode(ChannelCountMode.CLAMPED_MAX);
                stateManager.updatePipeline();
                stateManager.distanceModel = modelName;
                return;

            case AL_EXPONENT_DISTANCE:
                stateManager.pannerEnabled = true;
                stateManager.pannerNode.setDistanceModel(DistanceModelType.EXPONENTIAL);
                stateManager.pannerNode.setChannelCountMode(ChannelCountMode.MAX);
                stateManager.updatePipeline();
                stateManager.distanceModel = modelName;
                return;

            case AL_EXPONENT_DISTANCE_CLAMPED:
                stateManager.pannerEnabled = true;
                stateManager.pannerNode.setDistanceModel(DistanceModelType.EXPONENTIAL);
                stateManager.pannerNode.setChannelCountMode(ChannelCountMode.CLAMPED_MAX);
                stateManager.updatePipeline();
                stateManager.distanceModel = modelName;
                return;
        }

        stateManager.setError(AL_INVALID_ENUM);
    }

    public static void alSource3f(int source, int param, float v1, float v2, float v3)
    {
        StateManager stateManager = getStateManager();
        SourceManager sourceManager = getSourceManager();

        if (!sourceManager.isValid(source))
        {
            stateManager.setError(AL_INVALID_NAME);
            return;
        }

        ALSource sourceObject = sourceManager.getSource(source);

        switch (param)
        {
            case AL_SOURCE_TYPE:
            case AL_BUFFERS_QUEUED:
            case AL_BUFFERS_PROCESSED:
                stateManager.setError(AL_INVALID_OPERATION);
                return;

            case AL_POSITION:
                if (!areAllFinite(v1, v2, v3))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.posX = v1;
                sourceObject.posY = v2;
                sourceObject.posZ = v3;
                sourceObject.update();
                return;

            case AL_VELOCITY:
                if (!areAllFinite(v1, v2, v3))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.velX = v1;
                sourceObject.velY = v2;
                sourceObject.velZ = v3;
                sourceObject.update();
                return;

            case AL_DIRECTION:
                if (!areAllFinite(v1, v2, v3))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.dirX = v1;
                sourceObject.dirY = v2;
                sourceObject.dirZ = v3;
                sourceObject.update();
                return;
        }

        stateManager.setError(AL_INVALID_ENUM);
    }

    public static void alSourcef(int source, int param, float value)
    {
        StateManager stateManager = getStateManager();
        SourceManager sourceManager = getSourceManager();

        if (!sourceManager.isValid(source))
        {
            stateManager.setError(AL_INVALID_NAME);
            return;
        }

        ALSource sourceObject = sourceManager.getSource(source);

        switch (param)
        {
            case AL_SOURCE_TYPE:
            case AL_BUFFERS_QUEUED:
            case AL_BUFFERS_PROCESSED:
                stateManager.setError(AL_INVALID_OPERATION);
                return;

            case AL_GAIN:
                sourceObject.gain = value;
                sourceObject.update();
                return;

            case AL_PITCH:
                if (value <= 0)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.pitch = value;
                sourceObject.update();
                return;

            case AL_MAX_GAIN:
                if (value < 0f || value > 1f)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.maxGain = value;
                sourceObject.update();
                return;

            case AL_MIN_GAIN:
                if (value < 0f || value > 1f)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.minGain = value;
                sourceObject.update();
                return;

            case AL_REFERENCE_DISTANCE:
                if (value < 0f)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.referralDistance = value;
                sourceObject.update();
                return;

            case AL_ROLLOFF_FACTOR:
                if (value < 0f)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.rolloffFactor = value;
                sourceObject.update();
                return;

            case AL_MAX_DISTANCE:
                if (value < 0f)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.maxDistance = value;
                sourceObject.update();
                return;

            case AL_CONE_INNER_ANGLE:
                if (!isFinite(value))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.coneInnerAngle = value;
                sourceObject.update();
                return;

            case AL_CONE_OUTER_ANGLE:
                if (!isFinite(value))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.coneOuterAngle = value;
                sourceObject.update();
                return;

            case AL_CONE_OUTER_GAIN:
                if (value < 0 || value > 1)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.coneOuterGain = value;
                sourceObject.update();
                return;
        }

        stateManager.setError(AL_INVALID_ENUM);
    }

    public static void alSourcefv(int source, int param, Float32Array values)
    {
        StateManager stateManager = getStateManager();
        SourceManager sourceManager = getSourceManager();

        if (!sourceManager.isValid(source))
        {
            stateManager.setError(AL_INVALID_NAME);
            return;
        }

        ALSource sourceObject = sourceManager.getSource(source);

        switch (param)
        {
            case AL_SOURCE_TYPE:
            case AL_BUFFERS_QUEUED:
            case AL_BUFFERS_PROCESSED:
                stateManager.setError(AL_INVALID_OPERATION);
                return;

            case AL_POSITION:
                if (!areAllFinite(values.get(0), values.get(1), values.get(2)))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.posX = values.get(0);
                sourceObject.posY = values.get(1);
                sourceObject.posZ = values.get(2);
                sourceObject.update();
                return;

            case AL_VELOCITY:
                if (!areAllFinite(values.get(0), values.get(1), values.get(2)))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.velX = values.get(0);
                sourceObject.velY = values.get(1);
                sourceObject.velZ = values.get(2);
                sourceObject.update();
                return;

            case AL_GAIN:
                sourceObject.gain = values.get(0);
                sourceObject.update();
                return;

            case AL_MAX_GAIN:
                float value = values.get(0);
                if (value < 0f || value > 1f)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.maxGain = value;
                sourceObject.update();
                return;

            case AL_MIN_GAIN:
                value = values.get(0);
                if (value < 0f || value > 1f)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.minGain = value;
                sourceObject.update();
                return;

            case AL_PITCH:
                value = values.get(0);
                if (value <= 0)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.pitch = value;
                sourceObject.update();
                return;

            case AL_REFERENCE_DISTANCE:
                value = values.get(0);
                if (value < 0f)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.referralDistance = value;
                sourceObject.update();
                return;

            case AL_ROLLOFF_FACTOR:
                value = values.get(0);
                if (value < 0f)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.rolloffFactor = value;
                sourceObject.update();
                return;

            case AL_MAX_DISTANCE:
                value = values.get(0);
                if (value < 0f)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.maxDistance = value;
                sourceObject.update();
                return;

            case AL_DIRECTION:
                if (!areAllFinite(values.get(1), values.get(2), values.get(3)))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.dirX = values.get(1);
                sourceObject.dirY = values.get(2);
                sourceObject.dirZ = values.get(3);
                sourceObject.update();
                return;

            case AL_CONE_INNER_ANGLE:
                value = values.get(0);
                if (!isFinite(value))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.coneInnerAngle = value;
                sourceObject.update();
                return;

            case AL_CONE_OUTER_ANGLE:
                value = values.get(0);
                if (!isFinite(value))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.coneOuterAngle = value;
                sourceObject.update();
                return;

            case AL_CONE_OUTER_GAIN:
                value = values.get(0);
                if (value < 0 || value > 1)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.coneOuterGain = value;
                sourceObject.update();
                return;
        }

        stateManager.setError(AL_INVALID_ENUM);
    }

    public static void alSourceiv(int source, int param, Int32Array values)
    {
        StateManager stateManager = getStateManager();
        SourceManager sourceManager = getSourceManager();

        if (!sourceManager.isValid(source))
        {
            stateManager.setError(AL_INVALID_NAME);
            return;
        }

        ALSource sourceObject = sourceManager.getSource(source);

        switch (param)
        {
            case AL_SOURCE_TYPE:
            case AL_BUFFERS_QUEUED:
            case AL_BUFFERS_PROCESSED:
                stateManager.setError(AL_INVALID_OPERATION);
                return;

            case AL_POSITION:
                if (!areAllFinite(values.get(0), values.get(1), values.get(2)))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.posX = values.get(0);
                sourceObject.posY = values.get(1);
                sourceObject.posZ = values.get(2);
                sourceObject.update();
                return;

            case AL_VELOCITY:
                if (!areAllFinite(values.get(0), values.get(1), values.get(2)))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.velX = values.get(0);
                sourceObject.velY = values.get(1);
                sourceObject.velZ = values.get(2);
                sourceObject.update();
                return;

            case AL_SOURCE_RELATIVE:
                int value = values.get(0);
                if (value != AL_TRUE && value != AL_FALSE)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.sourceRelative = value;
                sourceObject.update();
                return;

            case AL_LOOPING:
                value = values.get(0);
                if (value != AL_TRUE && value != AL_FALSE)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.looping = value;
                sourceObject.update();
                return;

            case AL_BUFFER:
                value = values.get(0);
                switch (sourceObject.sourceState)
                {
                    case AL_PAUSED:
                    case AL_PLAYING:
                        stateManager.setError(AL_INVALID_OPERATION);
                        return;
                }
                if (!getBufferManager().isValid(value))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.sourceType = value == AL_NONE ? AL_STATIC : AL_UNDETERMINED;
                sourceObject.buffer = value;
                sourceObject.update();
                return;

            case AL_REFERENCE_DISTANCE:
                value = values.get(0);
                if (value < 0)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.referralDistance = value;
                sourceObject.update();
                return;

            case AL_ROLLOFF_FACTOR:
                value = values.get(0);
                if (value < 0)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.rolloffFactor = value;
                sourceObject.update();
                return;

            case AL_MAX_DISTANCE:
                value = values.get(0);
                if (value < 0)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.maxDistance = value;
                sourceObject.update();
                return;

            case AL_DIRECTION:
                if (!areAllFinite(values.get(1), values.get(2), values.get(3)))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.dirX = values.get(1);
                sourceObject.dirY = values.get(2);
                sourceObject.dirZ = values.get(3);
                sourceObject.update();
                return;

            case AL_CONE_INNER_ANGLE:
                value = values.get(0);
                if (!isFinite(value))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.coneInnerAngle = value;
                sourceObject.update();
                return;

            case AL_CONE_OUTER_ANGLE:
                value = values.get(0);
                if (!isFinite(value))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.coneOuterAngle = value;
                sourceObject.update();
                return;
        }

        stateManager.setError(AL_INVALID_ENUM);
    }

    public static void alSourcei(int source, int param, int value)
    {
        StateManager stateManager = getStateManager();
        SourceManager sourceManager = getSourceManager();

        if (!sourceManager.isValid(source))
        {
            stateManager.setError(AL_INVALID_NAME);
            return;
        }

        ALSource sourceObject = sourceManager.getSource(source);

        switch (param)
        {
            case AL_SOURCE_TYPE:
            case AL_BUFFERS_QUEUED:
            case AL_BUFFERS_PROCESSED:
                stateManager.setError(AL_INVALID_OPERATION);
                return;

            case AL_SOURCE_RELATIVE:
                if (value != AL_TRUE && value != AL_FALSE)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.sourceRelative = value;
                sourceObject.update();
                return;

            case AL_LOOPING:
                if (value != AL_TRUE && value != AL_FALSE)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.looping = value;
                sourceObject.update();
                return;

            case AL_BUFFER:
                switch (sourceObject.sourceState)
                {
                    case AL_PAUSED:
                    case AL_PLAYING:
                        stateManager.setError(AL_INVALID_OPERATION);
                        return;
                }
                if (!getBufferManager().isValid(value))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.sourceType = value == AL_NONE ? AL_STATIC : AL_UNDETERMINED;
                sourceObject.buffer = value;
                sourceObject.update();
                return;

            case AL_REFERENCE_DISTANCE:
                if (value < 0)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.referralDistance = value;
                sourceObject.update();
                return;

            case AL_ROLLOFF_FACTOR:
                if (value < 0)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.rolloffFactor = value;
                sourceObject.update();
                return;

            case AL_MAX_DISTANCE:
                if (value < 0)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.maxDistance = value;
                sourceObject.update();
                return;

            case AL_CONE_INNER_ANGLE:
                if (!isFinite(value))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.coneInnerAngle = value;
                sourceObject.update();
                return;

            case AL_CONE_OUTER_ANGLE:
                if (!isFinite(value))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.coneOuterAngle = value;
                sourceObject.update();
                return;
        }

        stateManager.setError(AL_INVALID_ENUM);
    }

    public static void alSource3i(int source, int param, int v1, int v2, int v3)
    {
        StateManager stateManager = getStateManager();
        SourceManager sourceManager = getSourceManager();

        if (!sourceManager.isValid(source))
        {
            stateManager.setError(AL_INVALID_NAME);
            return;
        }

        ALSource sourceObject = sourceManager.getSource(source);

        switch (param)
        {
            case AL_SOURCE_TYPE:
            case AL_BUFFERS_QUEUED:
            case AL_BUFFERS_PROCESSED:
                stateManager.setError(AL_INVALID_OPERATION);
                return;

            case AL_POSITION:
                if (!areAllFinite(v1, v2, v3))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.posX = v1;
                sourceObject.posY = v2;
                sourceObject.posZ = v3;
                sourceObject.update();
                return;

            case AL_VELOCITY:
                if (!areAllFinite(v1, v2, v3))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.velX = v1;
                sourceObject.velY = v2;
                sourceObject.velZ = v3;
                sourceObject.update();
                return;

            case AL_DIRECTION:
                if (!areAllFinite(v1, v2, v3))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                sourceObject.dirX = v1;
                sourceObject.dirY = v2;
                sourceObject.dirZ = v3;
                sourceObject.update();
                return;
        }

        stateManager.setError(AL_INVALID_ENUM);
    }

    public static void alSourcePlay(int source)
    {
        StateManager stateManager = getStateManager();
        SourceManager sourceManager = getSourceManager();

        if (!sourceManager.isValid(source))
        {
            stateManager.setError(AL_INVALID_NAME);
            return;
        }

        ALSource src = sourceManager.getSource(source);
        src.setSourceState(AL_PLAYING);
    }

    public static void alSourceStop(int source)
    {
        StateManager stateManager = getStateManager();
        SourceManager sourceManager = getSourceManager();

        if (!sourceManager.isValid(source))
        {
            stateManager.setError(AL_INVALID_NAME);
            return;
        }

        ALSource src = sourceManager.getSource(source);
        src.setSourceState(AL_STOPPED);
    }

    public static void alSourcePause(int source)
    {
        StateManager stateManager = getStateManager();
        SourceManager sourceManager = getSourceManager();

        if (!sourceManager.isValid(source))
        {
            stateManager.setError(AL_INVALID_NAME);
            return;
        }

        ALSource src = sourceManager.getSource(source);
        src.setSourceState(AL_PAUSED);
    }

    public static void alSourceRewind(int source)
    {
        StateManager stateManager = getStateManager();
        SourceManager sourceManager = getSourceManager();

        if (!sourceManager.isValid(source))
        {
            stateManager.setError(AL_INVALID_NAME);
            return;
        }

        ALSource src = sourceManager.getSource(source);

        src.setSourceState(AL_STOPPED);
        src.setSourceState(AL_INITIAL);
    }

    public static void alSourcePlayv(int n, int[] sNames)
    {
        for (int i = 0; i < n; i++)
            alSourcePlay(sNames[i]);
    }

    public static void alSourcePausev(int n, int[] sNames)
    {
        for (int i = 0; i < n; i++)
            alSourcePause(sNames[i]);
    }

    public static void alSourceStopv(int n, int[] sNames)
    {
        for (int i = 0; i < n; i++)
            alSourceStop(sNames[i]);
    }

    public static void alSourceRewindv(int n, int[] sNames)
    {
        for (int i = 0; i < n; i++)
            alSourceRewind(sNames[i]);
    }

    public static void alSourcePlayv(int... sNames)
    {
        for (int source : sNames)
            alSourcePlay(source);
    }

    public static void alSourcePausev(int... sNames)
    {
        for (int source : sNames)
            alSourcePause(source);
    }

    public static void alSourceStopv(int... sNames)
    {
        for (int source : sNames)
            alSourceStop(source);
    }

    public static void alSourceRewindv(int... sNames)
    {
        for (int source : sNames)
            alSourceRewind(source);
    }

    public static void alGetSource3f(int source, int param, ArrayBuffer v1, ArrayBuffer v2, ArrayBuffer v3)
    {
        alGetSource3f(source, param, DataViewNative.create(v1), DataViewNative.create(v2), DataViewNative.create(v3));
    }

    public static void alGetSource3f(int source, int param, DataView v1, DataView v2, DataView v3)
    {
        StateManager stateManager = getStateManager();
        SourceManager sourceManager = getSourceManager();

        // Ensure buffer size for a float
        if (hasEnoughBytes(v1, SIZEOF_FLOAT) ||
            hasEnoughBytes(v2, SIZEOF_FLOAT) ||
            hasEnoughBytes(v3, SIZEOF_FLOAT))
        {
            stateManager.setError(AL_INVALID_VALUE);
            return;
        }

        ALSource alSource = sourceManager.getSource(source);

        switch (param)
        {
            case AL_DIRECTION:
                v1.setFloat32(0, alSource.dirX);
                v2.setFloat32(0, alSource.dirY);
                v3.setFloat32(0, alSource.dirZ);
                return;

            case AL_POSITION:
                v1.setFloat32(0, alSource.posX);
                v2.setFloat32(0, alSource.posY);
                v3.setFloat32(0, alSource.posZ);
                return;

            case AL_VELOCITY:
                v1.setFloat32(0, alSource.velX);
                v2.setFloat32(0, alSource.velY);
                v3.setFloat32(0, alSource.velZ);
                return;
        }

        stateManager.setError(AL_INVALID_ENUM);
    }

    public static void alGetSource3i(int source, int param, ArrayBuffer v1, ArrayBuffer v2, ArrayBuffer v3)
    {
        alGetSource3i(source, param, DataViewNative.create(v1), DataViewNative.create(v2), DataViewNative.create(v3));
    }

    public static void alGetSource3i(int source, int param, DataView v1, DataView v2, DataView v3)
    {
        StateManager stateManager = getStateManager();
        SourceManager sourceManager = getSourceManager();

        // Ensure buffer size for an int
        if (hasEnoughBytes(v1, SIZEOF_INT) ||
            hasEnoughBytes(v2, SIZEOF_INT) ||
            hasEnoughBytes(v3, SIZEOF_INT))
        {
            stateManager.setError(AL_INVALID_VALUE);
            return;
        }

        ALSource alSource = sourceManager.getSource(source);

        switch (param)
        {
            case AL_DIRECTION:
                v1.setInt32(0, (int) alSource.dirX);
                v2.setInt32(0, (int) alSource.dirY);
                v3.setInt32(0, (int) alSource.dirZ);
                return;

            case AL_POSITION:
                v1.setInt32(0, (int) alSource.posX);
                v2.setInt32(0, (int) alSource.posY);
                v3.setInt32(0, (int) alSource.posZ);
                return;

            case AL_VELOCITY:
                v1.setInt32(0, (int) alSource.velX);
                v2.setInt32(0, (int) alSource.velY);
                v3.setInt32(0, (int) alSource.velZ);
                return;
        }

        stateManager.setError(AL_INVALID_ENUM);
    }

    public static float alGetSourcef(int source, int param)
    {
        StateManager stateManager = getStateManager();
        SourceManager sourceManager = getSourceManager();

        if (!sourceManager.isValid(source))
        {
            stateManager.setError(AL_INVALID_NAME);
            return 0;
        }

        ALSource alSource = sourceManager.getSource(source);

        switch (param)
        {
            case AL_MIN_GAIN:
                return alSource.minGain;

            case AL_MAX_GAIN:
                return alSource.maxGain;

            case AL_REFERENCE_DISTANCE:
                return alSource.referralDistance;

            case AL_ROLLOFF_FACTOR:
                return alSource.rolloffFactor;

            case AL_MAX_DISTANCE:
                return alSource.maxDistance;

            case AL_PITCH:
                return alSource.pitch;

            case AL_CONE_INNER_ANGLE:
                return alSource.coneInnerAngle;

            case AL_CONE_OUTER_ANGLE:
                return alSource.coneOuterAngle;

            case AL_CONE_OUTER_GAIN:
                return alSource.coneOuterGain;

            case AL_GAIN:
                return alSource.gain;
        }

        stateManager.setError(AL_INVALID_ENUM);
        return 0;
    }

    public static void alGetSourcef(int source, int param, DataView value)
    {
        // Ensure buffer size for a float
        if (hasEnoughBytes(value, SIZEOF_FLOAT))
        {
            getStateManager().setError(AL_INVALID_VALUE);
            return;
        }

        value.setFloat32(0, alGetSourcef(source, param));
    }

    public static void alGetSourcef(int source, int param, ArrayBuffer value)
    {
        alGetSourcef(source, param, DataViewNative.create(value));
    }

    public static void alGetSourcefv(int source, int param, DataView values)
    {
        StateManager stateManager = getStateManager();
        SourceManager sourceManager = getSourceManager();

        if (!sourceManager.isValid(source))
        {
            stateManager.setError(AL_INVALID_NAME);
            return;
        }

        ALSource alSource = sourceManager.getSource(source);

        switch (param)
        {
            case AL_MIN_GAIN:
                if (!checkSetError(!hasEnoughBytes(values, SIZEOF_FLOAT), AL_INVALID_VALUE))
                    values.setFloat32(0, alSource.minGain);
                return;

            case AL_MAX_GAIN:
                if (!checkSetError(!hasEnoughBytes(values, SIZEOF_FLOAT), AL_INVALID_VALUE))
                    values.setFloat32(0, alSource.maxGain);
                return;

            case AL_REFERENCE_DISTANCE:
                if (!checkSetError(!hasEnoughBytes(values, SIZEOF_FLOAT), AL_INVALID_VALUE))
                    values.setFloat32(0, alSource.referralDistance);
                return;

            case AL_ROLLOFF_FACTOR:
                if (!checkSetError(!hasEnoughBytes(values, SIZEOF_FLOAT), AL_INVALID_VALUE))
                    values.setFloat32(0, alSource.rolloffFactor);
                return;

            case AL_MAX_DISTANCE:
                if (!checkSetError(!hasEnoughBytes(values, SIZEOF_FLOAT), AL_INVALID_VALUE))
                    values.setFloat32(0, alSource.maxDistance);
                return;

            case AL_PITCH:
                if (!checkSetError(!hasEnoughBytes(values, SIZEOF_FLOAT), AL_INVALID_VALUE))
                    values.setFloat32(0, alSource.pitch);
                return;

            case AL_DIRECTION:
                if (!checkSetError(!hasEnoughBytes(values, 3 * SIZEOF_FLOAT), AL_INVALID_VALUE))
                {
                    values.setFloat32(0, alSource.dirX);
                    values.setFloat32(SIZEOF_FLOAT, alSource.dirY);
                    values.setFloat32(2 * SIZEOF_FLOAT, alSource.dirZ);
                }
                return;

            case AL_CONE_INNER_ANGLE:
                if (!checkSetError(!hasEnoughBytes(values, SIZEOF_FLOAT), AL_INVALID_VALUE))
                    values.setFloat32(0, alSource.coneInnerAngle);
                return;

            case AL_CONE_OUTER_ANGLE:
                if (!checkSetError(!hasEnoughBytes(values, SIZEOF_FLOAT), AL_INVALID_VALUE))
                    values.setFloat32(0, alSource.coneOuterAngle);
                return;

            case AL_CONE_OUTER_GAIN:
                if (!checkSetError(!hasEnoughBytes(values, SIZEOF_FLOAT), AL_INVALID_VALUE))
                    values.setFloat32(0, alSource.coneOuterGain);
                return;

            case AL_POSITION:
                if (!checkSetError(!hasEnoughBytes(values, 3 * SIZEOF_FLOAT), AL_INVALID_VALUE))
                {
                    values.setFloat32(0, alSource.posX);
                    values.setFloat32(SIZEOF_FLOAT, alSource.posY);
                    values.setFloat32(2 * SIZEOF_FLOAT, alSource.posZ);
                }
                return;

            case AL_VELOCITY:
                if (!checkSetError(!hasEnoughBytes(values, 3 * SIZEOF_FLOAT), AL_INVALID_VALUE))
                {
                    values.setFloat32(0, alSource.velX);
                    values.setFloat32(SIZEOF_FLOAT, alSource.velY);
                    values.setFloat32(2 * SIZEOF_FLOAT, alSource.velZ);
                }
                return;

            case AL_GAIN:
                if (!checkSetError(!hasEnoughBytes(values, SIZEOF_FLOAT), AL_INVALID_VALUE))
                    values.setFloat32(0, alSource.gain);
                return;
        }

        stateManager.setError(AL_INVALID_ENUM);
    }

    public static void alGetSourcefv(int source, int param, ArrayBuffer values)
    {
        alGetSourcefv(source, param, DataViewNative.create(values));
    }

    public static int alGetSourcei(int source, int param)
    {
        StateManager stateManager = getStateManager();
        SourceManager sourceManager = getSourceManager();

        if (!sourceManager.isValid(source))
        {
            stateManager.setError(AL_INVALID_NAME);
            return 0;
        }

        ALSource alSource = sourceManager.getSource(source);

        switch (param)
        {
            case AL_SOURCE_RELATIVE:
                return alSource.sourceRelative;

            case AL_SOURCE_TYPE:
                return alSource.sourceType;

            case AL_LOOPING:
                return alSource.looping;

            case AL_BUFFER:
                return alSource.buffer;

            case AL_BUFFERS_QUEUED:
                return alSource.buffersQueued;

            case AL_BUFFERS_PROCESSED:
                return alSource.buffersProcessed;

            case AL_REFERENCE_DISTANCE:
                return (int) alSource.referralDistance;

            case AL_ROLLOFF_FACTOR:
                return (int) alSource.rolloffFactor;

            case AL_MAX_DISTANCE:
                return (int) alSource.maxDistance;

            case AL_CONE_INNER_ANGLE:
                return (int) alSource.coneInnerAngle;

            case AL_CONE_OUTER_ANGLE:
                return (int) alSource.coneOuterAngle;

            case AL_SOURCE_STATE:
                return alSource.sourceState;
        }

        stateManager.setError(AL_INVALID_ENUM);
        return 0;
    }

    public static void alGetSourcei(int source, int param, DataView value)
    {
        // Ensure buffer size for a float
        if (hasEnoughBytes(value, SIZEOF_FLOAT))
        {
            getStateManager().setError(AL_INVALID_VALUE);
            return;
        }

        value.setInt32(0, alGetSourcei(source, param));
    }

    public static void alGetSourcei(int source, int param, ArrayBuffer value)
    {
        alGetSourcei(source, param, DataViewNative.create(value));
    }

    public static void alGetSourceiv(int source, int param, DataView values)
    {
        StateManager stateManager = getStateManager();
        SourceManager sourceManager = getSourceManager();

        if (!sourceManager.isValid(source))
        {
            stateManager.setError(AL_INVALID_NAME);
            return;
        }

        ALSource alSource = sourceManager.getSource(source);

        switch (param)
        {
            case AL_SOURCE_RELATIVE:
                if (!checkSetError(!hasEnoughBytes(values, SIZEOF_INT), AL_INVALID_VALUE))
                    values.setInt32(0, alSource.sourceRelative);
                return;

            case AL_SOURCE_TYPE:
                if (!checkSetError(!hasEnoughBytes(values, SIZEOF_INT), AL_INVALID_VALUE))
                    values.setInt32(0, alSource.sourceType);
                return;

            case AL_LOOPING:
                if (!checkSetError(!hasEnoughBytes(values, SIZEOF_INT), AL_INVALID_VALUE))
                    values.setInt32(0, alSource.looping);
                return;

            case AL_BUFFER:
                if (!checkSetError(!hasEnoughBytes(values, SIZEOF_INT), AL_INVALID_VALUE))
                    values.setInt32(0, alSource.buffer);
                return;

            case AL_BUFFERS_QUEUED:
                if (!checkSetError(!hasEnoughBytes(values, SIZEOF_INT), AL_INVALID_VALUE))
                    values.setInt32(0, alSource.buffersQueued);
                return;

            case AL_BUFFERS_PROCESSED:
                if (!checkSetError(!hasEnoughBytes(values, SIZEOF_INT), AL_INVALID_VALUE))
                    values.setInt32(0, alSource.buffersProcessed);
                return;

            case AL_REFERENCE_DISTANCE:
                if (!checkSetError(!hasEnoughBytes(values, SIZEOF_INT), AL_INVALID_VALUE))
                    values.setInt32(0, (int) alSource.referralDistance);
                return;

            case AL_ROLLOFF_FACTOR:
                if (!checkSetError(!hasEnoughBytes(values, SIZEOF_INT), AL_INVALID_VALUE))
                    values.setInt32(0, (int) alSource.rolloffFactor);
                return;

            case AL_MAX_DISTANCE:
                if (!checkSetError(!hasEnoughBytes(values, SIZEOF_INT), AL_INVALID_VALUE))
                    values.setInt32(0, (int) alSource.maxDistance);
                return;

            case AL_DIRECTION:
                if (!checkSetError(!hasEnoughBytes(values, 3 * SIZEOF_INT), AL_INVALID_VALUE))
                {
                    values.setInt32(0, (int) alSource.dirX);
                    values.setInt32(SIZEOF_INT, (int) alSource.dirY);
                    values.setInt32(2 * SIZEOF_INT, (int) alSource.dirZ);
                }
                return;

            case AL_CONE_INNER_ANGLE:
                if (!checkSetError(!hasEnoughBytes(values, SIZEOF_INT), AL_INVALID_VALUE))
                    values.setInt32(0, (int) alSource.coneInnerAngle);
                return;

            case AL_CONE_OUTER_ANGLE:
                if (!checkSetError(!hasEnoughBytes(values, SIZEOF_INT), AL_INVALID_VALUE))
                    values.setInt32(0, (int) alSource.coneOuterAngle);
                return;

            case AL_POSITION:
                if (!checkSetError(!hasEnoughBytes(values, 3 * SIZEOF_INT), AL_INVALID_VALUE))
                {
                    values.setInt32(0, (int) alSource.posX);
                    values.setInt32(SIZEOF_INT, (int) alSource.posY);
                    values.setInt32(2 * SIZEOF_INT, (int) alSource.posZ);
                }
                return;

            case AL_VELOCITY:
                if (!checkSetError(!hasEnoughBytes(values, 3 * SIZEOF_INT), AL_INVALID_VALUE))
                {
                    values.setInt32(0, (int) alSource.velX);
                    values.setInt32(SIZEOF_INT, (int) alSource.velY);
                    values.setInt32(2 * SIZEOF_INT, (int) alSource.velZ);
                }
                return;
        }

        stateManager.setError(AL_INVALID_ENUM);
    }

    public static void alGetSourceiv(int source, int param, ArrayBuffer values)
    {
        alGetSourceiv(source, param, DataViewNative.create(values));
    }

    public static void alListener3f(int param, float v1, float v2, float v3)
    {
        StateManager stateManager = getStateManager();
        ALListener listener = stateManager.listener;

        switch (param)
        {
            case AL_POSITION:
                if (!areAllFinite(v1, v2, v3))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                listener.posX = v1;
                listener.posY = v2;
                listener.posZ = v3;
                listener.update();
                return;

            case AL_VELOCITY:
                if (!areAllFinite(v1, v2, v3))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                listener.velX = v1;
                listener.velY = v2;
                listener.velZ = v3;
                listener.update();
                return;
        }

        stateManager.setError(AL_INVALID_ENUM);
    }

    public static void alListenerf(int param, float v)
    {
        StateManager stateManager = getStateManager();
        ALListener listener = stateManager.listener;

        switch (param)
        {
            case AL_GAIN:
                if (v < 0f)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                listener.gain = v;
                listener.update();
                return;
        }

        stateManager.setError(AL_INVALID_ENUM);
    }

    public static void alListenerfv(int param, DataView values)
    {
        StateManager stateManager = getStateManager();
        ALListener listener = stateManager.listener;

        switch (param)
        {
            case AL_POSITION:
                if (!hasEnoughBytes(values, 3 * SIZEOF_FLOAT))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                if (!areAllFinite(values.getFloat32(0), values.getFloat32(SIZEOF_FLOAT), values.getFloat32(2 * SIZEOF_FLOAT)))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                listener.posX = values.getFloat32(0);
                listener.posY = values.getFloat32(SIZEOF_FLOAT);
                listener.posZ = values.getFloat32(2 * SIZEOF_FLOAT);
                listener.update();
                return;

            case AL_VELOCITY:
                if (!hasEnoughBytes(values, 3 * SIZEOF_FLOAT))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                if (!areAllFinite(values.getFloat32(0), values.getFloat32(SIZEOF_FLOAT), values.getFloat32(2 * SIZEOF_FLOAT)))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                listener.velX = values.getFloat32(0);
                listener.velY = values.getFloat32(SIZEOF_FLOAT);
                listener.velZ = values.getFloat32(2 * SIZEOF_FLOAT);
                listener.update();
                return;

            case AL_GAIN:
                if (!hasEnoughBytes(values, SIZEOF_FLOAT))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                if (values.getFloat32(0) < 0f)
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                listener.gain = values.getFloat32(0);
                listener.update();
                return;

            case AL_ORIENTATION:
                if (!hasEnoughBytes(values, 6 * SIZEOF_FLOAT))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                float v0 = values.getFloat32(0);
                float v1 = values.getFloat32(SIZEOF_FLOAT);
                float v2 = values.getFloat32(2 * SIZEOF_FLOAT);
                float v3 = values.getFloat32(3 * SIZEOF_FLOAT);
                float v4 = values.getFloat32(4 * SIZEOF_FLOAT);
                float v5 = values.getFloat32(5 * SIZEOF_FLOAT);
                if (!areAllFinite(v0, v1, v2, v3, v4, v5))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                listener.orientationAtX = v0;
                listener.orientationAtY = v1;
                listener.orientationAtZ = v2;
                listener.orientationUpX = v3;
                listener.orientationUpY = v4;
                listener.orientationUpZ = v5;
                listener.update();
                return;
        }

        stateManager.setError(AL_INVALID_ENUM);
    }

    public static void alListenerfv(int param, ArrayBuffer values)
    {
        alListenerfv(param, DataViewNative.create(values));
    }

    public static void alListener3i(int param, int v1, int v2, int v3)
    {
        StateManager stateManager = getStateManager();
        ALListener listener = stateManager.listener;

        switch (param)
        {
            case AL_POSITION:
                if (!areAllFinite(v1, v2, v3))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                listener.posX = v1;
                listener.posY = v2;
                listener.posZ = v3;
                listener.update();
                return;

            case AL_VELOCITY:
                if (!areAllFinite(v1, v2, v3))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                listener.velX = v1;
                listener.velY = v2;
                listener.velZ = v3;
                listener.update();
                return;
        }

        stateManager.setError(AL_INVALID_ENUM);
    }

    public static void alListeneri(int param, int v)
    {
        StateManager stateManager = getStateManager();
        ALListener listener = stateManager.listener;


        stateManager.setError(AL_INVALID_ENUM);
    }

    public static void alListeneriv(int param, DataView values)
    {
        StateManager stateManager = getStateManager();
        ALListener listener = stateManager.listener;

        switch (param)
        {
            case AL_POSITION:
                if (!hasEnoughBytes(values, 3 * SIZEOF_INT))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                if (!areAllFinite(values.getInt32(0), values.getInt32(SIZEOF_INT), values.getInt32(2 * SIZEOF_INT)))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                listener.posX = values.getInt32(0);
                listener.posY = values.getInt32(SIZEOF_INT);
                listener.posZ = values.getInt32(2 * SIZEOF_INT);
                listener.update();
                return;

            case AL_VELOCITY:
                if (!hasEnoughBytes(values, 3 * SIZEOF_INT))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                if (!areAllFinite(values.getInt32(0), values.getInt32(SIZEOF_INT), values.getInt32(2 * SIZEOF_INT)))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                listener.velX = values.getInt32(0);
                listener.velY = values.getInt32(SIZEOF_INT);
                listener.velZ = values.getInt32(2 * SIZEOF_INT);
                listener.update();
                return;

            case AL_ORIENTATION:
                if (!hasEnoughBytes(values, 6 * SIZEOF_INT))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                int v0 = values.getInt32(0);
                int v1 = values.getInt32(SIZEOF_INT);
                int v2 = values.getInt32(2 * SIZEOF_INT);
                int v3 = values.getInt32(3 * SIZEOF_INT);
                int v4 = values.getInt32(4 * SIZEOF_INT);
                int v5 = values.getInt32(5 * SIZEOF_INT);
                if (!areAllFinite(v0, v1, v2, v3, v4, v5))
                {
                    stateManager.setError(AL_INVALID_VALUE);
                    return;
                }
                listener.orientationAtX = v0;
                listener.orientationAtY = v1;
                listener.orientationAtZ = v2;
                listener.orientationUpX = v3;
                listener.orientationUpY = v4;
                listener.orientationUpZ = v5;
                listener.update();
                return;
        }

        stateManager.setError(AL_INVALID_ENUM);
    }

    public static void alListeneriv(int param, ArrayBuffer values)
    {
        alListeneriv(param, DataViewNative.create(values));
    }

    public static void alGetListener3f(int param, DataView v1, DataView v2, DataView v3)
    {
        StateManager stateManager = getStateManager();

        // Ensure buffer size for a float
        if (hasEnoughBytes(v1, SIZEOF_FLOAT) ||
            hasEnoughBytes(v2, SIZEOF_FLOAT) ||
            hasEnoughBytes(v3, SIZEOF_FLOAT))
        {
            stateManager.setError(AL_INVALID_VALUE);
            return;
        }

        ALListener alListener = stateManager.listener;

        switch (param)
        {
            case AL_POSITION:
                v1.setFloat32(0, alListener.posX);
                v2.setFloat32(0, alListener.posY);
                v3.setFloat32(0, alListener.posZ);
                return;

            case AL_VELOCITY:
                v1.setFloat32(0, alListener.velX);
                v2.setFloat32(0, alListener.velY);
                v3.setFloat32(0, alListener.velZ);
                return;
        }

        stateManager.setError(AL_INVALID_ENUM);
    }

    public static void alGetListener3f(int param, ArrayBuffer v1, ArrayBuffer v2, ArrayBuffer v3)
    {
        alGetListener3f(param, DataViewNative.create(v1), DataViewNative.create(v2), DataViewNative.create(v3));
    }

    public static float alGetListenerf(int param)
    {
        StateManager stateManager = getStateManager();
        ALListener alListener = stateManager.listener;

        switch (param)
        {
            case AL_GAIN:
                return alListener.gain;
        }

        stateManager.setError(AL_INVALID_ENUM);
        return 0;
    }

    public static void alGetListenerf(int param, DataView value)
    {
        if (!hasEnoughBytes(value, SIZEOF_FLOAT))
        {
            getStateManager().setError(AL_INVALID_VALUE);
            return;
        }

        value.setFloat32(0, alGetListenerf(param));
    }

    public static void alGetListenerf(int param, ArrayBuffer value)
    {
        alGetListenerf(param, DataViewNative.create(value));
    }

    public static void alGetListenerfv(int param, DataView values)
    {
        StateManager stateManager = getStateManager();
        ALListener listener = stateManager.listener;

        switch (param)
        {
            case AL_ORIENTATION:
                if (!checkSetError(hasEnoughBytes(values, 6 * SIZEOF_FLOAT), AL_INVALID_VALUE))
                {
                    values.setFloat32(0, listener.orientationAtX);
                    values.setFloat32(SIZEOF_FLOAT, listener.orientationAtY);
                    values.setFloat32(SIZEOF_FLOAT * 2, listener.orientationAtZ);
                    values.setFloat32(SIZEOF_FLOAT * 3, listener.orientationUpX);
                    values.setFloat32(SIZEOF_FLOAT * 4, listener.orientationUpY);
                    values.setFloat32(SIZEOF_FLOAT * 5, listener.orientationUpZ);
                }
                return;

            case AL_GAIN:
                if (!checkSetError(hasEnoughBytes(values, SIZEOF_FLOAT), AL_INVALID_VALUE))
                    values.setFloat32(0, listener.gain);
                return;

            case AL_VELOCITY:
                if (!checkSetError(hasEnoughBytes(values, 3 * SIZEOF_FLOAT), AL_INVALID_VALUE))
                {
                    values.setFloat32(0, listener.velX);
                    values.setFloat32(SIZEOF_FLOAT, listener.velY);
                    values.setFloat32(SIZEOF_FLOAT * 2, listener.velZ);
                }
                return;

            case AL_POSITION:
                if (!checkSetError(hasEnoughBytes(values, 3 * SIZEOF_FLOAT), AL_INVALID_VALUE))
                {
                    values.setFloat32(0, listener.posX);
                    values.setFloat32(SIZEOF_FLOAT, listener.posY);
                    values.setFloat32(SIZEOF_FLOAT * 2, listener.posZ);
                }
                return;
        }

        stateManager.setError(AL_INVALID_ENUM);
    }

    public static void alGetListenerfv(int param, ArrayBuffer values)
    {
        alGetListenerfv(param, DataViewNative.create(values));
    }

    public static void alGetListener3i(int param, DataView v1, DataView v2, DataView v3)
    {
        StateManager stateManager = getStateManager();

        // Ensure buffer size for an int
        if (hasEnoughBytes(v1, SIZEOF_INT) ||
            hasEnoughBytes(v2, SIZEOF_INT) ||
            hasEnoughBytes(v3, SIZEOF_INT))
        {
            stateManager.setError(AL_INVALID_VALUE);
            return;
        }

        ALListener alListener = stateManager.listener;

        switch (param)
        {
            case AL_POSITION:
                v1.setInt32(0, (int) alListener.posX);
                v2.setInt32(0, (int) alListener.posY);
                v3.setInt32(0, (int) alListener.posZ);
                return;

            case AL_VELOCITY:
                v1.setInt32(0, (int) alListener.velX);
                v2.setInt32(0, (int) alListener.velY);
                v3.setInt32(0, (int) alListener.velZ);
                return;
        }

        stateManager.setError(AL_INVALID_ENUM);
    }

    public static void alGetListener3i(int param, ArrayBuffer v1, ArrayBuffer v2, ArrayBuffer v3)
    {
        alGetListener3i(param, DataViewNative.create(v1), DataViewNative.create(v2), DataViewNative.create(v3));
    }

    public static int alGetListeneri(int param)
    {
        StateManager stateManager = getStateManager();
        ALListener alListener = stateManager.listener;

        switch (param)
        {
            case AL_GAIN:
                return (int) alListener.gain;
        }

        stateManager.setError(AL_INVALID_ENUM);
        return 0;
    }

    public static void alGetListeneri(int param, DataView value)
    {
        if (!hasEnoughBytes(value, SIZEOF_INT))
        {
            getStateManager().setError(AL_INVALID_VALUE);
            return;
        }

        value.setInt32(0, alGetListeneri(param));
    }

    public static void alGetListeneri(int param, ArrayBuffer value)
    {
        alGetListeneri(param, DataViewNative.create(value));
    }

    public static void alGetListeneriv(int param, DataView values)
    {
        StateManager stateManager = getStateManager();
        ALListener listener = stateManager.listener;

        switch (param)
        {
            case AL_ORIENTATION:
                if (!checkSetError(hasEnoughBytes(values, 6 * SIZEOF_INT), AL_INVALID_VALUE))
                {
                    values.setInt32(0, (int) listener.orientationAtX);
                    values.setInt32(SIZEOF_INT, (int) listener.orientationAtY);
                    values.setInt32(SIZEOF_INT * 2, (int) listener.orientationAtZ);
                    values.setInt32(SIZEOF_INT * 3, (int) listener.orientationUpX);
                    values.setInt32(SIZEOF_INT * 4, (int) listener.orientationUpY);
                    values.setInt32(SIZEOF_INT * 5, (int) listener.orientationUpZ);
                }
                return;

            case AL_VELOCITY:
                if (!checkSetError(hasEnoughBytes(values, 3 * SIZEOF_INT), AL_INVALID_VALUE))
                {
                    values.setInt32(0, (int) listener.velX);
                    values.setInt32(SIZEOF_INT, (int) listener.velY);
                    values.setInt32(SIZEOF_INT * 2, (int) listener.velZ);
                }
                return;

            case AL_POSITION:
                if (!checkSetError(hasEnoughBytes(values, 3 * SIZEOF_INT), AL_INVALID_VALUE))
                {
                    values.setInt32(0, (int) listener.posX);
                    values.setInt32(SIZEOF_INT, (int) listener.posY);
                    values.setInt32(SIZEOF_INT * 2, (int) listener.posZ);
                }
                return;
        }

        stateManager.setError(AL_INVALID_ENUM);
    }

    public static void alGetListeneriv(int param, ArrayBuffer values)
    {
        alGetListeneriv(param, DataViewNative.create(values));
    }

    public static int alGetBufferi(int buffer, int param)
    {
        StateManager stateManager = getStateManager();
        BufferManager bufferManager = getBufferManager();

        if (!bufferManager.isValid(buffer))
        {
            stateManager.setError(AL_INVALID_NAME);
            return 0;
        }

        ALBuffer alBuffer = bufferManager.getBuffer(buffer);

        switch (param)
        {
            case AL_FREQUENCY:
                return (int) alBuffer.audioBuffer.getSampleRate();

            case AL_SIZE:
                return alBuffer.audioBuffer.getLength();

            case AL_BITS:
                return 16;

            case AL_CHANNELS:
                return alBuffer.audioBuffer.getNumberOfChannels();
        }

        stateManager.setError(AL_INVALID_ENUM);
        return 0;
    }

    public static void alGetBufferi(int buffer, int param, DataView value)
    {
        if (!hasEnoughBytes(value, SIZEOF_INT))
        {
            getStateManager().setError(AL_INVALID_VALUE);
            return;
        }

        value.setInt32(0, alGetBufferi(buffer, param));
    }

    public static void alGetBufferi(int buffer, int param, ArrayBuffer value)
    {
        alGetBufferi(buffer, param, DataViewNative.create(value));
    }

    public static void alGetBufferiv(int buffer, int param, DataView values)
    {
        StateManager stateManager = getStateManager();
        BufferManager bufferManager = getBufferManager();

        if (!bufferManager.isValid(buffer))
        {
            stateManager.setError(AL_INVALID_NAME);
            return;
        }

        if (!hasEnoughBytes(values, SIZEOF_INT))
        {
            stateManager.setError(AL_INVALID_VALUE);
            return;
        }

        ALBuffer alBuffer = bufferManager.getBuffer(buffer);

        switch (param)
        {
            case AL_FREQUENCY:
                values.setInt32(0, (int) alBuffer.audioBuffer.getSampleRate());
                return;

            case AL_SIZE:
                values.setInt32(0, alBuffer.audioBuffer.getLength());
                return;

            case AL_BITS:
                values.setInt32(0, 16);
                return;

            case AL_CHANNELS:
                values.setInt32(0, alBuffer.audioBuffer.getNumberOfChannels());
                return;
        }

        stateManager.setError(AL_INVALID_ENUM);
    }

    public static void alGetBufferiv(int buffer, int param, ArrayBuffer values)
    {
        alGetBufferiv(buffer, param, DataViewNative.create(values));
    }
}
