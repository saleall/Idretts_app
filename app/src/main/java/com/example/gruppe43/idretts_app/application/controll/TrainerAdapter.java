package com.example.gruppe43.idretts_app.application.controll;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gruppe43.idretts_app.R;
import com.example.gruppe43.idretts_app.application.model.TrainerDummyData;

import java.util.Collections;
import java.util.List;

/**
 * Created by gebi9 on 31-Mar-17.
 */

public class TrainerAdapter extends RecyclerView.Adapter<TrainerAdapter.TrainerViewHolder> {
    private Context context;
    List<TrainerDummyData> data = Collections.emptyList();// takes care of that we dont have empty null pointer exception
    private LayoutInflater inflater;
    private int previousPosition = 0;


    public TrainerAdapter(Context context, List<TrainerDummyData> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public TrainerViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflater.inflate(R.layout.trainer_items, parent, false);
        TrainerViewHolder holder = new TrainerViewHolder(view);
        return holder;
    }

    @Override//we use this to fill the data items inn
    public void onBindViewHolder(TrainerViewHolder trainerViewholder, final int position) {


        trainerViewholder.title.setText(data.get(position).tittle);
        trainerViewholder.icon.setImageResource(data.get(position).iconId);
        previousPosition = position;


        final int currentPosition = position;
        final TrainerDummyData ddata = data.get(position);

        trainerViewholder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "OnClick Called at position " + position, Toast.LENGTH_SHORT).show();
               // addItem(currentPosition, ddata); /*NEW ITEM ADDING COULD BE DONE HERE!///////////////////////////////////////////////////////////////////*/
            }
        });

        trainerViewholder.icon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "OnLongClick Called at position " + position, Toast.LENGTH_SHORT).show();
               // removeItem(ddata); /*NEW ITEM removing!///////////////////////////////////////////////////////////////////*/
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    /*here is where the data is fed into the recycler view///////////////////////////////////////////////*/
    class TrainerViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView icon;

        public TrainerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.listTextTrainer);
            icon = (ImageView) itemView.findViewById(R.id.listIconTrainer);
        }
    }

    // This method adds(duplicates) a Object (item ) to our Data set as well as Recycler View.
    private void addItem(int position, TrainerDummyData infoData) {

        data.add(position, infoData);
        notifyItemInserted(position);
    }

    // This removes the data from our Dataset and Updates the Recycler View.
    private void removeItem(TrainerDummyData infoData) {
        int currPosition = data.indexOf(infoData);
        data.remove(currPosition);
        notifyItemRemoved(currPosition);
    }
}
