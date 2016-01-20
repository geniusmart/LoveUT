package com.geniusmart.loveut.shadow;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

@Implements(Person.class)
public class ShadowPerson {

    @Implementation
    public String getName() {
        return "geniusmart";
    }
}
