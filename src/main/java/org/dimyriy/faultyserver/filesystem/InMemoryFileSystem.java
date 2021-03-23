package org.dimyriy.faultyserver.filesystem;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@ThreadSafe
public class InMemoryFileSystem {
    @NotNull
    private final Map<String, byte[]> files = new ConcurrentHashMap<>();
    @NotNull
    private final RwProtected rwProtected = new RwProtected();

    public boolean exists(@NotNull final String filename) {
        return rwProtected.read(() -> files.containsKey(filename));
    }

    public boolean put(@NotNull final String filename, @NotNull final byte[] file) {
        return rwProtected.write(() -> files.putIfAbsent(filename, file) == null);
    }

    @Nullable
    public byte[] get(@NotNull final String filename) {
        return rwProtected.read(() -> files.get(filename));
    }

    public boolean delete(@NotNull final String filename) {
        return rwProtected.write(() -> files.remove(filename) != null);
    }

    @NotNull
    public List<String> list() {
        return rwProtected.read(() -> new ArrayList<>(files.keySet()));
    }

    public void clear() {
        rwProtected.write(() -> {
            files.clear();
            return null;
        });
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
