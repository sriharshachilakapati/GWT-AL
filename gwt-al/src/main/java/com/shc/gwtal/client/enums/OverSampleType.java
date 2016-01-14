package com.shc.gwtal.client.enums;

/**
 * @author Sri Harsha Chilakapati
 */
public enum OverSampleType
{
    NONE("none"),
    TWO_X("2x"),
    FOUR_X("4x");

    private String jsType;

    OverSampleType(String jsType)
    {
        this.jsType = jsType;
    }

    public String getJsType()
    {
        return jsType;
    }

    public static OverSampleType forJsType(String jsType)
    {
        for (OverSampleType type : values())
            if (type.getJsType().equalsIgnoreCase(jsType))
                return type;

        return NONE;
    }
}
