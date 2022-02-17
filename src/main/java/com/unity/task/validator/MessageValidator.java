package com.unity.task.validator;

import com.unity.task.domain.Message;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * Custom message validator to validate the constraint.
 * "message" must be present, a JSON object, and have at least one field set
 */
public class MessageValidator implements ConstraintValidator<MessageValidation, Message> {

	@Override
	public boolean isValid(Message message, ConstraintValidatorContext constraintValidatorContext) {
		return message != null && (Objects.nonNull(message.getFoo()) || Objects.nonNull(message.getBar()));
	}
}
