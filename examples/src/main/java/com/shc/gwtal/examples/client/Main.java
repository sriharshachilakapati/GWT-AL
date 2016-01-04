package com.shc.gwtal.examples.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;


public class Main implements EntryPoint
{
    @Override
    public void onModuleLoad()
    {
        RootPanel.get().add(new HTML("<h1>Hello world</h1>"));
    }
}
