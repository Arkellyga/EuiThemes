package com.android.euithemes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Theme> mThemes;
    private ThemeFactory mFactory;
    private GridView mGrid;
    private GridAdapter mAdapter;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestStorage();
        mFactory = new ThemeFactory(this);
        setContentView(R.layout.activity_main);
        mGrid = (GridView) findViewById(R.id.grid_view_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_main);
        mProgressBar.setVisibility(View.INVISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mThemes = mFactory.getThemes(this);
        changeThemesName();
        mAdapter = new GridAdapter(this, mThemes);
        mGrid.setAdapter(mAdapter);
        mGrid.setNumColumns(3);
        mGrid.setHorizontalSpacing(8);
        mGrid.setVerticalSpacing(10);
        mGrid.setOnItemClickListener(mItemListener);
    }

    private void changeThemesName() {
        for (Theme theme : mThemes) {
            int resId = getResources().getIdentifier(theme.getName(), "string", getPackageName());
            theme.setName(getString(resId));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_about:
                Toast.makeText(this, R.string.about_text, Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    AdapterView.OnItemClickListener mItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.scale);
            view.startAnimation(anim);
            Theme theme = mThemes.get(position);
            if (theme.isDownloaded()) {
                Toast.makeText(MainActivity.this, R.string.already_downloaded, Toast.LENGTH_SHORT).show();
                return;
            }
            mProgressBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(MainActivity.this, DownloadService.class);
            intent.putExtra("url", mThemes.get(position).getUrl());
            intent.putExtra("receiver", new DownloadReceiver(new Handler()));
            intent.putExtra("name", mThemes.get(position).getName());
            intent.putExtra("position", position);
            startService(intent);
        }
    };

    private void requestStorage() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        }
    }

    private class DownloadReceiver extends android.os.ResultReceiver {
        public DownloadReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == DownloadService.DOWNLOAD_SUCCESSFUL) {
                int result = R.string.download_error;
                if (resultData.getBoolean("result")) {
                    result = R.string.download_successful;
                    mThemes.get(resultData.getInt("position")).setDownloaded(true);
                    mAdapter.notifyDataSetChanged();
                }
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }
    }
}
