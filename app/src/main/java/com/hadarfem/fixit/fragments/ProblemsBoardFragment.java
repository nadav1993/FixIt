package com.hadarfem.fixit.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.hadarfem.fixit.R;
import com.hadarfem.fixit.activities.SuggestPriceAcivity;
import com.hadarfem.fixit.adapters.FirebaseProblemAdapter;
import com.hadarfem.fixit.dal.models.Problem;
import com.hadarfem.fixit.models.User;

public class ProblemsBoardFragment extends Fragment {
    private BaseAdapter problemAdapter;
    private User loggedUser;

    public ProblemsBoardFragment() {
        // Required empty public constructor
    }


    public static ProblemsBoardFragment newInstance(Context context, User loggedUser) {
        ProblemsBoardFragment fragment = new ProblemsBoardFragment();
        Bundle args = new Bundle();
        args.putSerializable(context.getString(R.string.intent_logged_user), loggedUser);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_problems_board, container, false);
        loggedUser = (User) getArguments().getSerializable(getString(R.string.intent_logged_user));
        problemAdapter = new FirebaseProblemAdapter(getActivity(),
                view.findViewById(R.id.problemList), view.findViewById(R.id.progressBar));

        final ListView listView = (ListView) view.findViewById(R.id.problemList);

        listView.setAdapter(problemAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object listItem = listView.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), SuggestPriceAcivity.class);

                Problem p = (Problem)problemAdapter.getItem(position);
                intent.putExtra("PROBLEM_CITY", p.getCity());
                intent.putExtra("PROBLEM_CATEGORY", p.getCategory());
                intent.putExtra("PROBLEM_TITLE", p.getTitle());
                intent.putExtra("PROBLEM_DESCRIPTION", p.getDescription());
                intent.putExtra("PROBLEM_PICTURE", p.getPictureBase64());
                intent.putExtra("BIDDER_USERNAME", loggedUser.getName());
                intent.putExtra("COSTUMER_USERNAME", p.getUserName());
                intent.putExtra("URL_USERNAME", loggedUser.getProfilePictureUrl());
                startActivity(intent);

            }
        });

        return view;
    }
}
