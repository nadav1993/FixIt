package com.hadarfem.fixit.validation;

import java.util.List;

/**
 * Created by Tzach & Nadav on 2/6/2018.
 */

public class ValidationResult {
    private boolean isValid;
    private List<String> validationErrors;

    public boolean isValid() {
        return isValid;
    }

    public ValidationResult setValid(boolean valid) {
        isValid = valid;
        return this;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    public ValidationResult setValidationErrors(List<String> validationErrors) {
        this.validationErrors = validationErrors;
        return this;
    }

    public String getValidationString() {
        if (isValid()) {
            return "OK";
        } else {
            StringBuilder stringBuilder = new StringBuilder();

            for (String error : validationErrors) {
                stringBuilder.append(error);
                stringBuilder.append("\n");
            }

            return stringBuilder.toString();
        }
    }
}
