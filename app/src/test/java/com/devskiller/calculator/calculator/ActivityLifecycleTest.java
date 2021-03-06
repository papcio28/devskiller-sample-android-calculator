package com.devskiller.calculator.calculator;

import android.os.Bundle;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.assertj.android.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ActivityLifecycleTest {

    private ActivityController<MainActivity> controller;
    private MainActivity activity;

    @Before
    public void setUp() {
        controller = Robolectric.buildActivity(MainActivity.class);
        activity = controller
                .create()
                .start()
                .resume()
                .visible()
                .get();
    }

    @After
    public void tearDown() {
        controller
                .pause()
                .stop()
                .destroy();
    }

    @Test
    public void shouldKeepCalculatorValuesAfterActivityPauseAndResume() {

        // given
        activity.findViewById(R.id.btn1).performClick();
        activity.findViewById(R.id.btn2).performClick();
        activity.findViewById(R.id.btn3).performClick();

        // when
        controller.pause().resume();

        // then
        assertThat((TextView) activity.findViewById(R.id.result)).hasText("123");
    }

    @Test
    public void shouldKeepCalculatorValuesAfterActivityRecreation() {

        // given
        activity.findViewById(R.id.btn1).performClick();
        activity.findViewById(R.id.btn2).performClick();
        activity.findViewById(R.id.btn3).performClick();

        Bundle bundle = new Bundle();

        // when
        destroyOriginalActivity(bundle);
        bringUpOriginalActivity(bundle);
        activity.findViewById(R.id.btn4).performClick();

        // then
        assertThat((TextView) activity.findViewById(R.id.result)).hasText("1234");
    }

    private void bringUpOriginalActivity(Bundle bundle) {
        controller = Robolectric.buildActivity(MainActivity.class)
                .create(bundle)
                .start()
                .restoreInstanceState(bundle)
                .resume()
                .visible();
        activity = controller.get();
    }

    private void destroyOriginalActivity(Bundle bundle) {
        controller
                .saveInstanceState(bundle)
                .pause()
                .stop()
                .destroy();
    }
}
