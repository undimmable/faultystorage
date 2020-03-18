package org.dimyriy.faultyserver.faults;


import javax.annotation.Nullable;

public interface Failer {
    void apply() throws FailureException;

    void tamper(@Nullable final byte[] content);
}
