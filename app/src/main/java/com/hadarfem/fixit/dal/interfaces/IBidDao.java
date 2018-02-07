package com.hadarfem.fixit.dal.interfaces;

import com.hadarfem.fixit.dal.models.Bid;

/**
 * Created by Tzach & Nadav on 1/24/2018.
 */

public interface IBidDao {
    void addBid(Bid bid);

    void registerForUpdates(IUpdater<Bid> updater);
}
