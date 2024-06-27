package com.example.ar_glasses.ui.home;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ar_glasses.R;

public class TutorialDetailActivity extends AppCompatActivity {
    public static final String EXTRA_TUTORIAL_ID = "tutorial_id";
    public static final String EXTRA_TUTORIAL_TITLE = "tutorial_title";
    public static final String EXTRA_TUTORIAL_CONTENT = "tutorial_content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial_detail);

        int tutorialId = getIntent().getIntExtra(EXTRA_TUTORIAL_ID, -1);
        String title = getIntent().getStringExtra(EXTRA_TUTORIAL_TITLE);
        String content = getIntent().getStringExtra(EXTRA_TUTORIAL_CONTENT);

        TextView titleTextView = findViewById(R.id.tutorial_title);
        TextView contentTextView = findViewById(R.id.tutorial_content);

        titleTextView.setText(title);
        contentTextView.setText(content);

        // 设置返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        // 这里你可以根据 tutorialId 加载相应的图片或其他内容
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // 处理返回按钮点击
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}