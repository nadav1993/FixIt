package com.hadarfem.fixit.validation;

import com.hadarfem.fixit.dal.models.Bid;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tzach & Nadav on 1/28/2018.
 */

public class BidValidator implements IValidator<Bid> {
    @Override
    public ValidationResult validate(Bid model) {
        List<String> errors = new ArrayList<>();

        if (model.getPrice() == null) {
            errors.add("Price cannot be null");
        } else if (model.getPrice().isEmpty()) {
            errors.add("Price cannot be empty");
        }
        else if (!isNumeric(model.getPrice())) {
            errors.add("Price cannot be string");
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

    public boolean isNumeric(String str)
    {
        try
        {
            int d = Integer.parseInt(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}
