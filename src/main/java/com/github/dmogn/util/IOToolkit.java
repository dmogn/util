/* 
 * OldPortal Utilites Library is available under the MIT License. See http://opensource.org/licenses/MIT for full text.
 *
 * Copyright (C) Dmitry Ognyannikov, 2005
 */
package com.github.dmogn.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Vector;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.*;

/**
 *
 * @author Dmitry Ognyannikov
 */
public class IOToolkit {

    /**
     * Creates a new instance of IOToolkit
     */
    public IOToolkit() {
    }

    public static String getRelativePath(File fileFrom, File fileTo, String separator) {
        // from file directories path:
        Vector<File> fileFromParents = new Vector<File>();
        if (fileFrom.isDirectory()) {
            fileFromParents.add(fileFrom);
        }

        File fromParent = fileFrom.getParentFile();

        while (fromParent != null) {
            fileFromParents.add(0, fromParent);
            fromParent = fromParent.getParentFile();
        }

        if (fileFromParents.size() == 0) {
            return null;
        }

        // to file directories path:
        Vector<File> fileToParents = new Vector<File>();
        if (fileTo.isDirectory()) {
            fileToParents.add(fileTo);
        }

        File toParent = fileTo.getParentFile();

        while (toParent != null) {
            fileToParents.add(0, toParent);
            toParent = toParent.getParentFile();
        }

        if (fileToParents.size() == 0) {
            return null;
        }

        // compare paths and find shared parent directory:
        int pathLastEqualIndex = -1;

        for (int i = 0; i < fileFromParents.size() && i < fileToParents.size(); i++) {
            File fromParentDir = fileFromParents.get(i);
            File toParentDir = fileToParents.get(i);
            if (fromParentDir.getAbsolutePath().equals(toParentDir.getAbsolutePath())) {
                pathLastEqualIndex = i;
            } else {
                break;
            }
        }

        String path = "";

        int stepsBack = fileFromParents.size() - pathLastEqualIndex - 1;

        if (stepsBack == 0) {
            path = "." + separator;
        } else {
            for (int i = 0; i < stepsBack; i++) {
                path += ".." + separator;
            }
        }

        for (int i = pathLastEqualIndex + 1; i < fileToParents.size(); i++) {
            path += fileToParents.get(i).getName() + separator;
        }

        path += fileTo.getName();

        return path;
    }

    public static String getFileExtension(String fileName) {
        for (int i = fileName.length() - 1; i >= 0; i--) {
            if (fileName.charAt(i) == '.') {
                if (i < fileName.length() - 1) {
                    return fileName.substring(i + 1);
                } else {
                    return "";
                }
            }

            if (fileName.charAt(i) == File.separatorChar) {
                return "";
            }

            if (fileName.charAt(i) == '/') {
                return "";
            }
        }
        return "";
    }

    public static Strings loadTextFile(File file, String charsetName) {
        String fileText = loadFile(file, charsetName);
        return new Strings(fileText);
    }

    public static Strings loadTextFile(File file) {
        return loadTextFile(file, Charset.defaultCharset().name());
    }

    public static boolean saveTextFile(File file, Strings text, String charsetName) {
        return writeFile(file.getAbsolutePath(), text.toString(), charsetName);
    }

    public static boolean saveTextFile(File file, Strings text, String charsetName, String stringEndTemplate) {
        return writeFile(file.getAbsolutePath(), text.toString(), charsetName, stringEndTemplate);
    }

    public static boolean saveTextFile(File file, Strings text) {
        return writeFile(file.getAbsolutePath(), text.toString(), Charset.defaultCharset().name(), "\n");
//        try{
//        Strings ret = new Strings();
//
//        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
//
//        for (int i=0; i<text.size(); i++)
//        {
//            writer.write(text.get(i));
//            if (i<(text.size()-1))
//                writer.newLine();
//        }
//
//        writer.close();
//
//        return true;
//        } catch (Exception ex)
//        {
//            ex.printStackTrace();
//            return false;
//        }
    }

    /**
     * Create text string from text file.
     */
    public static String loadFile(File file, String charsetName) {
        return loadFile(file.getAbsolutePath(), charsetName);
    }

    /**
     * Create text string from text file.
     */
    public static String loadFile(String filename, String charsetName) {
        try {
            String rawFileText = loadRawFile(filename, charsetName);

            if (rawFileText == null) {
                return null;
            }

            char rawTextArray[] = rawFileText.toCharArray();

            // detect string end:
            boolean haveCarriageReturn = false;
            for (int i = 0; i < rawTextArray.length; i++) {
                if (rawTextArray[i] == '\r') {
                    haveCarriageReturn = true;
                    break;
                }
            }

            boolean haveLineFeed = false;
            for (int i = 0; i < rawTextArray.length; i++) {
                if (rawTextArray[i] == '\n') {
                    haveLineFeed = true;
                    break;
                }
            }

            if (haveCarriageReturn == false) {// UNIX text file
                return rawFileText;
            } else if (haveCarriageReturn && haveLineFeed) {// Windows text file
                StringBuilder ret = new StringBuilder(rawTextArray.length);
                for (int i = 0; i < rawTextArray.length; i++) {
                    if (rawTextArray[i] != '\r') {
                        ret.append(rawTextArray[i]);
                    }
                }
                return ret.toString();
            } else {// Mac text file
                StringBuilder ret = new StringBuilder(rawTextArray.length);
                for (int i = 0; i < rawTextArray.length; i++) {
                    if (rawTextArray[i] != '\r') {
                        ret.append(rawTextArray[i]);
                    } else {
                        ret.append('\n');
                    }
                }
                return ret.toString();
            }
        } catch (Exception ex) {
            return null;
        }
    }

    public static String loadRawFile(String filename, String charsetName) {
        try {
            InputStream in = new FileInputStream(filename);
            // charset settings:
            Charset charset = Charset.forName(charsetName);

            StringBuilder ret = new StringBuilder(in.available());

            InputStreamReader reader = new InputStreamReader(in, charset);

            char buffer[] = new char[16384];
            int len = 0;
            while ((len = reader.read(buffer, 0, buffer.length)) >= 0) {
                ret.append(buffer, 0, len);
            }
            reader.close();
            in.close();
            return ret.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Create text file from string with defined string end symbols.
     */
    public static boolean writeFile(File file, String string, String stringEncoding, String stringEndTemplate) {
        return writeFile(file.getAbsolutePath(), string, stringEncoding, stringEndTemplate);
    }

    /**
     * Create text file from string with defined string end symbols.
     */
    public static boolean writeFile(String filename, String string, String stringEncoding, String stringEndTemplate) {
        try {
            // charset settings:
            Charset charset = Charset.forName(stringEncoding);

            File outFile = new File(filename);
            if (!outFile.exists()) {
                File parentDir = outFile.getParentFile();
                if (parentDir != null) {
                    if (!parentDir.exists()) {
                        parentDir.mkdirs();
                    }

                    if (!outFile.createNewFile()) {
                        return false;
                    }
                }
            }
            if (!outFile.canWrite()) {
                return false;
            }

            OutputStream out = new FileOutputStream(outFile);
            com.github.dmogn.util.Strings strings = new com.github.dmogn.util.Strings();
            strings.loadFromString(string);
            for (int i = 0; i < strings.size(); i++) {
                String line = strings.get(i);

                out.write(toBytes(charset, line));

                if (i < (strings.size() - 1)) {
                    out.write(toBytes(charset, stringEndTemplate));
                }
            }
            out.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean writeFile(String filename, String string, String stringEncoding) {
        try {
            File outFile = new File(filename);
            if (!outFile.exists()) {
                File parentDir = outFile.getParentFile();
                if (parentDir != null) {
                    if (!parentDir.exists()) {
                        parentDir.mkdirs();
                    }

                    if (!outFile.createNewFile()) {
                        return false;
                    }
                }
            }
            if (!outFile.canWrite()) {
                return false;
            }

            OutputStream out = new FileOutputStream(outFile);
            byte array[] = string.getBytes(stringEncoding);
            out.write(array);
            out.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    static private byte[] toBytes(Charset charset, String string) {
        ByteBuffer buffer = charset.encode(string);
        return Arrays.copyOf(buffer.array(), string.length());
    }

    static public String getFileMD5(File file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            FileInputStream in = new FileInputStream(file);
            byte buf1[] = new byte[1];
            byte buf16[] = new byte[16];
            while (in.available() > 0) {
                if (in.available() >= 16) {
                    in.read(buf16);
                    digest.update(buf16);
                } else {
                    in.read(buf1);
                    digest.update(buf1);
                }
            }
            byte result[] = digest.digest();
            in.close();

            // convert digest result to HEX string
            return StringToolkit.toHexString(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    static public String getFileSHA(File file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA");
            digest.reset();
            FileInputStream in = new FileInputStream(file);
            byte buf1[] = new byte[1];
            byte buf16[] = new byte[16];
            while (in.available() > 0) {
                if (in.available() >= 16) {
                    in.read(buf16);
                    digest.update(buf16);
                } else {
                    in.read(buf1);
                    digest.update(buf1);
                }
            }
            byte result[] = digest.digest();
            in.close();

            // convert digest result to HEX string
            return StringToolkit.toHexString(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * return true if files content is equal
     */
    static public boolean compareFilesByContent(File file1, File file2) {// Сначала сравниваем файлы по длине, потом побайтно
        if (file1.length() != file2.length()) {
            return false;
        }

        try {
            FileInputStream in1 = new FileInputStream(file1);
            FileInputStream in2 = new FileInputStream(file2);

            while (in1.available() > 0) {
                if (in1.read() != in2.read()) {
                    return false;
                }
            }

            in1.close();
            in2.close();

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean downloadFile(String urlString, String localFilename) {
        try {
            FileOutputStream fout = new FileOutputStream(localFilename);
            boolean ret = downloadFile(urlString, fout);
            fout.close();
            return ret;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean downloadFile(String urlString, OutputStream out) {
        HttpURLConnection conn = null;
        try {
            /**
             * ****** Формирование запроса text/plain *******
             */
            String request = "";

            //Logger.getLogger(EPortConnection.class).debug("Start process payment: "+request);
            /**
             * ****** устанавливаем HTTP - соединение  *******
             */
            System.out.println("try open URL");
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            //conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            String srvCharset = "windows-1251";

            // преобразуем запрос в данные в соответствующей кодировке
            byte[] data = request.getBytes(srvCharset);

            // указываем кодировку сообщения и формат данных
            //String ctypeHeader = "text/plain; charset=" + srvCharset;
            //conn.addRequestProperty("Content-Type", ctypeHeader);
            /**
             * ****** отправляем запрос на сервер *******
             */
            OutputStream os = conn.getOutputStream();
            os.write(data, 0, data.length);
            os.close();

            /**
             * ****** обрабатываем ответ сервера *******
             */
            InputStreamReader ir;
            BufferedReader br;
            String line;

            // http-код ответа указывает на ошибку
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                // в случае ошибки читаем текст с комментарием
                System.out.println("HTTP ERROR " + conn.getResponseCode());
                System.out.println(conn.getResponseMessage());
                //System.err.println("HTTP ERROR " + conn.getResponseCode());
                //System.err.println(conn.getResponseMessage());
                ir = new InputStreamReader(conn.getErrorStream());
                br = new BufferedReader(ir);
                while ((line = br.readLine()) != null) {
                    System.err.println(line);
                }
                br.close();
                conn.disconnect();
                return false;
            } else {// http-код ответа 200
                System.out.println("ResponseMessage:"
                        + conn.getResponseMessage());

                InputStream in = conn.getInputStream();

                System.out.println("begin download");

                int i = 0;
                while ((i = in.read()) >= 0) {
                    out.write(i);
                }

                in.close();

                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();

            if (conn != null) {
                conn.disconnect();
            }

            return false;
        }
    }

    public static boolean copyFile(String fromFilename, String toFilename) {
        InputStream in = null;
        OutputStream out = null;
        System.out.println("copyFile(" + fromFilename + ", " + toFilename + ")");
        try {
            in = new FileInputStream(fromFilename);
            out = new FileOutputStream(toFilename);

            int byteBuf = 0;
            while ((byteBuf = in.read()) >= 0) {
                out.write(byteBuf);
            }

            //in.close();
            //out.close();
        } catch (Exception ex) {
            System.out.println("copyFile() exception: " + ex.toString());
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception ex) {
                System.out.println("copyFile() exception: " + ex.toString());
                return false;
            }
        }
        return true;
    }

    public static boolean isZipArchive(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return false;
        }
        return isZipArchive(file);
    }

    public static boolean isZipArchive(File file) {
        try {
            if (file == null || !file.exists()) {
                return false;
            }

            ZipFile zipFile = new ZipFile(file, ZipFile.OPEN_READ);
            int zipEntriesSize = zipFile.size();

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * распаковывает дистрибутив терминала из ZIP файла, перезаписывая
     * существующие файлы
     */
    public static boolean unzipDistributive(String fromFile, String toLocation) {
        final int BUFFER = 2048;
        try {
            File FileToLocationDir = new File(toLocation);
            if (!FileToLocationDir.exists()) {
                FileToLocationDir.mkdirs();
            }
            FileInputStream fis = new FileInputStream(fromFile);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                int count;
                byte data[] = new byte[BUFFER];
                String entryName = com.github.dmogn.util.StringToolkit.replaceAll(entry.getName(), "/", File.separator);
                System.out.println("Extracting: " + entryName);
                File entryFile = new File(toLocation + File.separator + entryName);

                if (entry.isDirectory()) {
                    if (!entryFile.exists()) {
                        entryFile.mkdirs();
                    }
                    continue;
                } else {
                    if (!entryFile.getParentFile().exists()) {
                        entryFile.getParentFile().mkdirs();
                    }
                }
                // write the files to the disk
                FileOutputStream fos = new FileOutputStream(entryFile);
                BufferedOutputStream dest = new BufferedOutputStream(fos, BUFFER);
                while ((count = zis.read(data, 0, BUFFER)) != -1) {
                    dest.write(data, 0, count);
                }
                dest.flush();
                dest.close();
            }
            zis.close();
            fis.close();

            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static byte intToIOByte(int i) {
        return (byte) (i & 0xFF);
    }
}
