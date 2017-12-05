package com.example.stephanie.feedthekitty;

import java.util.List;
import java.util.ArrayList;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



/**
 * Created by Stephanie on 11/29/17.
 */

//TODO: what else would be needed...

public class EventListAdapter extends BaseAdapter {

    private List<Event> eventList = new ArrayList<Event>();
    private LayoutInflater inflater;
    private Context mContext;

    private static final String TAG = " Event List Adapter";

    public EventListAdapter(Context context){
        mContext = context;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount(){
        return eventList.size();
    }

    @Override
    public Object getItem(int position){
        return eventList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        View newView = convertView;

        //retrieve current event
        Event curr = eventList.get(position);





        return newView;

    }

}
