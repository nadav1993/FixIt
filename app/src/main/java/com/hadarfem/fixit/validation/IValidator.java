package com.hadarfem.fixit.validation;

/**
 * Created by Tzach & Nadav on 1/28/2018.
 */

public interface IValidator<T> {
    ValidationResult validate(T model);
}
