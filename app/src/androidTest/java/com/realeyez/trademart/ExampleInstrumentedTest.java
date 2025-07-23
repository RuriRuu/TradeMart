package com.realeyez.trademart;

import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.realeyez.trademart.request.Request;
import com.realeyez.trademart.request.RequestUtil;
import com.realeyez.trademart.request.Response;
import com.realeyez.trademart.request.Request.RequestBuilder;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Rule;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Rule
    public ActivityScenarioRule<SkillCardView> activityRule
    =new ActivityScenarioRule<>(SkillCardView.class);

    @Test
    public void viewProfileView(){
        ActivityScenario.launch(SkillCardView.class);
    }

    // @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//        Instrumentation instrumentation = Instrumentation.
        assertEquals("com.example.myapplication", appContext.getPackageName());
    }

    // @Test
    public void test_requestParams() throws IOException, JSONException {
        HashMap<String, String> params = new HashMap<>();
        params.put("q", "Hello, World");
        Response response = RequestUtil.sendGetRequest("/test", params);
        JSONObject json = response.getContentJson();
        Log.d("trademart", response.getContent());
        assertEquals("Hello, World", json.getString("value"));
    }

}
