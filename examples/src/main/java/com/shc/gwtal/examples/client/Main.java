package com.shc.gwtal.examples.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.xhr.client.XMLHttpRequest;
import com.shc.gwtal.client.webaudio.AudioBuffer;
import com.shc.gwtal.client.webaudio.nodes.AudioBufferSourceNode;
import com.shc.gwtal.client.webaudio.AudioContext;
import com.shc.gwtal.client.webaudio.AudioContextException;

public class Main implements EntryPoint
{
    private AudioBuffer           buffer;
    private AudioBufferSourceNode source;

    @Override
    public void onModuleLoad()
    {
        try
        {
            // Create an audio context
            AudioContext context = AudioContext.create();

            context.setOnStateChange(() ->
            {
                GWT.log(context.getState().getJsState());
            });

            FlowPanel panel = new FlowPanel();

            // Create the play button
            Button playButton = new Button("Play");
            playButton.addClickHandler((e) ->
            {
                // Stop if already playing
                if (source != null)
                {
                    source.stop();
                    source = null;
                }

                // Create the source node
                source = context.createBufferSource();
                source.setBuffer(buffer);

                // Connect to the destination and play
                source.connect(context.getDestination());
                source.start();
            });
            playButton.setEnabled(false);

            // Create the stop button
            Button stopButton = new Button("Stop");
            stopButton.addClickHandler((e) ->
            {
                if (source != null)
                {
                    source.stop();
                    source = null;
                }
            });

            // Add the buttons to the panel
            panel.add(playButton);
            panel.add(stopButton);

            // Add the panel to the document
            RootPanel.get().add(panel);

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
                    // Start decoding the data into an AudioBuffer
                    context.decodeAudioData
                            (
                                    // The response that we got from the request
                                    request.getResponseArrayBuffer(),

                                    // Upon success, we are returned with an AudioBuffer
                                    (buffer) ->
                                    {
                                        // Save the buffer
                                        this.buffer = buffer;
                                        playButton.setEnabled(true);
                                    }
                            );
                }
            });

            // Send the request so browser starts loading the file
            request.send();
        }
        catch (AudioContextException e)
        {
            // In case of any exception, log to the console
            GWT.log(e.toString());
        }
    }
}
