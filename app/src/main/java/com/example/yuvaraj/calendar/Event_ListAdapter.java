package com.example.yuvaraj.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuvaraj yadav
 */

/**
 * Created by Yuvaraj Yadav on 16/7/15.
 */

public class Event_ListAdapter extends BaseAdapter {


    Context context;
    public List<Event_Pojo> eventList = new ArrayList<Event_Pojo>();

    LayoutInflater inflater;


    public Event_ListAdapter(Context c, List<Event_Pojo> schedulerlist) {
        // TODO Auto-generated constructor stub
        this.context = c;
        this.eventList = schedulerlist;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View vi = convertView;
        if (convertView == null)
            vi = inflater.inflate(R.layout.event_item, null);

        ImageView profile = (ImageView) vi.findViewById(R.id.list_image);
        ImageView type = (ImageView) vi.findViewById(R.id.type);
        TextView title = (TextView) vi.findViewById(R.id.title);
        TextView eventviewstartend = (TextView) vi.findViewById(R.id.message);
        TextView eventviewlocation = (TextView) vi.findViewById(R.id.location);

        title.setText(eventList.get(position).getHeading().substring(0, 1).toUpperCase() + eventList.get(position).getHeading().substring(1));
        eventviewstartend.setText(eventList.get(position).getText1());
        eventviewlocation.setText(eventList.get(position).getText2());

        profile.setImageResource(eventList.get(position).getProfile());
        type.setImageResource(eventList.get(position).getType());

        return vi;
    }

}
