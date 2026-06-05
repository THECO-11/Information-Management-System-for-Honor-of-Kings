package service;

import java.nio.file.Path;

public class FileStorageService {
    private final Path dataDirectory;

    public FileStorageService(Path dataDirectory) {
        this.dataDirectory = dataDirectory;
    }

    public void save(GameDataManager dataManager) {
        throw new UnsupportedOperationException("File saving is not implemented yet.");
    }

    public GameDataManager load() {
        throw new UnsupportedOperationException("File loading is not implemented yet.");
    }

    public Path getDataDirectory() {
        return dataDirectory;
    }
}
