package com.sojay.demo.label;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sojay.demo.R;
import com.sojay.demo.label.tag.TagBean;
import com.sojay.demo.label.tag.TagContainerLayout;
import com.sojay.demo.label.tag.TagView;
import com.sojay.demo.util.RegUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LabelActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String KEY_OBJ = "key_obj";//对象传参key值
    public static final int LAB = 1001;

    private TagContainerLayout mTclSelectTags;
    private EditText mEtInput;
    private TextView mTvAddTag;
    private TagContainerLayout mTclRecommendTag;

    private List<TagBean> mTags;
    private List<TagBean> mSelectTags;

    public static void start(Activity context) {
        context.startActivity(new Intent(context, LabelActivity.class));
    }

    public static void start(Activity context, List<TagBean> tags) {
        Intent it = new Intent(context, LabelActivity.class);
        it.putExtra(KEY_OBJ, new Gson().toJson(tags));
        context.startActivityForResult(it, LAB);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label);

        findViewById(R.id.toolbar_back).setOnClickListener(this);
        findViewById(R.id.toolbar_finish).setOnClickListener(this);
        mTclSelectTags = findViewById(R.id.select_tags);
        mEtInput = findViewById(R.id.et_input);
        mTvAddTag = findViewById(R.id.tv_add_tag);
        mTvAddTag.setOnClickListener(this);
        mTclRecommendTag = findViewById(R.id.recommend_tag);

        mSelectTags = new ArrayList<>();
        mTags = new ArrayList<>();

        String json = getIntent().getStringExtra(KEY_OBJ);
        if (!TextUtils.isEmpty(json)) {
            mSelectTags = new Gson().fromJson(json, new TypeToken<List<TagBean>>() {}.getType());
            mTclSelectTags.setTags(mSelectTags);
        }

        initTags();

        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    if (!labelNameCheck(editable)) {
                        int index = mEtInput.getSelectionStart();
                        if (index > 0)
                            editable.delete(index - 1, index);
                    }
                }
                mTvAddTag.setVisibility(TextUtils.isEmpty(editable.toString()) ? View.GONE : View.VISIBLE);
            }
        });

        mTclSelectTags.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, TagBean bean) {

            }

            @Override
            public void onTagLongClick(int position, TagBean bean) {

            }

            @Override
            public void onSelectedTagDrag(int position, TagBean bean) {

            }

            @Override
            public void onTagCrossClick(int position, TagBean bean) {
                int pos = getPosition(mSelectTags.get(position).getId());
                if (pos != -1) {
                    TagView tagView = mTclRecommendTag.getTagView(pos);
                    tagView.setTagTextColor(getResources().getColor(R.color.add_tag_input_text_color));
                    tagView.invalidate();
                    mTags.get(pos).setChecked(false);
                }
                mTclSelectTags.removeTag(position);
                mSelectTags.remove(position);
            }
        });

        mTclRecommendTag.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, TagBean bean) {
                if (!mTags.get(position).isChecked()) {
                    if (mSelectTags.size() >= 5) {
                        Toast.makeText(LabelActivity.this, "最多添加5个标签", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    TagView tagView = mTclRecommendTag.getTagView(position);
                    tagView.setTagTextColor(getResources().getColor(R.color.def_disable_color));
                    mTclSelectTags.addTag(bean);
                    mSelectTags.add(mTags.get(position));
                    mTags.get(position).setChecked(true);
                }
            }

            @Override
            public void onTagLongClick(int position, TagBean bean) {

            }

            @Override
            public void onSelectedTagDrag(int position, TagBean bean) {

            }

            @Override
            public void onTagCrossClick(int position, TagBean bean) {

            }
        });

    }

    private void initTags() {
        mTags.add(new TagBean("1", "aaaa"));
        mTags.add(new TagBean("2", "bbbb"));
        mTags.add(new TagBean("3", "cccc"));
        mTags.add(new TagBean("4", "dddd"));
        mTags.add(new TagBean("5", "eeee"));
        mTags.add(new TagBean("6", "ffff"));
        mTags.add(new TagBean("7", "gggg"));
        mTags.add(new TagBean("8", "hhhh"));
        mTags.add(new TagBean("9", "iiii"));
        mTclRecommendTag.setTags(mTags);
        if (mSelectTags.size() > 0) {
            for (int i = 0; i < mTags.size(); i++) {
                TagBean bean = mTags.get(i);
                for (TagBean b : mSelectTags) {
                    if (!TextUtils.isEmpty(b.getId()) && b.getId().equals(bean.getId())) {
                        TagView tagView = mTclRecommendTag.getTagView(i);
                        tagView.setTagTextColor(getResources().getColor(R.color.def_disable_color));
                        bean.setChecked(true);
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_back:
                finish();
                break;
            case R.id.toolbar_finish:
                if (mSelectTags.size() == 0) {
                    Toast.makeText(this, "至少添加一个标签", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(KEY_OBJ, new Gson().toJson(mSelectTags));
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
            case R.id.tv_add_tag:
                if (mSelectTags.size() >= 5) {
                    Toast.makeText(this, "最多添加5个标签", Toast.LENGTH_SHORT).show();
                    return;
                }
                String title = mEtInput.getText().toString();
                if (title.length() < 2 || title.length() > 6) {
                    Toast.makeText(this, "2-6个字", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!checkSelectTag(title)) {
                    mEtInput.setText("");
                    mTclSelectTags.addTag(new TagBean(title));
                    mSelectTags.add(new TagBean(title));
                } else {
                    Toast.makeText(this, "标签已存在", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private int getPosition(String id) {
        if (!TextUtils.isEmpty(id)) {
            for (int i = 0; i < mTags.size(); i++) {
                if (mTags.get(i).getId().equals(id))
                    return i;
            }
        }
        return -1;
    }

    /**
     * 判断已选标签中是否含有该标签
     */
    private boolean checkSelectTag(String title) {
        if (TextUtils.isEmpty(title))
            return false;
        for (TagBean bean : mSelectTags) {
            if (bean.getTag().equals(title)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 只能中文,数字,字母
     */
    public boolean labelNameCheck(Editable s) {
        int orignLen = s.length();
        String checkString = s.toString();
        String temp = null;
        for (int i = 0; i < orignLen; ++i) {
            temp = checkString.substring(i, i + 1);
            //特殊字符号
            if (RegUtils.isSymbol(temp))
                return false;
            //表情
            if (containsEmoji(temp))
                return false;
            //空格
            if (temp.trim().equals(""))
                return false;
            //中文
            try {
                if (temp.getBytes("utf-8").length != 3) {
                    //英文,数字
                    if (!RegUtils.isNumberLetter(temp))
                        return false;
                }
            } catch (UnsupportedEncodingException var5) {
                return false;
            }

        }
        return true;
    }

    public boolean containsEmoji(String str) {
        int len = str.length();

        for (int i = 0; i < len; ++i) {
            if (isEmojiCharacter(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }

    public boolean isEmojiCharacter(char codePoint) {
        return codePoint != 0 && codePoint != '\t' && codePoint != '\n' && codePoint != '\r' && (codePoint < ' ' || codePoint > '\ud7ff') && (codePoint < '\ue000' || codePoint > '�') && (codePoint < 65536 || codePoint > 1114111);
    }
}
