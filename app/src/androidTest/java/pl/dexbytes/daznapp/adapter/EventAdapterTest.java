package pl.dexbytes.daznapp.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.MediumTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.dexbytes.daznapp.activity.MainActivity;
import pl.dexbytes.daznapp.model.Event;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class EventAdapterTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    Context mContext;

    @Before
    public void setUp(){
        mContext = InstrumentationRegistry.getInstrumentation().getContext();
    }

    private void setLocale(String language, String country) {
        Locale locale = new Locale(language, country);
        Locale.setDefault(locale);
        Resources res = mContext.getResources();
        Configuration config = res.getConfiguration();
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    @Test
    public void clear() {
        EventAdapter adapter = new EventAdapter(mContext, null, null);
        adapter.add(new Event());
        adapter.add(new Event());
        adapter.clear();
        assertEquals(0, adapter.getItemCount());
    }

    @Test
    public void add() {
        EventAdapter adapter = new EventAdapter(mContext, null, null);
        adapter.add(new Event());
        adapter.add(new Event());
        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void getItemCount() {
        EventAdapter adapter = new EventAdapter(mContext, null, null);
        adapter.add(new Event());
        adapter.add(new Event());
        assertEquals(2, adapter.getItemCount());
    }

    @Test
    public void getItemId() {
        EventAdapter adapter = new EventAdapter(mContext, null, null);
        Event event = new Event();
        event.setId(12345);
        adapter.add(event);
        assertEquals(12345, adapter.getItemId(0));
    }

    @Test
    public void formatDatePl() {
        setLocale("pl", "PL");
        Date now = getDatePlusDays(new Date(), 1);
        CharSequence format = EventAdapter.formatDate(mContext, now);
        assertTrue(format.toString().toLowerCase().contains("jutro"));

        now = getDatePlusDays(new Date(), -1);
        format = EventAdapter.formatDate(mContext, now);
        assertTrue(format.toString().toLowerCase().contains("wczoraj"));
    }

    @Test
    public void formatDateEn(){
        Date now = getDatePlusDays(new Date(), 1);
        setLocale("en", "GB");
        CharSequence format = EventAdapter.formatDate(mContext, now);
        assertTrue(format.toString().toLowerCase().contains("tomorrow"));

        now = getDatePlusDays(new Date(), -1);
        format = EventAdapter.formatDate(mContext, now);
        assertTrue(format.toString().toLowerCase().contains("yesterday"));
    }

    @Test
    public void getItems() {
        EventAdapter adapter = new EventAdapter(mContext, null, null);
        List<Event> events = new ArrayList<>();
        events.add(new Event());
        events.add(new Event());
        events.add(new Event());
        adapter.addAll(events);
        assertEquals(events, adapter.getItems());
    }

    @Test
    public void addAll() {
        EventAdapter adapter = new EventAdapter(mContext, null, null);
        List<Event> events = new ArrayList<>();
        events.add(new Event());
        events.add(new Event());
        events.add(new Event());
        adapter.addAll(events);
        assertEquals(3, adapter.getItemCount());
    }

    public Date setDate(int dd, int MM, int yyyy, int HH, int mm, int ss){
        Calendar c = Calendar.getInstance();
        c.set(dd, MM, yyyy, HH, mm, ss);
        return c.getTime();
    }

    public Date getDatePlusDays(Date date, int days){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, days);
        return c.getTime();
    }
}