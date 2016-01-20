package com.shc.gwtal.client.webaudio;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.JsArrayNumber;
import com.google.gwt.dom.client.MediaElement;
import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.typedarrays.shared.Float32Array;
import com.google.gwt.typedarrays.shared.Float64Array;
import com.google.gwt.xml.client.DOMException;
import com.shc.gwtal.client.webaudio.enums.AudioContextState;
import com.shc.gwtal.client.webaudio.enums.AudioContextPlaybackCategory;
import com.shc.gwtal.client.webaudio.nodes.*;

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
        return create(new Options());
    }

    public static AudioContext create(Options options) throws AudioContextException
    {
        AudioContext context = nCreate(options.toDictionary());

        if (context == null)
            throw new AudioContextException("Error creating a context");

        return context;
    }

    private static native AudioContext nCreate(DictionaryJSO options) /*-{
        var contextClass = (
            $wnd.AudioContext
            || $wnd.webkitAudioContext
            || $wnd.mozAudioContext
            || $wnd.oAudioContext
            || $wnd.msAudioContext
        );

        if (contextClass)
            return new contextClass(options);
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
                audioEventHandler.@com.shc.gwtal.client.webaudio.AudioEventHandler::invoke()();
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
                    successCallback.@com.shc.gwtal.client.webaudio.AudioContext.DecodeSuccessCallback::invoke(*)(decodedData);
            },

            function (domexception)
            {
                if (errorCallback != null)
                    errorCallback.@com.shc.gwtal.client.webaudio.AudioContext.DecodeErrorCallback::invoke(*)(domexception);
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

    public native SpatialPannerNode createSpatialPanner() /*-{
        return this.createSpatialPanner();
    }-*/;

    public native StereoPannerNode createStereoPanner() /*-{
        return this.createStereoPanner();
    }-*/;

    public native ConvolverNode createConvolver() /*-{
        return this.createConvolver();
    }-*/;

    public ChannelSplitterNode createChannelSplitter()
    {
        return createChannelSplitter(6);
    }

    public native ChannelSplitterNode createChannelSplitter(int numberOfOutputs) /*-{
        return this.createChannelSplitter(numberOfOutputs);
    }-*/;

    public ChannelMergerNode createChannelMerger()
    {
        return createChannelMerger(6);
    }

    public native ChannelMergerNode createChannelMerger(int numberOfInputs) /*-{
        return this.createChannelMerger(numberOfInputs);
    }-*/;

    public native DynamicsCompressorNode createDynamicsCompressor() /*-{
        return this.createDynamicsCompressor();
    }-*/;

    public PeriodicWave createPeriodicWave(Float32Array real, Float32Array imag)
    {
        return createPeriodicWave(real, imag, new PeriodicWave.Constraints());
    }

    public PeriodicWave createPeriodicWave(Float32Array real, Float32Array imag, PeriodicWave.Constraints constraints)
    {
        return createPeriodicWave(real, imag, constraints.toDictionary());
    }

    private native PeriodicWave createPeriodicWave(Float32Array real, Float32Array imag, DictionaryJSO constraints) /*-{
        return this.createPeriodicWave(real, imag, constraints);
    }-*/;

    public native MediaElementAudioSourceNode createMediaElementSource(MediaElement mediaElement) /*-{
        return this.createMediaElementSource(mediaElement);
    }-*/;

    public native MediaStreamAudioSourceNode createMediaStreamSource(JavaScriptObject mediaStream) /*-{
        return this.createMediaStreamSource(mediaStream);
    }-*/;

    public native MediaStreamAudioDestinationNode createMediaStreamDestination() /*-{
        return this.createMediaStreamDestination();
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

        private DictionaryJSO dictionary;

        DictionaryJSO toDictionary()
        {
            if (dictionary == null)
                dictionary = DictionaryJSO.create();

            dictionary.define("playbackCategory", playbackCategory.getJsState());

            return dictionary;
        }
    }
}
