package com.gaparmar.mediaflashback;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.gaparmar.mediaflashback.UI.BackgroundService;
import com.gaparmar.mediaflashback.UI.MainActivity;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by veronica.lin1218 on 3/3/2018.
 */

public class MusicDownloader {
    private Context myContext;
    private MusicQueuer mq;
    HashMap<String, String> allUrls = new HashMap<>();
    private final String MEDIA_PATH = Environment.DIRECTORY_DOWNLOADS
                + File.separator + "myDownloads";
    private final String COMPLETE_PATH =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()
                    + File.separator + "myDownloads";

    public MusicDownloader(Context context) {
        myContext = context;
        mq = BackgroundService.getMusicQueuer();

    }

    /**
     * Download song given a URL and it's song name to myDownloads folder (raw folder is RO)
     * @param url string link
     * @param filename Name of file to be downloaded
     *  @param type mp3 (for song) or zip (for album)
     */
    public void downloadData(String url, String filename, String type) {
        final String t = type;
        final String filenameReceiver = filename;
        /**
         * Unzip file after download completion
         */
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                if (t.equals("zip")) {
                    File temp = new File(COMPLETE_PATH + File.separator + filenameReceiver + ".zip");
                    Log.d("md:unzip", "Start unzipping " + temp.getParent());
                    unZip(COMPLETE_PATH + File.separator + filenameReceiver + ".zip",
                            temp.getParent() + File.separator, filenameReceiver +".zip");
                }
            }
        };
        myContext.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        request.setDescription(url);
        request.setTitle(filename);

        request.setDestinationInExternalPublicDir(MEDIA_PATH, filename+"." + type);
        DownloadManager dm = (DownloadManager) myContext.getSystemService(DOWNLOAD_SERVICE);

        // add song to download list
        dm.enqueue(request);


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
        Log.d("unzip", "in unzip method " + path);

        try {
            String songFileName;
            is = new FileInputStream(path);
            zis = new ZipInputStream(new BufferedInputStream(is));
            ZipEntry ze;
            byte[] buffer = new byte[1024];
            int count;

            while ((ze = zis.getNextEntry()) != null) {
                songFileName = ze.getName();
                Log.d("unzip: fileName", songFileName);
                if (ze.isDirectory()) {
                    Log.d("unzip: isDirectory", targetDir+ songFileName);
                    File newFile = new File(targetDir + songFileName);
                    newFile.mkdirs();
                    continue;
                }
                FileOutputStream fout = new FileOutputStream(targetDir + songFileName);

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
