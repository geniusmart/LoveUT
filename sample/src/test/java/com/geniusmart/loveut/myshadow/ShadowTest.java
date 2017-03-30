package com.geniusmart.loveut.myshadow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.geniusmart.loveut.BuildConfig;
import com.geniusmart.loveut.activity.MainActivity;
import com.geniusmart.loveut.shadow.Person;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.internal.ShadowExtractor;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowBitmap;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertEquals;

/**
 * 注：使用自定义Shadow时，测试类的包名不能与被Shadow的类的包名一致，否则会出问题
 * 如：http://stackoverflow.com/questions/18976851/robolectrictestrunner-test-class-can-only-have-one-constructor
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class,shadows = {ShadowPerson.class},sdk = 21)
public class ShadowTest {

    /**
     * 测试自定义的Shadow
     */
    @Test
    public void testCustomShadow(){
        Person person = new Person("genius");
        //getName()实际上调用的是ShadowPerson的方法
        assertEquals("geniusmart", person.getName());

        //获取Person对象对应的Shadow对象
        ShadowPerson shadowPerson = (ShadowPerson) ShadowExtractor.extract(person);
        assertEquals("geniusmart", shadowPerson.getName());
    }

    /**
     * 测试框架提供的Shadow类
     */
    @Test
    public void testDefaultShadow(){

        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);

        //通过Shadows.shadowOf()可以获取很多Android对象的Shadow对象
        ShadowActivity shadowActivity = Shadows.shadowOf(mainActivity);
        ShadowApplication shadowApplication = Shadows.shadowOf(RuntimeEnvironment.application);

        Bitmap bitmap = BitmapFactory.decodeFile("Path");
        ShadowBitmap shadowBitmap = Shadows.shadowOf(bitmap);

        //Shadow对象提供方便我们用于模拟业务场景进行测试的api
        assertNull(shadowActivity.getNextStartedActivity());
        assertNull(shadowApplication.getNextStartedActivity());
        assertNotNull(shadowBitmap);

    }

}
