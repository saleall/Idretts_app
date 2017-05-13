package com.example.gruppe43.idretts_app.application.view.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gruppe43.idretts_app.R;
import com.example.gruppe43.idretts_app.application.controll.DataBaseHelperA;
import com.example.gruppe43.idretts_app.application.controll.DataBaseHelperB;
import com.example.gruppe43.idretts_app.application.controll.DatabaseHelperC;
import com.example.gruppe43.idretts_app.application.helper_classes.PrefferencesClass;
import com.example.gruppe43.idretts_app.application.interfaces.FragmentActivityInterface;
import com.example.gruppe43.idretts_app.application.model.UsersModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Team extends Fragment {

    private DatabaseReference fbDbRef;
    private DatabaseReference fbDbUsersRef;
    private DataBaseHelperA databaseHelper;
    private FragmentActivityInterface mCallback;
    private PrefferencesClass prefs;
    private RecyclerView recyclerViewTeamRV;
    static boolean isForCheckAbsence;
    private RelativeLayout adtoAbsentHolderRLayout;
    private Button button2AddToAbsentLists;
    public static String selectedUserIdInTeam;
    private ArrayList<String> listOfTobeAddedToAbsent;//send

    private static ArrayList<String> listOfPlayersAlreadMarkedAsAbsent;//set on init team
    private static ArrayList<String> listOfAllUsersId;//set on init team

    private ArrayList<View> listOfClicked;//temp
    private ArrayList<View> listOfViews;//temp

    int countIndex = 0;


    public static ArrayList<String> getListOfAllUsersId() {
        return listOfAllUsersId;
    }

    public static void setListOfAllUsersId(ArrayList<String> listOfAllUsersId) {
        Team.listOfAllUsersId = listOfAllUsersId;
    }

    public static void setListOfPlayersAlreadMarkedAsAbsent(ArrayList<String> listOfPlayersAlreadMarkedAsAbsent) {
        Team.listOfPlayersAlreadMarkedAsAbsent = listOfPlayersAlreadMarkedAsAbsent;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (FragmentActivityInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement IFragmentToActivity");
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_team, container, false);
        isForCheckAbsence = false;
        listOfViews = new ArrayList<>();

        listOfTobeAddedToAbsent = new ArrayList<>();
        listOfClicked = new ArrayList<>();
        recyclerViewTeamRV = (RecyclerView) view.findViewById(R.id.recyclerViewTeam);

        fbDbRef = FirebaseDatabase.getInstance().getReference().child("PlayerPosts");
        fbDbUsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        adtoAbsentHolderRLayout = (RelativeLayout) view.findViewById(R.id.adtoAbsentHolderRLayout);
        button2AddToAbsentLists = (Button) view.findViewById(R.id.button2AddToAbsentLists);
        adtoAbsentHolderRLayout.setVisibility(view.GONE);

        button2AddToAbsentLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!listOfTobeAddedToAbsent.isEmpty()) {
                    mCallback.getmFragmentManager().popBackStack();

                    DataBaseHelperB dbha = new DataBaseHelperB(mCallback.getContext());
                    dbha.setAbsenceForArrayOfAbsentPlayerIds(listOfTobeAddedToAbsent, FullActivityInfo.getActivityType(), TrainerActivityRegistration.getSelectedActivityPostKey());
                } else {
                    Toast.makeText(mCallback.getContext(), R.string.noChangesToBeRegisteredToast, Toast.LENGTH_SHORT).show();
                }

            }
        });
        try {
            Bundle bundle = this.getArguments();
            isForCheckAbsence = bundle.getBoolean("absenceCheck");
            if (isForCheckAbsence) {
                adtoAbsentHolderRLayout.setVisibility(view.VISIBLE);
            }
        } catch (Exception e) {
            Log.e("Bundle::::", "isNull");
        }

        FirebaseRecyclerAdapter<UsersModel, Team.TeamViewHolder> teamAdapter = new FirebaseRecyclerAdapter<UsersModel, Team.TeamViewHolder>(
                UsersModel.class,
                R.layout.team_items,
                Team.TeamViewHolder.class,
                fbDbUsersRef
        ) {
            @Override
            protected void populateViewHolder(final TeamViewHolder viewHolder, UsersModel model, final int position) {
                try {
                    listOfViews.add(viewHolder.getView());
                    if (isForCheckAbsence) {
                        String currentPositionPlayerId = listOfAllUsersId.get(countIndex);
                        countIndex++;
                        if (listOfPlayersAlreadMarkedAsAbsent.contains(currentPositionPlayerId)) {
                            viewHolder.getAbsenceIndicatorImageView().setImageResource(R.drawable.x);
                        } else {
                            viewHolder.getAbsenceIndicatorImageView().setImageResource(R.drawable.check_black);
                        }
                    }
                    viewHolder.setProfileImage(mCallback.getContext(), model.getImage());
                    String nameAndDate = model.getFirstName() + " " + model.getLastName();
                    viewHolder.setUserNameLastName(nameAndDate);
                    viewHolder.setProfileImage(mCallback.getContext(), model.getImage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        try {
                            final String clickedPlayerId;
                            try {
                                int clickedPosition = listOfViews.indexOf(v);
                                clickedPlayerId = listOfAllUsersId.get(clickedPosition);
                                selectedUserIdInTeam = clickedPlayerId;
                            } catch (NullPointerException npee) {
                                Log.i("NPE!","Team not fully loaded yet!");
                                return;
                            }

                            if (isForCheckAbsence && !listOfClicked.contains(v) && !listOfPlayersAlreadMarkedAsAbsent.contains(clickedPlayerId)) {
                                viewHolder.getAbsenceIndicatorImageView().setImageResource(R.drawable.x);
                                listOfClicked.add(v);
                                listOfTobeAddedToAbsent.add(clickedPlayerId);//userIds

                            } else if (isForCheckAbsence && listOfClicked.contains(v)) {
                                viewHolder.getAbsenceIndicatorImageView().setImageResource(R.drawable.check_black);
                                listOfClicked.remove(v);
                                listOfTobeAddedToAbsent.remove(clickedPlayerId);//userIds
                            } else if (isForCheckAbsence && listOfPlayersAlreadMarkedAsAbsent.contains(clickedPlayerId)) {
                                Toast.makeText(mCallback.getContext(), R.string.playerAlreadyAbsent, Toast.LENGTH_SHORT).show();
                            } else {
                                DatabaseHelperC dbhc = new DatabaseHelperC(mCallback.getContext());
                                //dbhb.getProfileViewDataForUser(getRef(position).getKey()); sometimes firebase recycler return wrong index!
                                //derfor bruker vi egne maate aa finne posisjon av view paa!
                                dbhc.getProfileViewDataForUser(clickedPlayerId);
                            }
                        } catch (NullPointerException npe) {
                            Log.i("NPE!","Team not fully loaded yet!");
                            return;
                        }
                    }
                });
            }
        };

        recyclerViewTeamRV.setAdapter(teamAdapter);
        recyclerViewTeamRV.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        return view;
    }

    private static class TeamViewHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageView profileImage;
        TextView userNameLastName;
        ImageView absenceIndicatorIV;

        public TeamViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            absenceIndicatorIV = (ImageView) mView.findViewById(R.id.indicateBoxAbsence);
        }

        private ImageView getAbsenceIndicatorImageView() {
            return absenceIndicatorIV;
        }

        public View getView() {
            return mView;
        }

        private void setProfileImage(final Context ctx, final String image) {
            profileImage = (ImageView) mView.findViewById(R.id.userImageIV);
            Picasso.with(ctx)
                    .load(image)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(profileImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            //picture exist in cache
                        }

                        @Override
                        public void onError() {
                            Picasso.with(ctx)
                                    .load(image)
                                    .error(R.drawable.pph_s)//default profile place holder
                                    .into(profileImage);
                        }
                    });


        }

        private void setUserNameLastName(String firstLastName) {
            userNameLastName = (TextView) mView.findViewById(R.id.userFirstAndLastName);
            userNameLastName.setText(firstLastName);
        }
    }

}

