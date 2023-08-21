package com.openclassrooms.realestatemanager;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UtilsInstrumentedTest {

    @Test
    public void checkInternetConnection() {
        assertEquals(true, Utils.isInternetAvailable(ApplicationProvider.getApplicationContext()));
    }
}
