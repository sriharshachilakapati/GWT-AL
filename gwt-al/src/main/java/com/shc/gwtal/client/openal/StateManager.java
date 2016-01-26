package com.shc.gwtal.client.openal;

import com.shc.gwtal.client.webaudio.AudioContext;
import com.shc.gwtal.client.webaudio.nodes.AudioNode;
import com.shc.gwtal.client.webaudio.nodes.GainNode;
import com.shc.gwtal.client.webaudio.nodes.PannerNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sri Harsha Chilakapati
 */
final class StateManager
{
    private static final Map<ALContext, StateManager> stateManagers = new HashMap<>();

    public AudioNode  inputNode;
    public GainNode   gainNode;
    public PannerNode pannerNode;

    public boolean gainEnabled;
    public boolean pannerEnabled;

    private AudioContext    context;
    private List<AudioNode> pipeline;

    private StateManager(AudioContext context)
    {
        this.context = context;
        this.pipeline = new ArrayList<>();

        // Input node is a gain node with gain fixed as 1.0
        // used to give input to the pipeline.
        inputNode = context.createGain();

        // Create other nodes in this states too
        gainNode = context.createGain();
        pannerNode = context.createPanner();

        updatePipeline();
    }

    public static StateManager forContext(ALContext context)
    {
        return stateManagers.get(context);
    }

    public static StateManager create(ALContext context)
    {
        StateManager sm = new StateManager(context.getWebAudioContext());
        stateManagers.put(context, sm);
        return sm;
    }

    public static void destroy(ALContext context)
    {
        stateManagers.remove(context);
    }

    public void updatePipeline()
    {
        pipeline.clear();

        if (pannerEnabled) pipeline.add(pannerNode);
        if (gainEnabled) pipeline.add(gainNode);

        AudioNode node = inputNode;

        for (AudioNode pipelineNode : pipeline)
        {
            // Disconnect from existing next nodes
            node.disconnect();

            // Connect to the next node in pipeline
            node.connect(pipelineNode);
            node = pipelineNode;
        }

        // Connect last node to destination
        node.disconnect();
        node.connect(context.getDestination());
    }
}
