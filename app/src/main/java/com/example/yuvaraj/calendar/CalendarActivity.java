package com.example.yuvaraj.calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
/**
 * @author yuvaraj yadav
 */
/**
 * Created by Yuvaraj Yadav on 16/7/15.
 */
public class CalendarActivity extends FragmentActivity {


    protected final Calendar calendar;
    private final Locale locale;
    private ViewSwitcher calendarSwitcher;
    private TextView currentMonth;
    private CalendarAdapter calendarAdapter;

    ListView lv_event;

    ArrayList<String> eventlist = new ArrayList<String>();

    public CalendarActivity() {
        calendar = Calendar.getInstance();
        locale = Locale.getDefault();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GridView calendarDayGrid = (GridView) findViewById(R.id.calendar_days_grid);
        final GestureDetector swipeDetector = new GestureDetector(this, new SwipeGesture(this));
        final GridView calendarGrid = (GridView) findViewById(R.id.calendar_grid);

        lv_event = (ListView) findViewById(R.id.listView);

        calendarSwitcher = (ViewSwitcher) findViewById(R.id.calendar_switcher);
        currentMonth = (TextView) findViewById(R.id.current_month);

        eventlist.add("1862015");
        eventlist.add("1962015");
        eventlist.add("1062015");
        eventlist.add("2072015");
        eventlist.add("2382015");

        calendarAdapter = new CalendarAdapter(this, calendar, eventlist);
        updateCurrentMonth();

        final ImageView nextMonth = (ImageView) findViewById(R.id.next_month);
        nextMonth.setOnClickListener(new NextMonthClickListener());
        final ImageView prevMonth = (ImageView) findViewById(R.id.previous_month);
        prevMonth.setOnClickListener(new PreviousMonthClickListener());
        calendarGrid.setOnItemClickListener(new DayItemClickListener());

        calendarGrid.setAdapter(calendarAdapter);
        calendarAdapter.notifyDataSetChanged();
        calendarGrid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return swipeDetector.onTouchEvent(event);
            }
        });

        calendarDayGrid.setAdapter(new ArrayAdapter<String>(this, R.layout.day_item, getResources().getStringArray(R.array.days_array)));


    }


    protected void updateCurrentMonth() {
        calendarAdapter.refreshDays();
        calendarAdapter.notifyDataSetChanged();
        currentMonth.setText(String.format(locale, "%tB", calendar) + " " + calendar.get(Calendar.YEAR));
    }


    private final class DayItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final TextView dayView = (TextView) view.findViewById(R.id.date);
            final CharSequence text = dayView.getText();
            if (text != null && !"".equals(text)) {
                calendarAdapter.setSelected(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), Integer.valueOf(String.valueOf(text)));
            }

            List<Event_Pojo> eventlist = new ArrayList<Event_Pojo>();
            eventlist.clear();

            for (int i = 0; i < 2; i++) {
                Event_Pojo eventitems = new Event_Pojo();
                eventitems.setHeading("Today at 1:00 Pm");
                eventitems.setText1("Manhattan");
                eventitems.setText2("New York");
                eventitems.setType(R.drawable.manhattan);
                eventitems.setProfile(R.drawable.newyork_manhattan);

                eventlist.add(eventitems);
                eventitems = null;
            }

            Event_ListAdapter myListAdapter = new Event_ListAdapter(CalendarActivity.this, eventlist);
            lv_event.setAdapter(myListAdapter);
            myListAdapter.notifyDataSetChanged();

        }
    }

    protected final void onNextMonth() {
        calendarSwitcher.setInAnimation(CalendarActivity.this, R.anim.in_from_right);
        calendarSwitcher.setOutAnimation(CalendarActivity.this, R.anim.out_to_left);
        calendarSwitcher.showNext();
        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
            calendar.set((calendar.get(Calendar.YEAR) + 1), Calendar.JANUARY, 1);
        } else {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        }
        updateCurrentMonth();
    }

    protected final void onPreviousMonth() {
        calendarSwitcher.setInAnimation(CalendarActivity.this, R.anim.in_from_left);
        calendarSwitcher.setOutAnimation(CalendarActivity.this, R.anim.out_to_right);
        calendarSwitcher.showPrevious();
        if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
            calendar.set((calendar.get(Calendar.YEAR) - 1), Calendar.DECEMBER, 1);
        } else {
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        }
        updateCurrentMonth();
    }

    private final class NextMonthClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            onNextMonth();
        }
    }

    private final class PreviousMonthClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            onPreviousMonth();
        }
    }


    private final class SwipeGesture extends GestureDetector.SimpleOnGestureListener {

        public SwipeGesture(Context context) {
            final ViewConfiguration viewConfig = ViewConfiguration.get(context);
        }

        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();

                if (diffAbs > SWIPE_MAX_OFF_PATH)
                    return false;

                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE  && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    onNextMonth();

                    // Right swipe
                } else if (-diff > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    onPreviousMonth();
                }
            } catch (Exception e) {
                Log.e("Calendar Activity", "Error on gestures");
            }
            return false;
        }
    }

}
