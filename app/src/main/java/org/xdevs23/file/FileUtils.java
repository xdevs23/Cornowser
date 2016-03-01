package org.xdevs23.file;

import android.util.Log;

import org.xdevs23.debugutils.Logging;
import org.xdevs23.debugutils.StackTraceParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;

public class FileUtils {

    private FileUtils() {

    }

    private static void logt(String message) {
        Logging.logd("[FileUtils] " + message);
    }

    /**
     * Read a file
     * @param path Path to file
     * @param defaultValue Default value if file does not exist
     * @return Content of the file
     */
    public static String readFileString(String path, String defaultValue) {
        File file = new File(path);
        logt("Checking if file exists: " + path);
        if( file.exists() ) {
            String readResult = "";
            try {
                InputStream inputStream = new FileInputStream(file);
                logt("Reading file...");
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String readString;
                StringBuilder stringBuilder = new StringBuilder();

                while ( (readString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(readString).append("\n");
                }

                stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length() - 2);

                inputStream.close();
                logt("Read complete.");
                readResult = stringBuilder.toString();
                stringBuilder.delete(0, stringBuilder.length() - 1);
            } catch (FileNotFoundException e) {
                Log.e("readFile", "File not found: " + e.toString());
                readResult = "null;FileNotFoundException";
            } catch (IOException e) {
                Log.e("readFile", "Cannot read file (IOException): " + e.toString());
                readResult = "null;IOException";
            }

            return readResult;
        } else {
            return defaultValue;
        }
    }

    public static String[] readFileStringArray(String path) {
        File file = new File(path);
        if(file.exists()) {
            try {
                ArrayList<String> stringList = new ArrayList<String>();
                InputStream inputStream = new FileInputStream(file);
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(inputStream));

                String readString;
                while ( (readString = bufferedReader.readLine()) != null) {
                    stringList.add(readString);
                }

                inputStream.close();
                return stringList.toArray(new String[0]);
            } catch(Exception ex) {
                return new String[0];
            }
        } else {
            return new String[0];
        }
    }

    /**
     * Write a String to file
     * @param path Path to file
     * @param content Content of file
     * @return True if successful, false if not
     */
    public static boolean writeFileString(String path, String content) {
        try {
            File fileWrite = new File(path);
            File fileDir   = new File(fileWrite.toString().substring(0, path.lastIndexOf("/")));

            logt("Destination path: " + path);

            logt("Making directories...");
            fileDir.mkdirs();

            Writer writer = new BufferedWriter(new FileWriter(fileWrite));

            logt("Writing...");
            writer.write(content);
            logt("Closing...");
            writer.close();
            logt("Finished!");
            return true;
        } catch(Exception ex) {
            StackTraceParser.logStackTrace(ex);
            return false;
        }

    }

    /**
     * Copies a file
     * @param src Source file
     * @param dst Destination file
     * @throws IOException In/Out Exception
     */
    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        byte[] buf = new byte[8192];
        int len;
        while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
        in.close();
        out.close();
    }
}
