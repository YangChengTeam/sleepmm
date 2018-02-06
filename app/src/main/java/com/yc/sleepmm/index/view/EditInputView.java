package com.yc.sleepmm.index.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.yc.sleepmm.R;
import com.yc.sleepmm.index.util.CommonUtils;

/**
 * TinyHung@Outlook.com
 * 2018/2/5.
 */

public class EditInputView extends RelativeLayout {

    private EditText mEtInput;
    private ImageView mIv_clean_icon;

    public EditInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context,attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {
        View.inflate(context, R.layout.view_input_layout,this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.EditInputView);
        //文字颜色
        int textColor=typedArray.getColor(R.styleable.EditInputView_textColor, CommonUtils.getColor(R.color.input_text_color));
        //文字Hint颜色
        int hintTextColor=typedArray.getColor(R.styleable.EditInputView_hintTextColor, CommonUtils.getColor(R.color.input_hint_color));
        //输入框Hint文字
        String hintText=typedArray.getString(R.styleable.EditInputView_hintText);
        //输入框LOGO
        Drawable inputIcon=typedArray.getDrawable(R.styleable.EditInputView_inputIcon);
        //清除Logo
        Drawable cleanIcon=typedArray.getDrawable(R.styleable.EditInputView_cleanIcon);
        //inputType
//        int inputType = typedArray.getInt(R.styleable.EditInputView_inputType, EditorInfo.TYPE_NULL);
        ImageView ic_input_icon = (ImageView) findViewById(R.id.ic_input_icon);
        mIv_clean_icon = (ImageView) findViewById(R.id.iv_clean_icon);
        mEtInput = (EditText) findViewById(R.id.et_input);

        mEtInput.setHint(hintText);
        mEtInput.setHintTextColor(hintTextColor);
        mEtInput.setTextColor(textColor);
//        mEtInput.setInputType(inputType);

        ic_input_icon.setImageDrawable(inputIcon);
        mIv_clean_icon.setImageDrawable(cleanIcon);

        typedArray.recycle();

        mEtInput.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(null!=mEtInput&&null!=mIv_clean_icon){
                    if(hasFocus){
                        if(mEtInput.toString().length()>0){
                            mIv_clean_icon.setVisibility(View.VISIBLE);
                        }
                    }else{
                        if(null!=mIv_clean_icon) mIv_clean_icon.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(null!=mIv_clean_icon) mIv_clean_icon.setVisibility(!TextUtils.isEmpty(s)&&s.length()>0?View.VISIBLE:View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mIv_clean_icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=mEtInput){
                    mEtInput.setText("");
                }
            }
        });
    }

    public void setEditContent(String content){
        if(null!=mEtInput){
            mEtInput.setText(content);
            if(!TextUtils.isEmpty(content)&&content.length()>0){
                mEtInput.setSelection(content.length());
            }
        }
    }

    public String getEditContent(){
        return null==mEtInput?null:mEtInput.getText().toString().trim();
    }
}
