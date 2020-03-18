package org.dimyriy.faultyserver.faults;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Component
@PropertySource("classpath:failure.properties")
public class PossibleFailer implements Failer {
    @NotNull
    private static final Logger LOGGER = LoggerFactory.getLogger(PossibleFailer.class);
    @NotNull
    private final ProbabilityFunction probabilityFunction;
    @NotNull
    private final Tamperer tamperer;
    private final int failureProbability;
    private final int hangProbability;
    private final long hangTimeMs;
    private final int tamperingProbability;

    @Autowired
    public PossibleFailer(@NotNull ProbabilityFunction probabilityFunction,
                          @NotNull Tamperer tamperer,
                          @Value("${failure.probability}") final int failureProbability,
                          @Value("${hang.probability}") final int hangProbability,
                          @Value("${hang.timeMs}") final long hangTimeMs,
                          @Value("${tampering.probability}") final int tamperingProbability) {
        this.failureProbability = failureProbability;
        LOGGER.info("Failure probability is set to {}", this.failureProbability);
        this.hangProbability = hangProbability;
        LOGGER.info("Hang probability is set to {}", this.hangProbability);
        this.hangTimeMs = hangTimeMs;
        LOGGER.info("Hang time is set to {}", this.hangTimeMs);
        this.tamperingProbability = tamperingProbability;
        LOGGER.info("Tampering probability is set to {}", this.tamperingProbability);
        this.probabilityFunction = probabilityFunction;
        this.tamperer = tamperer;
    }

    @Override
    public void apply() throws FailureException {
        possiblyFail();
        possiblyHang();
    }

    @NotNull
    @Override
    public void tamper(@Nullable byte[] content) {
        if (content == null) {
            return;
        }
        LOGGER.debug("Should I tamper?...");
        if (probabilityFunction.nextBoolean(tamperingProbability)) {
            LOGGER.info("...fortune teller says you loose, tampering!");
            tamperer.tamper(content);
        } else {
            LOGGER.debug("...not this time!");
        }
    }

    private void possiblyHang() {
        LOGGER.debug("Should I hang?...");
        if (probabilityFunction.nextBoolean(hangProbability)) {
            LOGGER.info("...fortune teller says you loose, hanging...");
            try {
                Thread.sleep(hangTimeMs);
            } catch (final InterruptedException e) {

                Thread.currentThread().interrupt();
            }
            LOGGER.info("...enough hanging!");
        } else {
            LOGGER.debug("...not this time!");
        }
    }

    private void possiblyFail() {
        LOGGER.debug("Should I fail?...");
        if (probabilityFunction.nextBoolean(failureProbability)) {
            LOGGER.info("...fortune teller says you loose, failing!");
            throw new FailureException("Unexpectedly failed");
        } else {
            LOGGER.debug("...not this time!");
        }
    }
}
