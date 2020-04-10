package com.ashish.currencyconverter

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.Observer
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.rule.ActivityTestRule
import com.ashish.currencyconverter.rest.RepositoryImplementation
import com.ashish.currencyconverter.ui.home.CurrencyConverter
import com.ashish.currencyconverter.util.Constants
import org.hamcrest.Matchers.not
import org.json.JSONObject
import org.junit.Assert
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class CurrencyConverterTest {
    private lateinit var stringToBetyped: String
    private val lifecycleOwner = TestLifecycleOwner()

    @Mock
    lateinit var repositoryImplementation: RepositoryImplementation
    @get:Rule
    var activityRule: ActivityTestRule<CurrencyConverter> =
        ActivityTestRule(CurrencyConverter::class.java)

    @Before
    fun initValidString() {
        MockitoAnnotations.initMocks(this)
        stringToBetyped = "Currency Converter"
    }

    @Test
    fun onLaunchCheckAmountInputIsDisplayed() {
        ActivityScenario.launch(CurrencyConverter::class.java)
        onView(withId(R.id.tvFromInput)).check(matches(isDisplayed()))
    }

    @Test
    fun testDefaultCodeIsNot() {
        ActivityScenario.launch(CurrencyConverter::class.java)
        onView(withId(R.id.tvFromCode)).check(matches(not(withText("null"))))
        onView(withId(R.id.tvToCode)).check(matches(not(withText("null"))))
        onView(withId(R.id.tvFromInputCode)).check(matches(not(withText("null"))))
        onView(withId(R.id.tvToInputCode)).check(matches(not(withText("null"))))
        onView(withId(R.id.tvFromInput)).check(matches(not(withText(""))))

    }

    @Test
    fun clickFromCodeLayout() {
        ActivityScenario.launch(CurrencyConverter::class.java)
        Thread.sleep(2000)
        onView(withId(R.id.fromInputLayout)).perform(click())
        Thread.sleep(3000)
    }

    @Test
    fun clickToCodeLayout() {
        ActivityScenario.launch(CurrencyConverter::class.java)
        Thread.sleep(200)
        onView(withId(R.id.toInputLayout)).perform(click())
        Thread.sleep(3000)
    }

    @Test
    fun testAPI() {
        var repositoryImplementation = RepositoryImplementation()

        Thread.sleep(5000)


       var thread = Thread() {
            repositoryImplementation.getCurrencyCodes("USD", activityRule.activity.baseContext)
                .observe(lifecycleOwner, Observer {
                    var jsonObject = JSONObject(it.toString())
                    Assert.assertEquals(jsonObject.optString(Constants.RESULT,
                        Constants.DEFAULT_VALUE), Constants.SUCCESS)
                })
        }
        thread.start()
    }

    class TestLifecycleOwner : LifecycleOwner {

        private val lifecycle = LifecycleRegistry(this)

        fun onCreate() {
            lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        }

        fun onStart() {
            lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)
        }

        // Other lifecycle callback methods

        override fun getLifecycle() = lifecycle
    }

}