package com.android.euithemes;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadService extends IntentService {
    public static final int DOWNLOAD_SUCCESSFUL = 8344;
    public DownloadService() {
        super("DownloadService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        checkDir();
        boolean result = true;
        String urlToDownload = intent.getStringExtra("url");
        ResultReceiver receiver = intent.getParcelableExtra("receiver");
        try {
            URL url = new URL(urlToDownload);
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar

            // download the file
            InputStream input = new BufferedInputStream(connection.getInputStream());
            StringBuilder fileName = new StringBuilder();
            fileName.append(Environment.getExternalStorageDirectory());
            fileName.append("/.EUITheme/");
            fileName.append(intent.getStringExtra("name"));
            fileName.append(".zip");
            OutputStream output = new FileOutputStream(
                    fileName.toString());

            byte data[] = new byte[1024];
            int count;
            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }

        Bundle resultData = new Bundle();
        resultData.putInt("position", intent.getIntExtra("position", -1));
        resultData.putBoolean("result", result);
        receiver.send(DOWNLOAD_SUCCESSFUL, resultData);
    }

    private void checkDir() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/.EUITheme");
        if (!dir.exists())
            dir.mkdir();
    }
}
