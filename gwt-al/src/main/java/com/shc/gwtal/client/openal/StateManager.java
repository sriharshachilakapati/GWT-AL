package com.shc.gwtal.client.openal;

import com.shc.gwtal.client.webaudio.AudioContext;
import com.shc.gwtal.client.webaudio.nodes.AudioNode;
import com.shc.gwtal.client.webaudio.nodes.GainNode;
import com.shc.gwtal.client.webaudio.nodes.PannerNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.shc.gwtal.client.openal.AL10.*;

/**
 * @author Sri Harsha Chilakapati
 */
final class StateManager
{
    private static final Map<ALContext, StateManager> stateManagers = new HashMap<>();

    final BufferManager bufferManager = new BufferManager();
    final SourceManager sourceManager = new SourceManager();

    final ALListener listener;

    AudioNode  inputNode;
    PannerNode pannerNode;

    boolean pannerEnabled;

    int distanceModel;

    float speedOfSound = 343.3f;
    float dopplerFactor = 1.0f;

    AudioContext context;

    private int error;

    private List<AudioNode> pipeline;

    private StateManager(AudioContext context)
    {
        this.error = AL_NO_ERROR;
        this.distanceModel = AL_INVERSE_DISTANCE_CLAMPED;

        this.context = context;
        this.pipeline = new ArrayList<>();

        // Input node is a gain node with gain fixed as 1.0
        // used to give input to the pipeline.
        inputNode = context.createGain();
        ((GainNode) inputNode).getGain().setValue(1.0f);

        // Create other nodes in this states too
        pannerNode = context.createPanner();

        // Create the listener
        this.listener = new ALListener(context, this);

        updatePipeline();
    }

    static StateManager forContext(ALContext context)
    {
        return stateManagers.get(context);
    }

    static StateManager create(ALContext context)
    {
        StateManager sm = new StateManager(context.getWebAudioContext());
        stateManagers.put(context, sm);
        return sm;
    }

    static void destroy(ALContext context)
    {
        stateManagers.remove(context);
    }

    void updatePipeline()
    {
        pipeline.clear();

        if (pannerEnabled) pipeline.add(pannerNode);

        AudioNode node = inputNode;

        for (AudioNode pipelineNode : pipeline)
        {
            // Disconnect from existing next nodes
            node.disconnect();

            // Connect to the next node in pipeline
            node.connect(pipelineNode);
            node = pipelineNode;
        }

        // Connect last node to listener
        node.disconnect();
        node.connect(listener.inputNode);

        // Connect listener to destination
        listener.outputNode.connect(context.getDestination());
        listener.update();
    }

    int getError()
    {
        int error = this.error;
        this.error = AL_NO_ERROR;
        return error;
    }

    void setError(int error)
    {
        // The error should only be changed when the current recorded error is none. According to
        // the section 2.7 of the OpenAL 1.1 Specification:
        //
        //     "When an error is detected by AL, a flag is set and the error code is recorded. Further
        // errors, if they occur, do not affect this recorded code."
        //
        // As mentioned in the specification, we only set the error if there is no error recorded.

        if (this.error == AL_NO_ERROR)
            this.error = error;
    }
}
