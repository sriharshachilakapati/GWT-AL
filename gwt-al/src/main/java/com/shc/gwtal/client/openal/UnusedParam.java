package com.shc.gwtal.client.openal;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * The {@link UnusedParam} annotation is used to mark down the parameters of OpenAL functions that are not used
 * in the implementation. Though this is a bug, these will be slowly eliminated as the library progresses.
 *
 * @author Sri Harsha Chilakapati
 */
@Documented
@Target(ElementType.PARAMETER)
public @interface UnusedParam
{
}
