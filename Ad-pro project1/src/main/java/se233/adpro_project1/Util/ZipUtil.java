package se233.adpro_project1.Util;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.*;

public class ZipUtil {

    public static List<File> extractImages(File zipFile) throws IOException {
        List<File> images = new ArrayList<>();
        Path tempDir = Files.createTempDirectory("vectorizer_");
        tempDir.toFile().deleteOnExit();

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String name = entry.getName().toLowerCase();
                if (entry.isDirectory() || !(name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"))) {
                    continue;
                }

                Path target = tempDir.resolve(Paths.get(entry.getName()).getFileName().toString());
                Files.copy(zis, target, StandardCopyOption.REPLACE_EXISTING);
                target.toFile().deleteOnExit();
                images.add(target.toFile());
            }
        }
        return images;
    }
}
