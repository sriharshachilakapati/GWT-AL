package com.shc.gwtal.examples.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.xhr.client.XMLHttpRequest;
import com.shc.gwtal.client.AudioBufferSourceNode;
import com.shc.gwtal.client.AudioContext;
import com.shc.gwtal.client.AudioContextException;

public class Main implements EntryPoint
{
    @Override
    public void onModuleLoad()
    {
        try
        {
            // Create an audio context
            AudioContext context = AudioContext.create();

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
                                        // Create the source, set the buffer, connect to the destination and play
                                        AudioBufferSourceNode source = context.createBufferSource();
                                        source.setBuffer(buffer);

                                        source.connect(context.getDestination());
                                        source.start(0);
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
