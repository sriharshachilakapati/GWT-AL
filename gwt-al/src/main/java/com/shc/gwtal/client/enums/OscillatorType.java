package com.shc.gwtal.client.enums;

/**
 * @author Sri Harsha Chilakapati
 */
public enum OscillatorType
{
    SINE("sine"),
    SQUARE("square"),
    SAWTOOTH("sawtooth"),
    TRIANGLE("triangle"),
    CUSTOM("custom");

    private String jsType;

    OscillatorType(String jsType)
    {
        this.jsType = jsType;
    }

    public String getJsType()
    {
        return this.jsType;
    }

    public static OscillatorType forJsType(String jsType)
    {
        for (OscillatorType type : values())
            if (type.getJsType().equalsIgnoreCase(jsType))
                return type;

        return CUSTOM;
    }
}
