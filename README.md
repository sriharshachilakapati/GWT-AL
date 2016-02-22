# GWT-AL

![GWT-AL Logo](logo.png)

An OpenAL implementation based on WebAudio API for GWT. Currently supports loading buffers and playing sources as well as setting properties like position, velocity, gain, pitch etc., allowing to easily port OpenAL applications to the Web platform using GWT.

## Design

The design is to conform to the [OpenAL 1.1 Specification](https://www.openal.org/documentation/openal-1.1-specification.pdf) and the API is to be compatible with the OpenAL bindings API of the LWJGL project. The implementation is based on the WebAudio API bindings written internal to this library (but are also exposed). The developer is free to use either OpenAL or WebAudio API with this library.

## Installation

The only way to install for now is to clone the repo and build it manually. I will release the JAR and in maven central once the library is out of alpha and all the remaining OpenAL API has been implemented.

~~~bash
./gradlew clean build javadoc
~~~

That should create a JAR file called as `gwt-al.jar` which you should add to the classpath of your GWT project. Now inherit the GWT-AL library in your module.

~~~xml
<inherits name='com.shc.gwtal.client'/>
~~~

That should make you able to use the OpenAL API as well as the WebAudio API bindings in the module which inherits GWT-AL. You can now proceed to write your audio application.

## Context Creation

The initial step is to create a context that can be used as a handle to the audio. Note that this throws a `AudioContextException` which is a checked exception and must be caught by the developer when creating the context.

~~~java
try
{
    AudioContext context = AudioContext.create();
}
catch (AudioContextException e)
{
    e.printStackTrace();
}
~~~

The OpenAL context creation is different, and you need to make the context current, so as to conform to the API. It will throw the `AudioContextException` too.

~~~java
try
{
    ALContext context = ALContext.create();
    AL.setCurrentContext(context);
}
catch (AudioContextException e)
{
    e.printStackTrace();
}
~~~

Why this occurs is that whenever you create an `ALContext` instance, an `AudioContext` object is created too, because the OpenAL implementation uses the underlying AudioContext to play the actual audio. If that creation is failed, then the exception is thrown, so OpenAL is also not possible on browsers that don't support WebAudio API.
