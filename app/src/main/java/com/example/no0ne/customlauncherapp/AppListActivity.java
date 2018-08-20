package com.example.no0ne.customlauncherapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class AppListActivity extends AppCompatActivity {

    private PackageManager mManager;
    private List<Item> mAppList;

    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        loadApps();
        loadListView();
        addClickListener();
    }

    private void loadApps() {
        mManager = getPackageManager();
        mAppList = new ArrayList<>();

        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = mManager.queryIntentActivities(intent, 0);

        for (ResolveInfo info : availableActivities) {
            Item app = new Item();
            app.label = info.activityInfo.packageName; // Get app package
            app.name = info.loadLabel(mManager); // Get app name
            app.icon = info.loadIcon(mManager); // Get app icon

            mAppList.add(app);
        }
    }

    private void loadListView() {
        mListView = (ListView) findViewById(R.id.list_view);

        ArrayAdapter<Item> adapter = new ArrayAdapter<Item>(this, R.layout.item, mAppList) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.item, null);
                }

                ImageView iconImageView = convertView.findViewById(R.id.image_view_icon);
                iconImageView.setImageDrawable(mAppList.get(position).icon);

                TextView nameTextView = convertView.findViewById(R.id.text_view_name);
                nameTextView.setText(mAppList.get(position).name);

                return convertView;
            }
        };

        mListView.setAdapter(adapter);
    }

    private void addClickListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = mManager.getLaunchIntentForPackage(mAppList.get(position).label.toString());
                startActivity(intent);
            }
        });
    }
}
