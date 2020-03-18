package org.dimyriy.faultyserver.storage;

import org.dimyriy.faultyserver.faults.Failer;
import org.dimyriy.faultyserver.filesystem.OldFileSystem;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class OldFaultyStorage extends FaultyStorage {
    public OldFaultyStorage(@NotNull final Failer failer, @NotNull final OldFileSystem fs) {
        super(failer, fs);
    }
}
