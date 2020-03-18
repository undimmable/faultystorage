package org.dimyriy.faultyserver.filesystem;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@ThreadSafe
public class FileSystem {
    @NotNull
    private final Map<String, byte[]> FS = new ConcurrentHashMap<>();
    @NotNull
    private final RwProtected rwProtected = new RwProtected();

    public boolean exists(@NotNull final String filename) {
        return rwProtected.read(() -> FS.containsKey(filename));
    }

    public boolean put(@NotNull final String filename, @NotNull final byte[] file) {
        return rwProtected.write(() -> FS.putIfAbsent(filename, file) == null);
    }

    public boolean putContent(@NotNull final String filename, @NotNull final String fileContent) {
        return put(filename, fileContent.getBytes(StandardCharsets.UTF_16));
    }

    @Nullable
    public byte[] get(@NotNull final String filename) {
        return rwProtected.read(() -> FS.get(filename));
    }

    @Nullable
    public String getContent(@NotNull final String filename) {
        final byte[] file = get(filename);
        return file != null ? new String(file, StandardCharsets.UTF_16) : null;
    }

    public boolean delete(@NotNull final String filename) {
        return rwProtected.write(() -> FS.remove(filename) != null);
    }

    @NotNull
    public List<String> list() {
        return rwProtected.read(() -> new ArrayList<>(FS.keySet()));
    }

    @ThreadSafe
    private static class RwProtected {
        private final ReadWriteLock RW = new ReentrantReadWriteLock();

        public <T> T read(@NotNull final Read<T> read) {
            RW.readLock().lock();
            try {
                return read.read();
            } finally {
                RW.readLock().unlock();
            }
        }

        public <T> T write(@NotNull final Write<T> write) {
            RW.writeLock().lock();
            try {
                return write.write();
            } finally {
                RW.writeLock().unlock();
            }
        }

        public interface Read<T> {
            T read();
        }

        public interface Write<T> {
            T write();
        }
    }
}
