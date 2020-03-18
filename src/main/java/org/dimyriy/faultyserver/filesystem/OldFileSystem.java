package org.dimyriy.faultyserver.filesystem;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@PropertySource("classpath:fs.properties")
public class OldFileSystem extends FileSystem {
    public OldFileSystem(
            @NotNull final FileGenerator fileGenerator,
            @Value("${old.fs.size}") final int size) {
        fileGenerator.generateFiles(this, size);
    }
}
