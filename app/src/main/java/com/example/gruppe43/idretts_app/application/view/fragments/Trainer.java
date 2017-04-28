package com.example.gruppe43.idretts_app.application.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gruppe43.idretts_app.R;
import com.example.gruppe43.idretts_app.application.model.TrainerModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Trainer extends Fragment {
    private RecyclerView recyclerViewTrainerRV;
    private DatabaseReference fbDbRef;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trainer,container,false);

        recyclerViewTrainerRV = (RecyclerView) view.findViewById(R.id.recyclerViewTrainer);

        fbDbRef =  FirebaseDatabase.getInstance().getReference().child("TrainerPosts");

        FirebaseRecyclerAdapter<TrainerModel, TrainerViewHolder> trainerAdapter = new FirebaseRecyclerAdapter<TrainerModel, TrainerViewHolder>(
                TrainerModel.class,
                R.layout.trainer_items,
                TrainerViewHolder.class,
                fbDbRef
        ) {
            @Override
            protected void populateViewHolder(TrainerViewHolder viewHolder, TrainerModel model, final int position) {

                viewHolder.setActivityTitleDate(model.getTitle() +"|"+model.getActivityDate());
                viewHolder.setActivityStartEndTime(getString(R.string.actStarts)+model.getStartTime()+" | "+getString(R.string.actEnds)+model.getEndTime());
                viewHolder.setActivityPlace(getString(R.string.actLocation)+model.getPlace()+" | ");
                viewHolder.setTrainerActivityTime(model.getTimePosted()); 
                viewHolder.setTrainerActivityIntensity(model.getIntensity());
                viewHolder.setTrainerActivityIcon(model.getIcon());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO go to the full activity info page
                        System.out.println("//////////////////////////////RECYCLER click "+position);
                    }
                });
            }
        };

        recyclerViewTrainerRV.setAdapter(trainerAdapter);
        recyclerViewTrainerRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    public static class TrainerViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView trainerPostTitleDate;
        TextView trainerPostStartStopTime;
        TextView trainerPostPlace;
        TextView trainerPostTime;
        TextView trainerPostIntensity;
        ImageView trainerPostIcon;


        public TrainerViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setActivityTitleDate (String titleDate) {
            trainerPostTitleDate = (TextView) mView.findViewById(R.id.listTextTrainer);
            trainerPostTitleDate.setText(titleDate);
        }

        public void setActivityStartEndTime(String startEndTime) {
            trainerPostStartStopTime = (TextView) mView.findViewById(R.id.detailInfo1Trainer);
            trainerPostStartStopTime .setText(startEndTime);
        }

        public void setActivityPlace(String place ) {
            trainerPostPlace = (TextView) mView.findViewById(R.id.detailInfo2Trainer);
            trainerPostPlace.setText(place);
        }

        public void setTrainerActivityTime(String postTime) {
            trainerPostTime = (TextView) mView.findViewById(R.id.timePostedTrainer);
            trainerPostTime.setText(postTime);
        }

        public void setTrainerActivityIntensity(String intensity) {
            trainerPostIntensity = (TextView) mView.findViewById(R.id.percentageTrainer);
            trainerPostIntensity.setText(intensity);
        }

        public void setTrainerActivityIcon (String icon) {
            trainerPostIcon = (ImageView) mView.findViewById(R.id.listIconTrainer);
            if(icon.equals("camp")){
                trainerPostIcon.setImageResource(R.drawable.cmp);
            }else if(icon.equals("training_f")){//football
                trainerPostIcon.setImageResource(R.drawable.training);
            }else if(icon.equals("meeting")){
                trainerPostIcon.setImageResource(R.drawable.meeting);
            }else if(icon.equals("training")){//styrke
                trainerPostIcon.setImageResource(R.drawable.training_s);
            }
        }

    }///////////////////////////////


    /*public static List<TrainerModel>getData(){
        List<TrainerModel> data = new ArrayList<>();
        int[] icons = {R.drawable.training,R.drawable.cmp,R.drawable.training,R.drawable.meeting,
                R.drawable.training_s,R.drawable.training_s,R.drawable.training_s,R.drawable.training};
        String[] tittles = {"Football training ","Football kamp","Football training",
                "Oppmote for samlet teori","Styrke training","Styrke training","Styrke training","Football training",};

        //create list of dummydata objects to display.
        for (int i = 0; i <tittles.length && i < icons.length ; i++) {//so that if any of the arrays are shorter, that the app does not crash here..
            TrainerModel current = new TrainerModel();
            *//*current.iconId = icons[i];
            current.tittle = tittles[i];*//*
            data.add(current);
        }
        return data;
    }*/

}
