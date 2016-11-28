package com.github.fyr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by RussBuss on 11/25/2016.
 */

public class ChatListAdapter extends BaseAdapter {
    protected ArrayList<ListViewObjects> chatList;
    protected LayoutInflater infl;

    public ChatListAdapter(Context context, ArrayList<ListViewObjects> cL){
        this.chatList = cL;
        this.infl = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return this.chatList.size();
    }

    @Override
    public Object getItem(int i) {
        return this.chatList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = infl.inflate(R.layout.chat_list_layout, null);
            holder = new ViewHolder();
            holder.match_name = (TextView) convertView.findViewById(R.id.match_name);
            holder.email = (TextView) convertView.findViewById(R.id.email);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.match_name.setText(chatList.get(position).getName());
        holder.email.setText(chatList.get(position).getEmail());
        return convertView;
    }

    static class ViewHolder{
        TextView match_name;
        TextView email;
    }

    public ArrayList<ListViewObjects> getChatList(){
        return this.chatList;
    }
}
