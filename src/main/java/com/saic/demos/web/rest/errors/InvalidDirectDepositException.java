package com.saic.demos.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InvalidDirectDepositException extends AbstractThrowableProblem {
  private static final long serialVersionUID = 1L;

  public InvalidDirectDepositException() {
    super(ErrorConstants.DEFAULT_TYPE, "Invalid direct deposit value", Status.BAD_REQUEST);
  }
}
