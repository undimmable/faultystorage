package org.dimyriy.faultyserver.web;

import org.dimyriy.faultyserver.storage.FaultyStorage;
import org.dimyriy.faultyserver.storage.NewFaultyStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

import static org.dimyriy.faultyserver.validation.Constants.FILENAME_REGEX;

@RestController
@RequestMapping("/newStorage")
public class FaultyUploadController {
    @NotNull
    private final FaultyStorage faultyStorage;

    @Autowired
    public FaultyUploadController(@NotNull final NewFaultyStorage faultyStorage) {
        this.faultyStorage = faultyStorage;
    }

    @GetMapping(value = "files", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> list() {
        return faultyStorage.list();
    }

    @PostMapping(value = "files")
    public ResponseEntity<?> upload(@RequestParam("file") @NotNull(message = "'file' should be present") final MultipartFile file) {
        return faultyStorage.upload(file);
    }

    @NotNull
    @DeleteMapping("files/{filename}")
    public ResponseEntity<?> delete(@PathVariable
                                    @NotNull(message = "filename should be provided")
                                    @Pattern(regexp = FILENAME_REGEX, message = "filename is invalid") final String filename) {
        return faultyStorage.delete(filename);
    }

    @NotNull
    @GetMapping("files/{filename}")
    public ResponseEntity<Resource> download(@PathVariable
                                             @NotNull(message = "filename should be provided")
                                             @Pattern(regexp = FILENAME_REGEX, message = "filename is invalid") final String filename) {
        return faultyStorage.download(filename);
    }


}
