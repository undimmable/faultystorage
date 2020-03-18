package org.dimyriy.faultyserver.faults;

import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Random;

@Service
public class ProbabilityFunction {
    @NotNull
    private final Random random = new Random();

    public boolean nextBoolean(@Min(0)
                               @Max(100) final int probability) {
        return random.nextInt(100) < probability;
    }
}
