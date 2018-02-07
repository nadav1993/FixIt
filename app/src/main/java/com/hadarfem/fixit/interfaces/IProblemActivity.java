package com.hadarfem.fixit.interfaces;

import com.hadarfem.fixit.dal.models.Problem;

/**
 * Created by Tzach & Nadav on 1/26/2018.
 */

public interface IProblemActivity {
    void saveProblem(Problem problem);
}
