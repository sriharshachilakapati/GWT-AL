package com.shc.gwtal.examples.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;

/**
 * @author Sri Harsha Chilakapati
 */
public interface Resources extends ClientBundle
{
    Resources INSTANCE = GWT.create(Resources.class);

    @Source("music.ogg")
    DataResource music();
}
