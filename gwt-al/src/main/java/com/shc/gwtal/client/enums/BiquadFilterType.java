package com.shc.gwtal.client.enums;

/**
 * @author Sri Harsha Chilakapati
 */
public enum BiquadFilterType
{
    LOWPASS("lowpass"),
    HIGHPASS("highpass"),
    BANDPASS("bandpass"),
    LOWSHELF("lowshelf"),
    HIGHSHELF("highshelf"),
    PEAKING("peaking"),
    NOTCH("notch"),
    ALLPASS("allpass");

    private String jsType;

    BiquadFilterType(String jsType)
    {
        this.jsType = jsType;
    }

    public static BiquadFilterType forJsType(String jsType)
    {
        for (BiquadFilterType type : values())
            if (type.getJsType().equalsIgnoreCase(jsType))
                return type;

        return ALLPASS;
    }

    public String getJsType()
    {
        return this.jsType;
    }
}
