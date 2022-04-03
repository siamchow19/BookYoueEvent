package com.example.myapplication;

import android.content.Context;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OwnerHomePageAdapter extends RecyclerView.Adapter<OwnerHomePageAdapter.HomeViewHolder>{

    ArrayList<AddPartyCenterHelper> arrayList;
    OnNoteListenerHome mOnNoteListenerHome;
    Context context;

    public OwnerHomePageAdapter(ArrayList<AddPartyCenterHelper> arrayList, Context context, OnNoteListenerHome mOnNoteListenerHome) {
        this.arrayList = arrayList;
        this.context = context;
        this.mOnNoteListenerHome = mOnNoteListenerHome;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.party_center_list_card, parent, false);
        HomeViewHolder homeViewHolder = new HomeViewHolder(view, mOnNoteListenerHome);
        return homeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OwnerHomePageAdapter.HomeViewHolder holder, int position) {
        AddPartyCenterHelper helper = arrayList.get(position);
        holder.partyCenterName.setText(helper.getPartyCenterName());
        holder.capacity.setText(helper.getCapacity());
        holder.address.setText(helper.getPartyCenterAddress());
        holder.startingPrice.setText(helper.getStartingPrice());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public static class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView partyCenterName;
        TextView capacity;
        TextView address;
        TextView startingPrice;
        OnNoteListenerHome onNoteListenerHome;

        public HomeViewHolder(@NonNull View itemView, OnNoteListenerHome onNoteListenerHome) {
            super(itemView);


            partyCenterName = itemView.findViewById(R.id.list_party_center_name);
            capacity = itemView.findViewById(R.id.list_capacity);
            address = itemView.findViewById(R.id.list_address);
            startingPrice = itemView.findViewById(R.id.list_starting_price);
            this.onNoteListenerHome = onNoteListenerHome;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListenerHome.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListenerHome {
        void onNoteClick(int position);
    }
}
