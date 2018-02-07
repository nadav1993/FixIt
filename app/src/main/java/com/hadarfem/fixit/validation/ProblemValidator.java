package com.hadarfem.fixit.validation;

import com.hadarfem.fixit.dal.models.Problem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tzach & Nadav on 2/6/2018.
 */

public class ProblemValidator implements IValidator<Problem> {
    @Override
    public ValidationResult validate(Problem model) {
        List<String> errors = new ArrayList<>();

        if (model.getUserName() == null) {
            errors.add("User Name cannot be null");
        } else if (model.getUserName().isEmpty()) {
            errors.add("User Name cannot be empty");
        }

        if (model.getDescription() == null) {
            errors.add("Description cannot be null");
        } else if (model.getDescription().isEmpty()) {
            errors.add("Description cannot be empty");
        }

        if (model.getTitle() == null) {
            errors.add("Title cannot be null");
        } else if (model.getTitle().isEmpty()) {
            errors.add("Title cannot be empty");
        }

        if (model.getDate() == null) {
            errors.add("Date cannot be null");
        }

        if (errors.isEmpty()) {
            return new ValidationResult().setValid(true);
        } else {
            return new ValidationResult().setValid(false).setValidationErrors(errors);
        }
    }
}
