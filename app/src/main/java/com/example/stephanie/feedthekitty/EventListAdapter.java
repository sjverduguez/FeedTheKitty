package com.example.stephanie.feedthekitty;

import java.util.List;
import java.util.ArrayList;

import android.content.Context;
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
        ViewHolder holder;

        //retrieve current event
        Event curr = eventList.get(position);

        if(convertView == null){

            holder = new ViewHolder();

            //creates the view
            newView = inflater.inflate(R.layout.event_view, parent, false);

            //retrieve data to populate holder fields
            holder.eventTitle = (TextView) newView.findViewById(R.id.row_event_text);
            holder.fundTotal = (TextView) newView.findViewById(R.id.row_total_text);
            newView.setTag(holder);

        }else{

            holder = (ViewHolder) newView.getTag();
        }

        holder.eventTitle.setText(curr.getEventTitle());
        holder.fundTotal.setText(curr.getFundTotal());

        return newView;

    }

    static class ViewHolder{

        TextView eventTitle;
        TextView fundTotal;
    }
}
