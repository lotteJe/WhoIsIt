package com.example.android.whoisit.activities;


import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.test.ActivityInstrumentationTestCase2;

import com.example.android.whoisit.R;
import com.example.android.whoisit.WhoIsItApplication;
import com.example.android.whoisit.models.Student;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import io.objectbox.Box;
import io.objectbox.BoxStore;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.example.android.whoisit.activities.TestUtils.withRecyclerView;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by lottejespers.
 */
public class RecyclerViewTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private String naam = "Ellen";
    private String trait1 = "Bruine krullen";
    private String trait2 = "Leest graag";
    private String trait3 = "Groene tinten";

    public RecyclerViewTest() {
        super(MainActivity.class);
    }

    @Test
    public void testItemClickDisplayed() {
        initValidPerson();
        onView(withRecyclerView(R.id.recyclerview).atPosition(1)).perform(click());

        onView(withId(R.id.studentName)).check(matches(isDisplayed()));
        onView(withId(R.id.trait1)).check(matches(isDisplayed()));
        onView(withId(R.id.trait2)).check(matches(isDisplayed()));
        onView(withId(R.id.trait3)).check(matches(isDisplayed()));
        onView(withId(R.id.studentImage)).check(matches(isDisplayed()));
    }

    @Test
    public void testItemClickCorrectInfo() {
        initValidPerson();
        onView(withRecyclerView(R.id.recyclerview).atPosition(0)).perform(click());

        onView(withId(R.id.studentName)).check(matches(withText(naam)));
        onView(withId(R.id.trait1)).check(matches(withText(trait1)));
        onView(withId(R.id.trait2)).check(matches(withText(trait2)));
        onView(withId(R.id.trait3)).check(matches(withText(trait3)));
        onView(withId(R.id.studentImage)).check(matches(isDisplayed()));
    }

    private void initValidPerson() {
        WhoIsItApplication app = (WhoIsItApplication) getActivity().getApplication();
        Box studentBox = app.getBoxStore().boxFor(Student.class);
        studentBox.removeAll();
        studentBox.put(new Student(0, "", naam, Arrays.asList(trait1, trait2, trait3)));
        studentBox.put(new Student(0, "", "Lotte", Arrays.asList(trait1, trait2, trait3)));
        MainActivity activity = (MainActivity) getActivity();
        activity.showStudentsFragment();
        return;
    }

}