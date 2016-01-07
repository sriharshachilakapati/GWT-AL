package com.shc.gwtal.examples.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.shc.gwtal.client.AudioContext;
import com.shc.gwtal.client.AudioContextException;

public class Main implements EntryPoint
{
    @Override
    public void onModuleLoad()
    {
        RootPanel.get().add(new HTML("<h1>Hello world</h1>"));

        try
        {
            AudioContext context = AudioContext.create();

            RootPanel.get().add(new HTML("<b>Sample Rate:</b> " + context.getSampleRate()));
        }
        catch (AudioContextException e)
        {
            GWT.log(e.toString());
        }
    }
}
