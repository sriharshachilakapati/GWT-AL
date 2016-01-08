package com.shc.gwtal.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Sri Harsha Chilakapati
 */
public class Promise<T> extends JavaScriptObject
{
    protected Promise()
    {
    }

    public final native Promise<T> onError(OnRejected onRejected) /*-{
        return this['catch'](function (reason)
        {
            if (reason === undefined)
                onRejected.@com.shc.gwtal.client.Promise.OnRejected::invoke(*)(null);
            else
                onRejected.@com.shc.gwtal.client.Promise.OnRejected::invoke(*)(reason);
        });
    }-*/;

    public final Promise<T> then(OnFulfilled<T> onFulfilled)
    {
        return then(onFulfilled, new OnRejected()
        {
            @Override
            public void invoke(Object reason)
            {
                GWT.log(reason.toString());
            }
        });
    }

    public final native Promise<T> then(OnFulfilled<T> onFulfilled, OnRejected onRejected) /*-{
        return this.then(
            function (value)
            {
                if (value === undefined)
                    onFulfilled.@com.shc.gwtal.client.Promise.OnFulfilled::invoke(*)(null);
                else
                    onFulfilled.@com.shc.gwtal.client.Promise.OnFulfilled::invoke(*)(value);
            },

            function (reason)
            {
                onRejected.@com.shc.gwtal.client.Promise.OnRejected::invoke(*)(reason);
            }
        )
    }-*/;

    public interface OnFulfilled<T>
    {
        void invoke(T value);
    }

    public interface OnRejected
    {
        void invoke(Object reason);
    }
}
