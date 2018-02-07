package com.hadarfem.fixit.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.hadarfem.fixit.R;
import com.hadarfem.fixit.adapters.ArrayBidAdapter;
import com.hadarfem.fixit.cache.IPictureCache;
import com.hadarfem.fixit.cache.MemoryPictureCache;
import com.hadarfem.fixit.interfaces.IBidsBoardActivity;
import com.hadarfem.fixit.interfaces.IBidFilterable;
import com.hadarfem.fixit.models.User;

public class BidsBoardFragment extends Fragment {
    private BaseAdapter bidAdapter;
    private IBidsBoardActivity forumActivity;
    private IPictureCache pictureCache;
    private User loggedUser;

    public BidsBoardFragment() {
        // Required empty public constructor
        pictureCache = new MemoryPictureCache();
    }

    public static BidsBoardFragment newInstance(Context context, User loggedUser) {
        BidsBoardFragment fragment = new BidsBoardFragment();
        Bundle args = new Bundle();
        args.putSerializable(context.getString(R.string.intent_logged_user), loggedUser);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bids_board, container, false);

        bidAdapter = new ArrayBidAdapter(getActivity(),
                view.findViewById(R.id.bidsList), view.findViewById(R.id.progressBar), pictureCache);

        ListView listView = (ListView) view.findViewById(R.id.bidsList);
        listView.setAdapter(bidAdapter);

        ((EditText) view.findViewById(R.id.searchText)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterBids(s.toString());
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        loggedUser = (User) getArguments().getSerializable(getString(R.string.intent_logged_user));
        bidAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof IBidsBoardActivity)) {
            throw new RuntimeException("Activity must implement IBidsBoardActivity");
        }

        forumActivity = (IBidsBoardActivity) context;
    }

    private void filterBids(String filter) {
        if (bidAdapter instanceof IBidFilterable) {
            ((IBidFilterable) bidAdapter).setFilter(filter);
        }
    }
}
