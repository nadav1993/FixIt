package com.hadarfem.fixit.dal.interfaces;

import java.util.List;

/**
 * Created by Tzach & Nadav on 2/6/2018.
 */

public interface IUpdater<T> {
    void update(List<T> entities);
}
