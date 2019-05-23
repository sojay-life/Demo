package com.sojay.demo.article;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sojay.demo.ActionSelectInterface;
import com.sojay.demo.R;
import com.sojay.demo.article.bean.ImageBean;
import com.sojay.demo.article.view.RichEditor;
import com.sojay.demo.util.CompressImageUtils;
import com.sojay.demo.util.DensityUtils;
import com.sojay.demo.util.DeviceUtils;
import com.sojay.demo.util.ImmersionBarUtil;
import com.sojay.demo.util.KeyboardUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ArticleActivity extends AppCompatActivity implements View.OnClickListener, ActionSelectInterface.ActionSelectListener {

    public static int SCREENWIDTH;
    public static int SCREENHEIGHT;

    private RichEditor mRichEditor;
    private TextView mTvBold;
    private TextView mTvItalic;
    private TextView mTvUnderline;
    private View mVeiwUnderline;
    private TextView mTvQuote;
    private TextView mTvOrdered;
    private TextView mTvUnordered;
    private RelativeLayout mRlUnderlineLayout;
    private ImageView mIvKeyboard;
    private LinearLayout mArticleStyleLayout;
    private ScrollView mScrollView;

    private boolean isUnderline;
    private boolean isQuote;
    private boolean isOrdered;
    private boolean isUnordered;
    private boolean isBold;
    private boolean isItalic;
    private boolean mStyleShow;
    private boolean mKeyBoradShow;
    private boolean isClickShowStyle;
    private int meyboardHeight;

    private List<String> mSelected = new ArrayList<>();
    private List<ImageBean> mRotateImgs;
    private CompressImageUtils mUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        findView();

        mRichEditor.setPadding(13, 13, 13, 0);
        mRichEditor.setEditorFontSize(17);
        mRichEditor.setPlaceholder("点击输入正文");

        ActionSelectInterface anInterface = new ActionSelectInterface();
        anInterface.setListener(this);
        mRichEditor.addJavascriptInterface(anInterface, "RE");

        mUtils = new CompressImageUtils();

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        SCREENWIDTH = dm.widthPixels;
        SCREENHEIGHT = dm.heightPixels;

        ImmersionBarUtil.setOnKeyboardListener(this, (isPopup, keyboardHeight) -> {
            if (meyboardHeight == 0) {
                ViewGroup.LayoutParams params = mArticleStyleLayout.getLayoutParams();
                params.height = keyboardHeight;
                mArticleStyleLayout.setLayoutParams(params);
                meyboardHeight = keyboardHeight;
            }
            if (isPopup) {
                showStyleLayout(false);
            } else {
                if (isClickShowStyle) {
                    showStyleLayout(true);
                    isClickShowStyle = false;
                }
            }
            mKeyBoradShow = isPopup;
            setIvKeyboard();
        });

    }

    private void findView() {
        TextView title = findViewById(R.id.tv_title);
        title.setText("编辑文章");
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_ok).setOnClickListener(this);
        mRichEditor = findViewById(R.id.re_content);
        mIvKeyboard = findViewById(R.id.iv_keyboard);
        mIvKeyboard.setOnClickListener(this);
        findViewById(R.id.iv_post_img).setOnClickListener(this);
        findViewById(R.id.iv_style).setOnClickListener(this);
        mTvBold = findViewById(R.id.tv_bold);
        mTvBold.setOnClickListener(this);
        mTvItalic = findViewById(R.id.tv_italic);
        mTvItalic.setOnClickListener(this);
        mRlUnderlineLayout = findViewById(R.id.rl_underline_layout);
        mVeiwUnderline = findViewById(R.id.view_underline);
        mTvUnderline = findViewById(R.id.tv_underline);
        mRlUnderlineLayout.setOnClickListener(this);
        mTvQuote = findViewById(R.id.tv_quote);
        mTvQuote.setOnClickListener(this);
        mTvOrdered = findViewById(R.id.tv_ordered);
        mTvOrdered.setOnClickListener(this);
        mTvUnordered = findViewById(R.id.tv_unordered);
        mTvUnordered.setOnClickListener(this);
        mArticleStyleLayout = findViewById(R.id.article_style_layout);
        mScrollView = findViewById(R.id.scroll_style);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainPathResult(data);
            mRotateImgs = new ArrayList<>();
            if (mSelected.size() > 0) {
                for (String path : mSelected) {
                    int rotate = mUtils.readPictureDegree(path);
                    if (rotate == 0) {//图片没有旋转
                        mRichEditor.insertImage(path);
                    } else if (rotate == 180) {//图片旋转180
                        mRichEditor.insertRotateImage(rotate, path);
                    } else {//图片旋转90或270
                        Bitmap bitmap = mUtils.getCompressPhoto(path);
                        float viewW = DeviceUtils.getScreenWidth(this) - DensityUtils.dp2px(this, 26);
                        int viewH = (int) (DensityUtils.px2dp(this, viewW * bitmap.getWidth() / bitmap.getHeight()));
                        ImageBean bean = new ImageBean(System.currentTimeMillis(), path);
                        mRichEditor.insertImageLoading(bean.getId(), viewH);
                        mRotateImgs.add(bean);
                    }
                }

                if (mRotateImgs.size() > 0) {
                    for (ImageBean bean : mRotateImgs) {
                        rotateImg(bean);
                    }
                }
            }
        }
    }

    /**
     * 旋转图片
     * @param bean
     */
    private void rotateImg(ImageBean bean) {
        Observable.create((ObservableOnSubscribe<ImageBean>) emitter -> {
            String s = mUtils.amendRotatePhoto(bean.getUrl());
            bean.setUrl(s);
            emitter.onNext(bean);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ImageBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {}
                    @Override
                    public void onNext(ImageBean img) {
                        mRichEditor.setImage(img.getId(), img.getUrl());
                    }
                    @Override
                    public void onError(Throwable e) {}
                    @Override
                    public void onComplete() {}
                });
    }

    @Override
    public void onInputChange(final String html) {
        runOnUiThread(() -> {
            mRichEditor.setContents(html);
        });
    }

    @Override
    public void onStatesChange(String states) {
        runOnUiThread(() -> {
            isBold = states.contains("bold");
            isItalic = states.contains("italic");
            isUnderline = states.contains("underline");
            isQuote = states.contains("blockquote");
            isOrdered = states.contains("orderedList");
            isUnordered = states.contains("unList");
            setStyle();
        });
    }

    private void setStyle() {
        if (mTvBold == null)
            return;
        mTvBold.setTextColor(isBold ? getResources().getColor(R.color.def_error_color) : getResources().getColor(R.color.def_title_color));
        mTvItalic.setTextColor(isItalic ? getResources().getColor(R.color.def_error_color) : getResources().getColor(R.color.def_title_color));
        mTvUnderline.setTextColor(isUnderline ? getResources().getColor(R.color.def_error_color) : getResources().getColor(R.color.def_title_color));
        mVeiwUnderline.setBackgroundColor(isUnderline ? getResources().getColor(R.color.def_error_color) : getResources().getColor(R.color.def_title_color));
        mTvQuote.setTextColor(isQuote ? getResources().getColor(R.color.def_error_color) : getResources().getColor(R.color.def_title_color));
        mTvOrdered.setTextColor(isOrdered ? getResources().getColor(R.color.def_error_color) : getResources().getColor(R.color.def_title_color));
        mTvUnordered.setTextColor(isUnordered ? getResources().getColor(R.color.def_error_color) : getResources().getColor(R.color.def_title_color));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_ok:
                Toast.makeText(this, mRichEditor.getHtml(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_keyboard:
                if (mStyleShow) {
                    showStyleLayout(false);
                    setIvKeyboard();
                } else {
                    if (mKeyBoradShow) {
                        KeyboardUtils.hideSoftInput(ArticleActivity.this);
                    } else {
                        KeyboardUtils.showSoftInput(ArticleActivity.this);
                    }
                }
                break;
            case R.id.iv_style:
                if (isSoftShowing()) {
                    isClickShowStyle = true;
                    KeyboardUtils.hideSoftInput(ArticleActivity.this);
                } else {
                    showStyleLayout(true);
                    setIvKeyboard();
                }
                break;
            case R.id.iv_post_img:
                Matisse.from(ArticleActivity.this)
                        .choose(MimeType.ofImage())
                        .countable(true)
                        .capture(true)
                        .spanCount(4)
                        .maxSelectable(20)
                        .showSingleMediaType(true)
                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f)
                        .imageEngine(new GlideEngine())
                        .captureStrategy(
                                new CaptureStrategy(true, getPackageName()
                                        , "dianjiemian"))
                        .forResult(101);
                break;
            case R.id.tv_bold:
                isBold = !isBold;
                setStyle();
                mRichEditor.setBold();
                break;
            case R.id.tv_italic:
                isItalic = !isItalic;
                setStyle();
                mRichEditor.setItalic();
                break;
            case R.id.rl_underline_layout:
                isUnderline = !isUnderline;
                setStyle();
                mRichEditor.setUnderline();
                break;
            case R.id.tv_quote:
                isQuote = !isQuote;
                setStyle();
                mRichEditor.setBlockquote();
                break;
            case R.id.tv_ordered:
                isOrdered = !isOrdered;
                setStyle();
                mRichEditor.setOrderedList();
                break;
            case R.id.tv_unordered:
                isUnordered = !isUnordered;
                setStyle();
                mRichEditor.setUnorderedList();
                break;
            case R.id.color1:
                mRichEditor.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.color2:
                mRichEditor.setTextColor(getResources().getColor(R.color.def_main_color_2));
                break;
            case R.id.color3:
                mRichEditor.setTextColor(getResources().getColor(R.color.dodger_blue));
                break;
            case R.id.color4:
                mRichEditor.setTextColor(getResources().getColor(R.color.def_error_color));
                break;
        }
    }

    private void showStyleLayout(boolean isShow) {
        if (isShow) {
            if (mArticleStyleLayout.getVisibility() != View.VISIBLE) {
                mArticleStyleLayout.setVisibility(View.VISIBLE);
                mScrollView.scrollTo(0, 0);
                mStyleShow = true;
            }
        } else {
            if (mArticleStyleLayout.getVisibility() != View.GONE) {
                mArticleStyleLayout.setVisibility(View.GONE);
                mStyleShow = false;
            }
        }
    }

    private void setIvKeyboard() {
        mIvKeyboard.setImageResource(mKeyBoradShow || mStyleShow ? R.drawable.keyboard_hidd : R.drawable.keyboard_show);
    }

    private boolean isSoftShowing() {
        //获取当前屏幕内容的高度
        int screenHeight = getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        //DecorView即为activity的顶级view
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
        //选取screenHeight*2/3进行判断
        return screenHeight * 2 / 3 > rect.bottom;
    }

    /**
     * 获取html中所有的图片地址
     */
    private String getPhotos(String html) {
        String photos = "";
        Document doc = Jsoup.parse(html);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            String src = element.attr("src");
            if (TextUtils.isEmpty(photos)){
                photos += src;
            } else {
                photos = photos + "," + src;
            }
        }
        return photos;
    }

    /**
     * 获取html中需要上传的图片地址
     */
    public List<String> getImgs(String htmltext) {
        List<String> imgs = new ArrayList<>();
        Document doc = Jsoup.parse(htmltext);
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            String src = element.attr("src");
            if (!src.startsWith("http"))
                imgs.add(src);
        }
        return imgs;
    }

}
