package com.appamedix.makula.worker.filemanage;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipManager {
    private static final int BUFFER_SIZE = 4096;

    /**
     * Compresses a collection of files to a destination zip file.
     *
     * @param files: A collection of files.
     * @param zipFileName: The path of the zip file.
     */
    public void zip(String[] files, String zipFileName) {
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFileName);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[BUFFER_SIZE];

            for (String file : files) {
                Log.v("Compress", "Adding: " + file);
                FileInputStream fi = new FileInputStream(file);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);

                ZipEntry entry = new ZipEntry(file.substring(file.lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Copies a zip file to the destination directory.
     *
     * @param zipFile: The path of the zip file to import.
     * @param targetLocation: The destination directory.
     */
    public void unzip(String zipFile, String targetLocation) {
        // Create target location folder if not exist.
        dirChecker(targetLocation);

        try {
            FileInputStream fin = new FileInputStream(zipFile);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            while ((ze = zin.getNextEntry()) != null) {
                // Create dir if required while unzipping.
                if (ze.isDirectory()) {
                    dirChecker(ze.getName());
                } else {
                    FileOutputStream fout = new FileOutputStream(targetLocation + "/" + ze.getName());
                    for (int c = zin.read(); c != -1; c = zin.read()) {
                        fout.write(c);
                    }

                    zin.closeEntry();;
                    fout.close();
                }
            }
            zin.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Creates a directory to the specific location.
     *
     * @param dir: The directory path to create.
     */
    private void dirChecker(String dir) {
        File file = new File(dir);
        if (!file.isDirectory()) {
            file.mkdirs();
        }
    }
}
