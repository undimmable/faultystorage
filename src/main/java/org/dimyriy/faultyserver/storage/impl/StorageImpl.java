package org.dimyriy.faultyserver.storage.impl;

import org.dimyriy.faultyserver.faults.Throttler;
import org.dimyriy.faultyserver.filesystem.impl.InMemoryInMemoryFileSystemImpl;
import org.dimyriy.faultyserver.storage.Storage;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class StorageImpl extends Storage {
    public StorageImpl(@NotNull final Throttler throttler, @NotNull final InMemoryInMemoryFileSystemImpl fs) {
        super(throttler, fs);
    }
}
