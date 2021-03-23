package org.dimyriy.faultyserver.storage;

import org.dimyriy.faultyserver.faults.Throttler;
import org.dimyriy.faultyserver.filesystem.InMemoryFileSystem;
import org.dimyriy.faultyserver.util.Util;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

public class Storage {
    @NotNull
    private final Throttler throttler;
    @NotNull
    private final InMemoryFileSystem fs;

    public Storage(@NotNull final Throttler throttler, @NotNull final InMemoryFileSystem fs) {
        this.throttler = throttler;
        this.fs = fs;
    }

    @NotNull
    public List<String> list() {
        throttler.apply();
        return fs.list();
    }

    @NotNull
    public ResponseEntity<?> delete(@NotNull final String filename) {
        throttler.apply();
        if (!fs.delete(filename)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @NotNull
    public ResponseEntity<?> upload(@NotNull final MultipartFile file) {
        throttler.apply();
        final String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "filename should be present");
        }
        if (fs.exists(originalFilename)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "already exists");
        }
        try {
            final byte[] content = file.getBytes();
            throttler.tamper(content);
            if (!fs.put(originalFilename, content)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT);
            }
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "uploading failed");
        }
    }

    @NotNull
    public ResponseEntity<Resource> download(@NotNull final String filename) {
        throttler.apply();
        assertExists(filename);
        return ResponseEntity.ok(Util.asOctetStream(Util.getFile(filename, fs)));
    }

    private void assertExists(@NotNull final String filename) {
        if (!fs.exists(filename)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
