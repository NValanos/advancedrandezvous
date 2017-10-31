package com.unipi.mpsp160_02_12.advancedrandezvous.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.unipi.mpsp160_02_12.advancedrandezvous.FriendsActivity;
import com.unipi.mpsp160_02_12.advancedrandezvous.R;

import java.util.List;

/**
 * Created by Dimitris on 25/9/2017.
 */

public class FriendListAdapter extends ArrayAdapter<Friend> {

    public FriendListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public FriendListAdapter(Context context, int resource, List<Friend> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.row, null);
        }

        final Friend p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.friend);

            if (tt1 != null) {
                tt1.setText(p.getEmail());

            }

            Button btn = (Button) v.findViewById(R.id.btnListAction);
            if (FriendsActivity.isFromEvent){
                btn.setText(R.string.add);
            }
            else{
                btn.setText(R.string.delete);
            }

        }

        return v;
    }

}
