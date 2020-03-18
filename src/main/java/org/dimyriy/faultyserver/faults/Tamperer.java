package org.dimyriy.faultyserver.faults;

import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Random;

@Service
public class Tamperer {
    private final Random RANDOM = new Random();

    public void tamper(@NotNull final byte[] original) {
        RANDOM.nextBytes(original);
    }
}
