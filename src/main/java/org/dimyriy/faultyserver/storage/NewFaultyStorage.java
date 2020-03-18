package org.dimyriy.faultyserver.storage;

import org.dimyriy.faultyserver.faults.Failer;
import org.dimyriy.faultyserver.filesystem.NewFileSystem;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
public class NewFaultyStorage extends FaultyStorage {
    public NewFaultyStorage(@NotNull final Failer failer, @NotNull final NewFileSystem fs) {
        super(failer, fs);
    }
}
