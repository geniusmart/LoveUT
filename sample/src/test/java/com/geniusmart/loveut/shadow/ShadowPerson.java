package com.geniusmart.loveut.shadow;

import android.content.Context;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;

@Implements(Person.class)
public class ShadowPerson {

    @RealObject
    private Person person;

    public void __constructor(Context c) {
        //note the construction
    }

    @Implementation
    public String getName() {
        return "geniusmart";
    }
}
