package com.geniusmart.loveut.shadow;

import com.geniusmart.loveut.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import static junit.framework.TestCase.assertEquals;

@RunWith(CustomShadowTestRunner.class)
@Config(constants = BuildConfig.class,shadows = {ShadowPerson.class})
public class ShadowTest {

    @Test
    public void testGetName(){
        Person person = new Person();
        //Person person = new Person();
        //ShadowExtractor.extract(person);
        assertEquals("geniusmart", person.getName());
    }

}
