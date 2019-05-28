package com.sojay.demo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sojay.demo.article.ArticleActivity;
import com.sojay.demo.label.LabelActivity;
import com.sojay.demo.label.tag.TagBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private List<TagBean> mTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.article).setOnClickListener(this);
        findViewById(R.id.label).setOnClickListener(this);

        if (mTags == null)
            mTags = new ArrayList<>();

        requestPhotoPermissions();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.article:
                startActivity(new Intent(this, ArticleActivity.class));
                break;

            case R.id.label:
                LabelActivity.start(this, mTags);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == LabelActivity.LAB) {
            String json = data.getStringExtra(LabelActivity.KEY_OBJ);
            mTags = new Gson().fromJson(json, new TypeToken<List<TagBean>>() {}.getType());
            Toast.makeText(this, getTag(mTags), Toast.LENGTH_SHORT).show();
        }
    }

    private void requestPhotoPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){ //表示未授权时
            //进行授权
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO}, 1);
        }
    }

    /**
     * 将标签转换为字符串，","分隔
     * @param list
     * @return
     */
    private String getTag(List<TagBean> list) {
        String tag = "";
        for (TagBean bean : list) {
            tag += TextUtils.isEmpty(tag) ? bean.getTag() : "," + bean.getTag();
        }
        return tag;
    }
}
