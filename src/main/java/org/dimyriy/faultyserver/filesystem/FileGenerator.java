package org.dimyriy.faultyserver.filesystem;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class FileGenerator {
    @NotNull
    private final Lorem lorem = LoremIpsum.getInstance();
    @NotNull
    private final AtomicInteger nFiles = new AtomicInteger(0);

    public void generateFiles(@NotNull final InMemoryFileSystem inMemoryFileSystem, final int nFiles) {
        for (int i = 0; i < nFiles; i++) {
            generateFile(inMemoryFileSystem);
        }
    }

    public void generateFile(@NotNull final InMemoryFileSystem inMemoryFileSystem) {
        inMemoryFileSystem.put(nFiles.incrementAndGet() + ".txt", lorem.getWords(10, 100).getBytes());
    }
}
