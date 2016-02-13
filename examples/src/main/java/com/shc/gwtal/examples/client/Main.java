package com.shc.gwtal.examples.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.xhr.client.XMLHttpRequest;
import com.shc.gwtal.client.openal.AL;
import com.shc.gwtal.client.openal.AL10;
import com.shc.gwtal.client.openal.ALContext;
import com.shc.gwtal.client.openal.AudioDecoder;
import com.shc.gwtal.client.webaudio.AudioBuffer;
import com.shc.gwtal.client.webaudio.AudioContext;
import com.shc.gwtal.client.webaudio.AudioContextException;
import com.shc.gwtal.client.webaudio.nodes.AudioBufferSourceNode;

public class Main implements EntryPoint
{
    private AudioBuffer           webBuffer;
    private AudioBufferSourceNode sourceNode;

    @Override
    public void onModuleLoad()
    {
        // Test media support in browser
        GWT.log("MP3: " + AudioDecoder.isSupported(AudioDecoder.FileFormat.MP3));
        GWT.log("WAV: " + AudioDecoder.isSupported(AudioDecoder.FileFormat.WAV));
        GWT.log("OGG: " + AudioDecoder.isSupported(AudioDecoder.FileFormat.OGG));
        GWT.log("WEBM: " + AudioDecoder.isSupported(AudioDecoder.FileFormat.WEBM));

        String url = "https://upload.wikimedia.org/wikipedia/commons/1/1d/Demo_chorus.ogg";

        // Create an XMLHttpRequest to load the file from URL
        XMLHttpRequest request = XMLHttpRequest.create();
        request.setResponseType(XMLHttpRequest.ResponseType.ArrayBuffer);

        request.open("GET", url);

        // Attach a state change listener to check for the loading of the file
        request.setOnReadyStateChange((r) ->
        {
            if (request.getReadyState() == XMLHttpRequest.DONE)
            {
                try
                {
                    createWebAudioAPIExample(request.getResponseArrayBuffer());
                    createOpenALExample(request.getResponseArrayBuffer());
                }
                catch (AudioContextException e)
                {
                    e.printStackTrace();
                }
            }
        });

        // Send the request so browser starts loading the file
        request.send();
    }

    private void createOpenALExample(ArrayBuffer data) throws AudioContextException
    {
        ALContext context = ALContext.create();
        AL.setCurrentContext(context);

        int alSource = AL10.alGenSources();

        FlowPanel panel = new FlowPanel();
        panel.add(new Label("OpenAL example: "));

        Button playButton = new Button("Play");
        playButton.addClickHandler(event -> AL10.alSourcePlay(alSource));
        playButton.setEnabled(false);

        Button stopButton = new Button("Stop");
        stopButton.addClickHandler(event -> AL10.alSourceStop(alSource));

        Button pauseButton = new Button("Pause");
        pauseButton.addClickHandler(event -> AL10.alSourcePause(alSource));

        panel.add(playButton);
        panel.add(stopButton);
        panel.add(pauseButton);

        AudioDecoder.decodeAudio
                (
                        data,

                        alBufferID ->
                        {
                            AL10.alSourcei(alSource, AL10.AL_BUFFER, alBufferID);
                            playButton.setEnabled(true);
                        },

                        reason -> GWT.log("" + reason)
                );

        RootPanel.get().add(panel);
    }

    private void createWebAudioAPIExample(ArrayBuffer data) throws AudioContextException
    {
        // Create an audio context
        AudioContext context = AudioContext.create();

        context.setOnStateChange(() ->
        {
            GWT.log(context.getState().getJsState());
        });

        FlowPanel panel = new FlowPanel();

        panel.add(new Label("WebAudioAPI example: "));

        // Create the play button
        Button playButton = new Button("Play");
        playButton.addClickHandler((e) ->
        {
            // Stop if already playing
            if (sourceNode != null)
            {
                sourceNode.stop();
                sourceNode = null;
            }

            // Create the source node
            sourceNode = context.createBufferSource();
            sourceNode.setBuffer(webBuffer);

            // Connect to the destination and play
            sourceNode.connect(context.getDestination());
            sourceNode.start();
        });
        playButton.setEnabled(false);

        // Create the stop button
        Button stopButton = new Button("Stop");
        stopButton.addClickHandler((e) ->
        {
            if (sourceNode != null)
            {
                sourceNode.stop();
                sourceNode = null;
            }
        });

        // Add the buttons to the panel
        panel.add(playButton);
        panel.add(stopButton);

        // Add the panel to the document
        RootPanel.get().add(panel);

        // Decode using the context
        context.decodeAudioData(data, decodedData -> {
            webBuffer = decodedData;
            playButton.setEnabled(true);
        });
    }
}
