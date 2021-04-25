package com.mediscreen.patient.config;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.LengthRule;
import org.passay.PasswordValidator;
import org.passay.PasswordData;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword arg0) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        PasswordValidator validator = new PasswordValidator(Arrays.asList(
                new LengthRule(1, 125)))                                     // Entre 8 et 125 caractères
               /* new CharacterRule(EnglishCharacterData.UpperCase, 1),     // Au moins une majuscule
                new CharacterRule(EnglishCharacterData.Digit, 1),          // Au moins un chiffre
                new CharacterRule(EnglishCharacterData.Special, 1),        // Au moins un caractère spécial
                new WhitespaceRule()))*/
                ;

        RuleResult result = validator.validate(new PasswordData(password));
        if (result.isValid()) {
            return true;
        }
        List<String> messages = validator.getMessages(result);

        String messageTemplate = messages.stream().collect(Collectors.joining(","));
        if (context != null) {
            context.buildConstraintViolationWithTemplate(messageTemplate).addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }
        return false;
    }
}
