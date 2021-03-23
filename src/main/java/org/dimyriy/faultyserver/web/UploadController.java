package org.dimyriy.faultyserver.web;

import org.dimyriy.faultyserver.storage.Storage;
import org.dimyriy.faultyserver.storage.impl.StorageImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

import static org.dimyriy.faultyserver.validation.Constants.FILENAME_REGEX;

@RestController
@RequestMapping("/storage")
public class UploadController {
    @NotNull
    private final Storage storage;

    @Autowired
    public UploadController(@NotNull final StorageImpl faultyStorage) {
        this.storage = faultyStorage;
    }

    @GetMapping(value = "files", produces = MediaType.APPLICATION_JSON_VALUE)
    @Validated
    public List<String> list() {
        return storage.list();
    }

    @PostMapping(value = "files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Validated
    public ResponseEntity<?> upload(@RequestParam("file") @NotNull(message = "'file' should be present") final MultipartFile file) {
        return storage.upload(file);
    }

    @NotNull
    @DeleteMapping(value = "files/{filename}")
    @Validated
    public ResponseEntity<?> delete(@PathVariable
                                    @NotNull(message = "filename should be provided")
                                    @Pattern(regexp = FILENAME_REGEX, message = "filename is invalid") final String filename) {
        return storage.delete(filename);
    }

    @NotNull
    @GetMapping(value = "files/{filename}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @Validated
    public ResponseEntity<Resource> download(@PathVariable
                                             @NotNull(message = "filename should be provided")
                                             @Pattern(regexp = FILENAME_REGEX, message = "filename is invalid") final String filename) {
        return storage.download(filename);
    }


}
