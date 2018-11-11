package com.android.euithemes;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ThemeFactory {
    private Context mContext;

    public ThemeFactory(Context context) {
        mContext = context;
    }

    public ArrayList<Theme> getThemes(Context context) {
        ArrayList<Theme> list = new ArrayList<>();
        Theme theme;
        try {
            XmlPullParser xpp = context.getResources().getXml(R.xml.files);
            while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                switch (xpp.getEventType()) {
                    case XmlPullParser.START_TAG:
                        if (xpp.getName().equals("file")) {
                            theme = new Theme();
                            theme.setFileName(xpp.getAttributeValue(null, "name"));
                            theme.setPicture(xpp.getAttributeValue(null, "picture"));
                            theme.setUrl(xpp.getAttributeValue(null, "url"));
                            list.add(theme);
                        }
                        break;
                    default:
                        break;
                }
                xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        list = addNames(list);
        return checkDownloads(list);
    }

    private ArrayList<Theme> checkDownloads(ArrayList<Theme> list) {
        File dir = new File(Environment.getExternalStorageDirectory() + "/.EUITheme/");
        if (dir.exists()) {
            String[] files = dir.list();
            String file;
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    file = files[i].substring(0, files[i].length() - 4);
                    for (Theme theme : list) {
                        if (theme.getFileName().equals(file))
                            theme.setDownloaded(true);
                    }
                }
            }
        }
        return list;
    }

    private ArrayList<Theme> addNames(ArrayList<Theme> list) {
        for (Theme theme : list) {
            int resId = mContext.getResources().getIdentifier(theme.getFileName(), "string", mContext.getPackageName());
            theme.setName(mContext.getString(resId));
        }
        return list;
    }
}
