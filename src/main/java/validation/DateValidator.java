package validation;

import com.example.internshipapp.mapper.DateMapper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.format.DateTimeParseException;

public class DateValidator implements ConstraintValidator<ValidDate, String> {

    @Override
    public void initialize(ValidDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String date, final ConstraintValidatorContext context) {
        DateMapper dateMapper = new DateMapper();
        try {
            dateMapper.stringToLocalDateTime(date);
            return true;
        } catch (DateTimeParseException | NullPointerException e) {
            return false;
        }
    }
}
