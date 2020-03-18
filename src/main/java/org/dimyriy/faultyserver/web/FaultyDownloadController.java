package org.dimyriy.faultyserver.web;

import org.dimyriy.faultyserver.storage.FaultyStorage;
import org.dimyriy.faultyserver.storage.OldFaultyStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

import static org.dimyriy.faultyserver.validation.Constants.FILENAME_REGEX;

@RestController
@RequestMapping("/oldStorage")
public class FaultyDownloadController {
    @NotNull
    private final FaultyStorage faultyStorage;

    @Autowired
    public FaultyDownloadController(@NotNull final OldFaultyStorage faultyStorage) {
        this.faultyStorage = faultyStorage;
    }

    @NotNull
    @GetMapping(value = "files", produces = MediaType.APPLICATION_JSON_VALUE)
    @Validated
    public List<String> list() {
        return faultyStorage.list();
    }

    @NotNull
    @GetMapping(value = "files/{filename}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Validated
    public ResponseEntity<Resource> download(
            @PathVariable
            @NotNull(message = "filename should be provided")
            @Pattern(regexp = FILENAME_REGEX, message = "filename is invalid") final String filename) {
        return faultyStorage.download(filename);
    }

    @NotNull
    @DeleteMapping("files/{filename}")
    public ResponseEntity<?> delete(
            @PathVariable
            @NotNull(message = "filename should be provided")
            @Pattern(regexp = FILENAME_REGEX, message = "filename is invalid") final String filename) {
        return faultyStorage.delete(filename);
    }
}
