package org.dimyriy.faultyserver.util;

import org.dimyriy.faultyserver.filesystem.InMemoryFileSystem;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

public final class Util {
    private Util() {
    }

    @NotNull
    public static InputStreamResource asOctetStream(@NotNull final byte[] content) {
        try (final ByteArrayInputStream inputStream = new ByteArrayInputStream(content)) {
            return new InputStreamResource(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static byte[] getFile(@NotNull final String filename, @NotNull InMemoryFileSystem fs) {
        return Optional.ofNullable(fs.get(filename))
                       .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @NotNull
    public static InputStreamResource getAsResource(@NotNull final String filename, @NotNull final InMemoryFileSystem inMemoryFileSystem) {
        return asOctetStream(getFile(filename, inMemoryFileSystem));
    }
}
