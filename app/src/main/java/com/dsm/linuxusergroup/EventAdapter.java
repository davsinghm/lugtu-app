package com.dsm.linuxusergroup;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    public Context context;
    public ArrayList<Event> arrayList = new ArrayList<>();

    public EventAdapter(ArrayList<Event> arrayList) {
        this.arrayList = arrayList;
    }

    public void setArrayList(ArrayList<Event> arrayList) {
        this.arrayList = arrayList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView title;
        public TextView line1;
        public TextView line2;

        public ViewHolder(final View view) {
            super(view);

            CardView cardView = (CardView) view.findViewById(R.id.cardView);
            cardView.setOnClickListener(this);
            title = (TextView) view.findViewById(R.id.title);
            line1 = (TextView) view.findViewById(R.id.line1);
            line2 = (TextView) view.findViewById(R.id.line2);

        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, ScrollingActivity.class);
            intent.putExtra("event_id", arrayList.get(getAdapterPosition()).getId() + "");
            context.startActivity(intent);
            //notifyItemChanged(getAdapterPosition());
        }
    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context = parent.getContext()).inflate(R.layout.list_item_event, parent, false));
    }

    @Override
    public void onBindViewHolder(final EventAdapter.ViewHolder viewHolder, final int position) {

        Event event = arrayList.get(position);
        viewHolder.title.setText(event.getTitle());
        viewHolder.line1.setText("Location: " + event.getLocation());
        viewHolder.line2.setText(event.getStartTs() + " - " + event.getEndTs());

    }

    @Override
    public int getItemCount() {
        if (arrayList != null)
            return arrayList.size();
        return 0;
    }

}
