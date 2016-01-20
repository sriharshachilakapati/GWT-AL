package com.shc.gwtal.client.webaudio.enums;

/**
 * @author Sri Harsha Chilakapati
 */
public enum DistanceModelType
{
    LINEAR("linear"),
    INVERSE("inverse"),
    EXPONENTIAL("exponential");

    private String jsType;

    DistanceModelType(String jsType)
    {
        this.jsType = jsType;
    }

    public static DistanceModelType forJsType(String jsType)
    {
        for (DistanceModelType type : values())
            if (type.getJsType().equalsIgnoreCase(jsType))
                return type;

        return LINEAR;
    }

    public String getJsType()
    {
        return jsType;
    }
}
