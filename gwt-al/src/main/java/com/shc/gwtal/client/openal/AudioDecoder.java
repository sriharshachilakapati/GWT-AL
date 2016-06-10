package com.shc.gwtal.client.openal;

import com.google.gwt.dom.client.AudioElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.MediaElement;
import com.google.gwt.typedarrays.shared.ArrayBuffer;
import com.google.gwt.xml.client.DOMException;
import com.shc.gwtal.client.webaudio.AudioBuffer;
import com.shc.gwtal.client.webaudio.AudioContext;

import static com.shc.gwtal.client.openal.ALUtils.*;

/**
 * <p>A class useful for decoding the audio data from different file formats supported by the browser. Most common
 * supported formats are WAV, OGG, WebM, but to check the support at runtime, you can use the following method.</p>
 *
 * <pre>
 *     if (AudioDecoder.isSupported(AudioDecoder.FileFormat.MP3))
 *         // MP3 is supported by the browser
 * </pre>
 *
 * <p>The work done by this class is done asynchronously and the result is returned through a callback. It returns an
 * integer which is the handle for an OpenAL buffer. To decode successfully, it needs a current context.</p>
 *
 * @author Sri Harsha Chilakapati
 */
public final class AudioDecoder
{
    private static AudioElement tempAudioElement;

    private AudioDecoder()
    {
    }

    public static boolean isSupported(FileFormat format)
    {
        if (tempAudioElement == null)
            tempAudioElement = Document.get().createAudioElement();

        switch (format)
        {
            case MP3:
                return !tempAudioElement.canPlayType(AudioElement.TYPE_MP3).equals(MediaElement.CANNOT_PLAY);

            case WAV:
                return !tempAudioElement.canPlayType(AudioElement.TYPE_WAV).equals(MediaElement.CANNOT_PLAY);

            case OGG:
                return !tempAudioElement.canPlayType(AudioElement.TYPE_OGG).equals(MediaElement.CANNOT_PLAY);

            case WEBM:
                return !tempAudioElement.canPlayType("audio/webm").equals(MediaElement.CANNOT_PLAY);
        }

        return false;
    }

    public static void decodeAudio(ArrayBuffer data, final OnDecoded onDecoded, final OnError onError)
    {
        if (AL.getCurrentContext() == null)
            throw new IllegalStateException("Error, there must be an active OpenAL context to decode the audio.");

        final AudioContext ctx = AL.getCurrentContext().getWebAudioContext();

        ctx.decodeAudioData(
                data,

                new AudioContext.DecodeSuccessCallback()
                {
                    @Override
                    public void invoke(AudioBuffer decodedData)
                    {
                        BufferManager bufferManager = getBufferManager();

                        int bufferID = bufferManager.createBuffer();
                        bufferManager.getBuffer(bufferID).audioBuffer = decodedData;

                        onDecoded.invoke(bufferID);
                    }
                },

                new AudioContext.DecodeErrorCallback()
                {
                    @Override
                    public void invoke(DOMException error)
                    {
                        onError.invoke(error == null ? "Unknown error" : "" + error.getCode() + ": " + error.getMessage());
                    }
                }
        );
    }

    public enum FileFormat
    {
        MP3, WAV, OGG, WEBM
    }

    public interface OnDecoded
    {
        void invoke(int alBufferID);
    }

    public interface OnError
    {
        void invoke(String reason);
    }
}
