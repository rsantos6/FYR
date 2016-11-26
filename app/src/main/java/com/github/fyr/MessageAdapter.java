package com.github.fyr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by RussBuss on 11/25/2016.
 */

public class MessageAdapter extends BaseAdapter {

    protected ArrayList<MessageObject> messageList;
    protected LayoutInflater infl;

    public MessageAdapter(Context context, ArrayList<MessageObject> mL){
        this.messageList = mL;
        this.infl = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return this.messageList.size();
    }

    @Override
    public MessageObject getItem(int i) {
        return this.messageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = infl.inflate(R.layout.message_layout, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.message = (TextView) convertView.findViewById(R.id.message);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(messageList.get(position).getName()+": ");
        holder.message.setText(messageList.get(position).getMessage());
        return convertView;
    }

    static class ViewHolder{
        TextView name;
        TextView message;
    }
}
