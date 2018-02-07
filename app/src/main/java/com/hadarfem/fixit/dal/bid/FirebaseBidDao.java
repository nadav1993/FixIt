package com.hadarfem.fixit.dal.bid;

import com.hadarfem.fixit.dal.interfaces.IBidDao;
import com.hadarfem.fixit.dal.interfaces.IUpdater;
import com.hadarfem.fixit.dal.models.Bid;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tzach & Nadav on 1/24/2018.
 */

public class FirebaseBidDao implements IBidDao {
    private static final String FIREBASE_BIDS = "bids";
    private String loggedUserName;

    public FirebaseBidDao(String loggedUserName)
    {
        this.loggedUserName = loggedUserName;
    }

    public FirebaseBidDao()
    {
    }

    @Override
    public void addBid(Bid bid) {
        getBidsReference().child(bid.getId()).setValue(bid);
    }

    @Override
    public void registerForUpdates(final IUpdater<Bid> updater) {
        getBidsReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Bid> bidList = new ArrayList<>();

                for (DataSnapshot entry : dataSnapshot.getChildren()) {
                    Bid bid = entry.getValue(Bid.class);

                    if (bid.getCostumerUserName().equals(loggedUserName))
                    {
                        bidList.add(bid);
                    }
                }

                updater.update(bidList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private DatabaseReference getBidsReference() {
        return FirebaseDatabase.getInstance().getReference().child(FIREBASE_BIDS);
    }
}
