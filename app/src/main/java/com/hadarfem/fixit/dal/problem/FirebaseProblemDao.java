package com.hadarfem.fixit.dal.problem;

import com.hadarfem.fixit.dal.interfaces.IProblemDao;
import com.hadarfem.fixit.dal.models.Problem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Tzach & Nadav on 2/6/2018.
 */

public class FirebaseProblemDao implements IProblemDao {
    private static final String FIREBASE_PROBLEMS = "problems";

    @Override
    public void addProblem(Problem problem) {
        getProblemsReference().child(problem.getId()).setValue(problem);
    }

    private DatabaseReference getProblemsReference() {
        return FirebaseDatabase.getInstance().getReference().child(FIREBASE_PROBLEMS);
    }
}
