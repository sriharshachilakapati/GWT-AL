package com.shc.gwtal.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author Sri Harsha Chilakapati
 */
public class PeriodicWave extends JavaScriptObject
{
    protected PeriodicWave()
    {
    }

    public static class Constraints
    {
        private DictionaryJSO dictionary;

        public boolean disableNormalization = false;

        DictionaryJSO toDictionary()
        {
            if (dictionary == null)
                dictionary = DictionaryJSO.create();

            dictionary.define("disableNormalization", disableNormalization);

            return dictionary;
        }
    }
}
