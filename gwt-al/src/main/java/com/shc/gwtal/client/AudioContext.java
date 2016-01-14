package com.shc.gwtal.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.typedarrays.shared.Float64Array;
import com.google.gwt.xml.client.DOMException;
import com.shc.gwtal.client.enums.AudioContextState;
import com.shc.gwtal.client.enums.AudioContextPlaybackCategory;
import com.shc.gwtal.client.nodes.AnalyserNode;
import com.shc.gwtal.client.nodes.AudioBufferSourceNode;
import com.shc.gwtal.client.nodes.AudioDestinationNode;
import com.shc.gwtal.client.nodes.BiquadFilterNode;
import com.shc.gwtal.client.nodes.DelayNode;
import com.shc.gwtal.client.nodes.GainNode;
import com.shc.gwtal.client.nodes.IIRFilterNode;
import com.shc.gwtal.client.nodes.PannerNode;
import com.shc.gwtal.client.nodes.ScriptProcessorNode;
import com.shc.gwtal.client.nodes.WaveShaperNode;

/**
 * @author Sri Harsha Chilakapati
 */
public final class AudioContext extends JavaScriptObject
{
    protected AudioContext()
    {
    }

    public static AudioContext create() throws AudioContextException
    {
        AudioContext context = nCreate();

        if (context == null)
            throw new AudioContextException("Error creating a context");

        return context;
    }

    private static native AudioContext nCreate() /*-{
        var contextClass = (
            $wnd.AudioContext
            || $wnd.webkitAudioContext
            || $wnd.mozAudioContext
            || $wnd.oAudioContext
            || $wnd.msAudioContext
        );

        if (contextClass)
            return new contextClass();
        else
            return null;
    }-*/;

    public native float getSampleRate() /*-{
        return this.sampleRate;
    }-*/;

    public native double getCurrentTime() /*-{
        return this.currentTime;
    }-*/;

    public native Promise<Void> suspend() /*-{
        return this.suspend();
    }-*/;

    public native Promise<Void> resume() /*-{
        return this.resume();
    }-*/;

    public native Promise<Void> close() /*-{
        return this.close();
    }-*/;

    public native void setOnStateChange(AudioEventHandler audioEventHandler) /*-{
        this.onstatechange = function ()
        {
            if (audioEventHandler)
                audioEventHandler.@com.shc.gwtal.client.AudioEventHandler::invoke()();
        };
    }-*/;

    public native AudioBuffer createBuffer(int numberOfChannels, int length, float sampleRate) /*-{
        return this.createBuffer(numberOfChannels, length, sampleRate);
    }-*/;

    public Promise<AudioBuffer> decodeAudioData(ArrayBuffer audioData, DecodeSuccessCallback successCallback)
    {
        return decodeAudioData(audioData, successCallback,
                new DecodeErrorCallback()
                {
                    @Override
                    public void invoke(DOMException error)
                    {
                        GWT.log(error.toString());
                    }
                }
        );
    }

    public Promise<AudioBuffer> decodeAudioData(ArrayBuffer audioData)
    {
        return decodeAudioData(audioData, new DecodeSuccessCallback()
        {
            @Override
            public void invoke(AudioBuffer decodedData)
            {
            }
        });
    }

    public native Promise<AudioBuffer> decodeAudioData(ArrayBuffer audioData, DecodeSuccessCallback successCallback,
                                                       DecodeErrorCallback errorCallback) /*-{
        return this.decodeAudioData(
            audioData,

            function (decodedData)
            {
                if (successCallback != null)
                    successCallback.@com.shc.gwtal.client.AudioContext.DecodeSuccessCallback::invoke(*)(decodedData);
            },

            function (domexception)
            {
                if (errorCallback != null)
                    errorCallback.@com.shc.gwtal.client.AudioContext.DecodeErrorCallback::invoke(*)(domexception);
            }
        );
    }-*/;

    public native AudioBufferSourceNode createBufferSource() /*-{
        return this.createBufferSource();
    }-*/;

    public native AudioDestinationNode getDestination() /*-{
        return this.destination;
    }-*/;

    @Deprecated
    public native AudioListener getAudioListener() /*-{
        return this.listener;
    }-*/;

    private native String nGetState() /*-{
        return this.state;
    }-*/;

    public AudioContextState getState()
    {
        String jsState = nGetState();

        for (AudioContextState state : AudioContextState.values())
            if (state.getJsState().equalsIgnoreCase(jsState))
                return state;

        // Unknown state, might be suspended
        return AudioContextState.SUSPENDED;
    }

    public native Promise<AudioWorker> createAudioWorker(String scriptURL) /*-{
        return this.createAudioWorker(scriptURL);
    }-*/;

    @Deprecated
    public ScriptProcessorNode createScriptProcessor()
    {
        return createScriptProcessor(0, 0, 0);
    }

    @Deprecated
    public ScriptProcessorNode createScriptProcessor(int bufferSize)
    {
        return createScriptProcessor(bufferSize, 0, 0);
    }

    @Deprecated
    public ScriptProcessorNode createScriptProcessor(int bufferSize, int numberOfInputChannels)
    {
        return createScriptProcessor(bufferSize, numberOfInputChannels, 0);
    }

    @Deprecated
    public native ScriptProcessorNode createScriptProcessor(int bufferSize, int numberOfInputChannels,
                                                            int numberOfOutputChannels) /*-{
        return this.createScriptProcessor(bufferSize, numberOfInputChannels, numberOfOutputChannels);
    }-*/;

    public native AnalyserNode createAnalyser() /*-{
        return this.createAnalyser();
    }-*/;

    public native GainNode createGain() /*-{
        return this.createGain();
    }-*/;

    public DelayNode createDelay()
    {
        return createDelay(1.0);
    }

    public native DelayNode createDelay(double maxDelayTime) /*-{
        return this.createDelay(maxDelayTime);
    }-*/;

    public native BiquadFilterNode createBiquadFilter() /*-{
        return this.createBiquadFilter();
    }-*/;

    public native IIRFilterNode createIIRFilter(JsArrayNumber feedforward, JsArrayNumber feedback) /*-{
        return this.createIIRFilter(feedforward, feedback);
    }-*/;

    public native IIRFilterNode createIIRFilter(JsArrayInteger feedforward, JsArrayInteger feedback) /*-{
        return this.createIIRFilter(feedforward, feedback);
    }-*/;

    public native IIRFilterNode createIIRFilter(Float64Array feedforward, Float64Array feedback) /*-{
        return this.createIIRFilter(feedforward, feedback);
    }-*/;

    public native WaveShaperNode createWaveShaper() /*-{
        return this.createWaveShaper();
    }-*/;

    public native PannerNode createPanner() /*-{
        return this.createPanner();
    }-*/;

    public interface DecodeSuccessCallback
    {
        void invoke(AudioBuffer decodedData);
    }

    public interface DecodeErrorCallback
    {
        void invoke(DOMException error);
    }

    public static class Options
    {
        public AudioContextPlaybackCategory playbackCategory = AudioContextPlaybackCategory.INTERACTIVE;
    }
}
