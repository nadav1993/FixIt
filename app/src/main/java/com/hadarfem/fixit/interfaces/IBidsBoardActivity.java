package com.hadarfem.fixit.interfaces;

import com.hadarfem.fixit.dal.interfaces.IUpdater;
import com.hadarfem.fixit.dal.models.Bid;

/**
 * Created by Tzach & Nadav on 1/24/2018.
 */

public interface IBidsBoardActivity {

    void savePost(Bid bid);

    void registerForBids(IUpdater<Bid> updater);
}
