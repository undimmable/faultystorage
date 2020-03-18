package org.dimyriy.faultyserver;

import org.dimyriy.faultyserver.faults.ProbabilityFunction;
import org.dimyriy.faultyserver.filesystem.FileGenerator;
import org.dimyriy.faultyserver.filesystem.OldFileSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Configuration
public class TestApplicationConfiguration {
    @NotNull
    @Bean
    public ProbabilityFunction probabilityFunction() {
        return new ProbabilityFunction() {
            @Override
            public boolean nextBoolean(@Min(0) @Max(100) int probability) {
                return false;
            }
        };
    }

    @NotNull
    @Bean
    public OldFileSystem oldFileSystem(@NotNull final FileGenerator fileGenerator) {
        return new OldFileSystem(fileGenerator, 0);
    }
}
