package org.dimyriy.faultyserver.faults;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.constraints.NotNull;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class FailureException extends RuntimeException {
    public FailureException(@NotNull final String message) {
        super(message);
    }

    @NotNull
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
