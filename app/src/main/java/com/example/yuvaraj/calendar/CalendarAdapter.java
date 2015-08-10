package com.example.yuvaraj.calendar;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * @author yuvaraj yadav
 */

/**
 * Created by Yuvaraj Yadav on 16/7/15.
 */

public class CalendarAdapter extends BaseAdapter {
	private static final int FIRST_DAY_OF_WEEK = Calendar.MONDAY;
    private final Calendar calendar;
    private final CalendarItem today;
    private final CalendarItem selected;
    private final LayoutInflater inflater;
    private CalendarItem[] days;

    private ArrayList<String> dotitems;

    public CalendarAdapter(Context context, Calendar monthCalendar,ArrayList<String> eventlist) {
    	calendar = monthCalendar;
    	today = new CalendarItem(monthCalendar);
    	selected = new CalendarItem(monthCalendar);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        this.dotitems = eventlist;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
	public int getCount() {
        return days.length;
    }

    @Override
	public Object getItem(int position) {
        return days[position];
    }

    @Override
	public long getItemId(int position) {
    	final CalendarItem item = days[position];
    	if (item != null) {
    		return days[position].id;
    	}
    	return -1;
    }

    @Override
	public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = inflater.inflate(R.layout.calendar_item, null);
        }
        final TextView dayView = (TextView)view.findViewById(R.id.date);
        final CalendarItem currentItem = days[position];

        if (currentItem == null) {
        	dayView.setClickable(false);
        	dayView.setFocusable(false);
        	view.setBackgroundDrawable(null);
        	dayView.setText(null);
        } else {
        	if (currentItem.equals(today)) {
        		//view.setBackgroundResource(R.drawable.today_background);
                dayView.setText(currentItem.text);
                dayView.setTextColor(Color.parseColor("#FFFFFF"));
                dayView.setBackgroundResource(R.drawable.draw_circle);
        	} else if (currentItem.equals(selected)) {
        		//view.setBackgroundResource(R.drawable.selected_background);
                dayView.setText(currentItem.text);
                dayView.setTextColor(Color.parseColor("#000000"));
                dayView.setBackgroundResource(R.drawable.draw_circle);
                //dayView.setBackgroundResource(android.R.color.white);
        	} else {
        		view.setBackgroundResource(R.drawable.normal_background);
                dayView.setText(currentItem.text);
                dayView.setTextColor(Color.parseColor("#000000"));
                dayView.setBackgroundResource(android.R.color.white);
        	}

            int monthaddone=currentItem.month+1;

            String checkdate=""+currentItem.day+monthaddone+currentItem.year;

            ImageView iw = (ImageView) view.findViewById(R.id.imageView);

            if (dotitems.contains(checkdate)) {
                iw.setVisibility(View.VISIBLE);
            } else {
                iw.setVisibility(View.GONE);
            }

        }

        return view;
    }

    public final void setSelected(int year, int month, int day) {
    	selected.year = year;
    	selected.month = month;
    	selected.day = day;
    	notifyDataSetChanged();
    }


    public final void refreshDays() {
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int firstDayOfMonth = calendar.get(Calendar.DAY_OF_WEEK);
        final int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        final int blankies;
        final CalendarItem[] days;

        if (firstDayOfMonth == FIRST_DAY_OF_WEEK) {
            blankies = 0;
        } else if (firstDayOfMonth < FIRST_DAY_OF_WEEK) {
            blankies = Calendar.SATURDAY - (FIRST_DAY_OF_WEEK - 1);
        } else {
            blankies = firstDayOfMonth - FIRST_DAY_OF_WEEK;
        }
        days = new CalendarItem[lastDayOfMonth + blankies];

        for (int day = 1, position = blankies; position < days.length; position++) {
            days[position] = new CalendarItem(year, month, day++);
        }

        this.days = days;
        notifyDataSetChanged();

    }

    private static class CalendarItem {
    	public int year;
    	public int month;
    	public int day;
    	public String text;
    	public long id;

    	public CalendarItem(Calendar calendar) {
    		this(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    	}

    	public CalendarItem(int year, int month, int day) {
			this.year = year;
			this.month = month;
			this.day = day;
			this.text = String.valueOf(day);
			this.id = Long.valueOf(year + "" + month + "" + day);
		}

    	@Override
    	public boolean equals(Object o) {
    		if (o != null && o instanceof CalendarItem) {
    			final CalendarItem item = (CalendarItem)o;
    			return item.year == year && item.month == month && item.day == day;
    		}
    		return false;
    	}
    }
}