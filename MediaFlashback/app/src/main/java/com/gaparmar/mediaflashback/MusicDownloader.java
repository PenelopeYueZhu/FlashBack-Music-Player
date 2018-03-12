package com.gaparmar.mediaflashback;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.gaparmar.mediaflashback.UI.MainActivity;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by veronica.lin1218 on 3/3/2018.
 */

public class MusicDownloader {
    private Context myContext;
    private MusicQueuer mq;
    private final String MEDIA_PATH = Environment.DIRECTORY_DOWNLOADS
                + File.separator + "myDownloads";

    public MusicDownloader(Context context) {
        myContext = context;
        mq = MainActivity.getMusicQueuer();

    }

    /**
     * Download song given a URL and it's song name to myDownloads folder (raw folder is RO)
     * @param url string link
     * @param filename Name of file to be downloaded
     *  @param type mp3 (for song) or zip (for album)
     */
    public void downloadData(String url, String filename, String type) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        request.setDescription(url);
        request.setTitle(filename);

        request.setDestinationInExternalPublicDir(MEDIA_PATH, filename+"." + type);
        DownloadManager dm = (DownloadManager) myContext.getSystemService(DOWNLOAD_SERVICE);

        // add song to download list
        dm.enqueue(request);
        if (type.equals("zip")) {
            unZip(MEDIA_PATH+File.pathSeparator, MEDIA_PATH+File.pathSeparator, filename +".zip");
        }
    }

    /**
     * Unzip a file (album) for its songs
     * Source: https://stackoverflow.com/questions/3382996/how-to-unzip-files-programmatically-in-android
     * @param path Directory to save album in
     * @param targetDir Album Name
     * @param zipName filename
     */
    public boolean unZip(String path, String targetDir, String zipName) {
        InputStream is;
        ZipInputStream zis;

        try {
            String songFileName;
            is = new FileInputStream(path + zipName);
            zis = new ZipInputStream(new BufferedInputStream(is));

            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                songFileName = ze.getName();

                if (ze.isDirectory()) {
                    File song = new File(path + songFileName);
                    song.mkdirs();
                    continue;
                }
                FileOutputStream fout = new FileOutputStream(path + songFileName);

                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }
                fout.close();
                zis.closeEntry();
            }
            zis.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;


    }
}
