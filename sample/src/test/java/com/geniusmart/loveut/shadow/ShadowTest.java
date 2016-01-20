package com.geniusmart.loveut.shadow;

import com.geniusmart.loveut.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.internal.ShadowExtractor;

import static junit.framework.TestCase.assertEquals;

@RunWith(CustomShadowTestRunner.class)
@Config(constants = BuildConfig.class,shadows = {ShadowPerson.class})
public class ShadowTest {

    /**
     * 测试自定义的Shadow
     */
    @Test
    public void testCustom(){
        Person person = new Person("genius");
        //getName()实际上调用的是ShadowPerson的方法
        assertEquals("geniusmart", person.getName());

        //获取Person对象对应的Shadow对象
        ShadowPerson shadowPerson = (ShadowPerson) ShadowExtractor.extract(person);
        assertEquals("geniusmart", shadowPerson.getName());
    }

    @Test
    public void testDefault(){

    }

}
