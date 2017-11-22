package com.unipi.mpsp160_02_12.advancedrandezvous.models;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.unipi.mpsp160_02_12.advancedrandezvous.EventActivity;
import com.unipi.mpsp160_02_12.advancedrandezvous.R;
import com.unipi.mpsp160_02_12.advancedrandezvous.models.Event;

import java.util.List;

/**
 * Created by Nick on 17/9/2017.
 */

public class CompletedListAdapter extends ArrayAdapter<Event> {

    public CompletedListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public CompletedListAdapter(Context context, int resource, List<Event> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.completed_events_item, null);
        }

        final Event p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.completed_event_row_title);

            if (tt1 != null) {
                tt1.setText(p.getTitle());
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), EventActivity.class);
                        intent.putExtra("id", p.getId());
                        getContext().startActivity(intent);
                    }
                });
            }
        }

        return v;
    }
}

