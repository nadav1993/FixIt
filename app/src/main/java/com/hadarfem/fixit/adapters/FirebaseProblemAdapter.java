package com.hadarfem.fixit.adapters;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.hadarfem.fixit.R;
import com.hadarfem.fixit.dal.models.Problem;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by Tzach & Nadav on 1/24/2018.
 */

public class FirebaseProblemAdapter extends FirebaseListAdapter<Problem> {
    private static final String FIREBASE_PROBLEMS = "problems";
    private View listView;
    private View progressBar;

    public FirebaseProblemAdapter(Activity activity, View listView, View progressBar) {
        super(activity, Problem.class, R.layout.problem_row,
                FirebaseDatabase.getInstance().getReference().child(FIREBASE_PROBLEMS).orderByChild("time"));

        this.listView = listView;
        this.progressBar = progressBar;
    }

    @Override
    protected void populateView(View rowView, Problem problem, int position) {
        if (listView.getVisibility() == View.INVISIBLE) {
            listView.setVisibility(View.VISIBLE);
        }

        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.INVISIBLE);
        }

        TextView titleView = (TextView) rowView.findViewById(R.id.problemTitle);
        TextView cityView = (TextView) rowView.findViewById(R.id.problemCity);
        TextView categoryView = (TextView) rowView.findViewById(R.id.problemCategory);
        TextView dateView = (TextView) rowView.findViewById(R.id.date);

        if (titleView != null) {
            titleView.setText(problem.getTitle());
        }

        if (cityView != null) {
            cityView.setText(problem.getCity());
        }

        if (categoryView != null) {
            categoryView.setText(problem.getCategory());
        }

        if (dateView != null && problem.getDate() != null) {
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
            String dateString = dateFormat.format(problem.getDate());
            dateView.setText(dateString);
        }
    }
}
