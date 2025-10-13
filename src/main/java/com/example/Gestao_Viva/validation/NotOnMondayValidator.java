package com.example.Gestao_Viva.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class NotOnMondayValidator implements ConstraintValidator<NotOnMonday, String> {

    @Override
    public boolean isValid(String dateString, ConstraintValidatorContext context) {
        if (dateString == null || dateString.isEmpty()) {
            return true;
        }
        try {
            LocalDate date = LocalDate.parse(dateString);
            return date.getDayOfWeek() != DayOfWeek.MONDAY;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}