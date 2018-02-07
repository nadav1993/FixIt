package com.hadarfem.fixit.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hadarfem.fixit.R;
import com.hadarfem.fixit.dal.models.Problem;
import com.hadarfem.fixit.interfaces.IProblemActivity;
import com.hadarfem.fixit.models.User;
import com.hadarfem.fixit.validation.IValidator;
import com.hadarfem.fixit.validation.ProblemValidator;
import com.hadarfem.fixit.validation.ValidationResult;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ProblemFragment extends Fragment {
    public static final int COMPRESSION_QUALITY = 100;
    private final int IMAGE_CODE = 0;
    private final String IMAGE_DATA = "data";
    private final int CANCEL = 0;

    private User loggedUser;
    private Bitmap capturedImage;
    private IProblemActivity problemActivity;
    private IValidator<Problem> problemValidator;

    private Spinner citySpinner;
    private Spinner categorySpinner;
    private Button cameraButton;
    private ImageView cameraImageView;
    private static final String FIREBASE_CITIES = "cities";
    private static final String FIREBASE_CATEGORIES = "categories";

    public ProblemFragment() {
        // Required empty public constructor
        problemValidator = new ProblemValidator();
    }

    public static ProblemFragment newInstance(Context context, User loggedUser) {
        ProblemFragment fragment = new ProblemFragment();
        Bundle args = new Bundle();
        args.putSerializable(context.getString(R.string.intent_logged_user), loggedUser);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_problem, container, false);

        cameraButton = (Button) view.findViewById(R.id.cameraButton);
        citySpinner = (Spinner) view.findViewById(R.id.citySpinner);
        categorySpinner = (Spinner) view.findViewById(R.id.categorySpinner);
        cameraImageView = (ImageView) view.findViewById(R.id.cameraImage);
        getCities();
        getCategories();


        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPicture();
            }
        });

        view.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendProblem();
            }
        });

        view.findViewById(R.id.clearButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });

        return view;
    }

    private void getCities(){
        final List<String> cities = new ArrayList<String>();
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child(FIREBASE_CITIES);
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot citySnapshot : dataSnapshot.getChildren()) {
                    String cityName = citySnapshot.child("name").getValue(String.class);
                    cities.add(cityName);
                }


                ArrayAdapter<String> citiesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cities);
                citiesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                citySpinner.setAdapter(citiesAdapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});
    }

    private void getCategories(){
        final List<String> categories = new ArrayList<String>();
        DatabaseReference dr = FirebaseDatabase.getInstance().getReference().child(FIREBASE_CATEGORIES);
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String cityName = categorySnapshot.child("name").getValue(String.class);
                    categories.add(cityName);
                }


                ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categories);
                categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(categoriesAdapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }});
    }

    public void addPicture() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, IMAGE_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if were catching the camera result(bitmap)
        if (IMAGE_CODE == requestCode && resultCode != CANCEL) {
            ImageView cameraImageView = (ImageView) getView().findViewById(R.id.cameraImage);

            if (cameraImageView != null) {
                capturedImage = (Bitmap) data.getExtras().get(IMAGE_DATA);
                cameraImageView.setImageBitmap(capturedImage);

                cameraButton.setText(getString(R.string.camera_button_label_added));
            }
        }
    }


    public void sendProblem() {
        String description = ((EditText) getActivity().findViewById(R.id.descriptionProblemText)).getText().toString();
        String title = ((EditText) getActivity().findViewById(R.id.titleText)).getText().toString();

        Problem problem = new Problem().setId(UUID.randomUUID().toString()).setDate(new Date()).setTitle(title).
                setDescription(description).setUserName(loggedUser.getName()).setCity(citySpinner.getSelectedItem().toString()).setCategory(categorySpinner.getSelectedItem().toString());

        if (capturedImage != null) {
            problem.setPictureBase64(toBase64(capturedImage));
        }



        ValidationResult validationResult = problemValidator.validate(problem);

        if (!validationResult.isValid()) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(getActivity().getString(R.string.invalid_problem_title))
                    .setMessage(validationResult.getValidationString())
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            return;
        }

        problemActivity.saveProblem(problem);

        new AlertDialog.Builder(getActivity())
                .setTitle(getActivity().getString(R.string.success_title))
                .setMessage(getActivity().getString(R.string.report_success_message))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();

        clear();
    }

    public void clear() {
        EditText editTextDescription = (EditText) getView().findViewById(R.id.descriptionProblemText);
        EditText editTextTitle = (EditText) getView().findViewById(R.id.titleText);

        if (cameraImageView != null) {
            cameraImageView.setImageBitmap(null);
        }

        if (editTextDescription != null) {
            editTextDescription.setText("");
        }

        if (editTextTitle != null) {
            editTextTitle.setText("");
        }

        if (cameraButton != null) {
            cameraButton.setText(getString(R.string.camera_button_label));
        }

        capturedImage = null;

    }

    @Override
    public void onResume() {
        super.onResume();

        loggedUser = (User) getArguments().getSerializable(getString(R.string.intent_logged_user));



        if (capturedImage != null) {
            cameraButton.setText(getString(R.string.camera_button_label_added));
            cameraImageView.setImageBitmap(capturedImage);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (!(context instanceof IProblemActivity)) {
            throw new RuntimeException("Activity must implement IProblemActivity");
        }

        problemActivity = (IProblemActivity) context;
    }

    private String toBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESSION_QUALITY, outputStream);

        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        return encodedImage;
    }
}
