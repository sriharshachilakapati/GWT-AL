package com.shc.gwtal.client.openal;

import com.google.gwt.dom.client.AudioElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.MediaElement;
import com.google.gwt.typedarrays.shared.ArrayBuffer;

/**
 * <p>A class useful for decoding the audio data from different file formats supported by the browser. Most common
 * supported formats are WAV, OGG, WebM, but to check the support at runtime, you can use the following method.</p>
 *
 * <pre>
 *     if (AudioDecoder.isSupported(AudioDecoder.FileFormat.MP3))
 *         // MP3 is supported by the browser
 * </pre>
 *
 * <p>The work done by this class is done asynchronously. Note that there must be an active OpenAL context for this
 * class to work. Otherwise it will crash with possibly a {@link IllegalStateException}.</p>
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

    public enum FileFormat
    {
        MP3, WAV, OGG, WEBM
    }

    public interface OnDecoded
    {
        void invoke(ArrayBuffer arrayBuffer, int alFormat, int frequency);
    }

    public interface OnError
    {
        void invoke(String reason);
    }
}
