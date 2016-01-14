package com.shc.gwtal.client.enums;

/**
 * @author Sri Harsha Chilakapati
 */
public enum PanningModelType
{
    EQUALPOWER("equalpower"),
    HRTF("HRTF");

    private String jsType;

    PanningModelType(String jsType)
    {
        this.jsType = jsType;
    }

    public static PanningModelType forJsType(String jsType)
    {
        for (PanningModelType type : values())
            if (type.getJsType().equalsIgnoreCase(jsType))
                return type;

        return EQUALPOWER;
    }

    public String getJsType()
    {
        return jsType;
    }
}
