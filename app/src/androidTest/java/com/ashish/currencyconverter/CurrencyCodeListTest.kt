package com.ashish.currencyconverter

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.InstrumentationRegistry.getContext
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.ashish.currencyconverter.room.CurrencyRepo
import com.ashish.currencyconverter.ui.codelist.CurrencyCodeList
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.security.AccessController.getContext


@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class CurrencyCodeListTest {
    private lateinit var stringToBetyped: String


    @get:Rule
    var activityRule: ActivityTestRule<CurrencyCodeList> =
        ActivityTestRule(CurrencyCodeList::class.java)
/*
    @get:Rule
    val mActivityTestRule: ActivityTestRule<CurrencyCodeList> =
        object : ActivityTestRule<CurrencyCodeList>(CurrencyCodeList::class.java) {
            override fun getActivityIntent(): Intent {
               var currencyRepo=  CurrencyRepo()
                val targetContext = InstrumentationRegistry.getInstrumentation().targetContext
                return Intent(targetContext, CurrencyCodeList::class.java).apply {
                    putExtra("CUR_API_ARRAY",currencyRepo.getRates())

                }
            }
        }*/

    @Before
    fun initValidString() {
        stringToBetyped = "Currency Converter"
    }


    @Test
    fun onLaunchCheckAmountInputIsDisplayed() {
        ActivityScenario.launch(CurrencyCodeList::class.java)
        onView(withId(R.id.codeList)).check(ViewAssertions.matches(
            isDisplayed()))
    }


    @Test
    fun onBackButtonClick(){
        Thread.sleep(3000)
        onView(withId(R.id.backToHome)).perform(click())
    }
    @Test fun itemClick() {
        Thread.sleep(3000)
        onView(withId(R.id.codeList)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

    }

}