package org.dimyriy.faultyserver.storage;

import org.dimyriy.faultyserver.faults.Failer;
import org.dimyriy.faultyserver.filesystem.FileSystem;
import org.dimyriy.faultyserver.util.Util;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;

public class FaultyStorage {
    @NotNull
    private final Failer failer;
    @NotNull
    private final FileSystem fs;

    public FaultyStorage(@NotNull final Failer failer, @NotNull final FileSystem fs) {
        this.failer = failer;
        this.fs = fs;
    }

    @NotNull
    public List<String> list() {
        failer.apply();
        return fs.list();
    }

    @NotNull
    public ResponseEntity<?> delete(@NotNull final String filename) {
        failer.apply();
        if (!fs.delete(filename)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok().build();
        }
    }

    @NotNull
    public ResponseEntity<?> upload(@NotNull final MultipartFile file) {
        failer.apply();
        final String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "filename should be present");
        }
        if (fs.exists(originalFilename)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "already exists");
        }
        try {
            final byte[] content = file.getBytes();
            failer.tamper(content);
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
        failer.apply();
        assertExists(filename);
        return ResponseEntity.ok(Util.asOctetStream(Util.getFile(filename, fs)));
    }

    private void assertExists(@NotNull final String filename) {
        if (!fs.exists(filename)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
