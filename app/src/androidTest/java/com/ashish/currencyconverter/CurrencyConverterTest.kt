package com.ashish.currencyconverter

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.ashish.currencyconverter.rest.RepositoryImplementation
import com.ashish.currencyconverter.ui.home.CurrencyConverter
import org.hamcrest.Matchers.not

@RunWith(AndroidJUnit4::class)
@LargeTest
class CurrencyConverterTest {
    private lateinit var stringToBetyped: String

    @get:Rule
    var activityRule: ActivityTestRule<CurrencyConverter> =
        ActivityTestRule(CurrencyConverter::class.java)

    @Before
    fun initValidString() {
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
    
}