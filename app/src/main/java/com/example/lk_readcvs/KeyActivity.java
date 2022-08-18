package com.example.lk_readcvs;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lk_readcvs.View.BottomUI;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class KeyActivity extends AppCompatActivity {
    @BindView(R.id.btn_get)
    Button btn_get;
    @BindView(R.id.tvKey)
    TextView tvKey;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.tvCode0)
    TextView tvCode0;
    @BindView(R.id.tvCode1)
    TextView tvCode1;
    @BindView(R.id.tvCode2)
    TextView tvCode2;
    @BindView(R.id.tvCode3)
    TextView tvCode3;
    @BindView(R.id.tvCode4)
    TextView tvCode4;
    @BindView(R.id.tvCode5)
    TextView tvCode5;
    @BindView(R.id.tvCode6)
    TextView tvCode6;
    @BindView(R.id.tvCode7)
    TextView tvCode7;
    @BindView(R.id.tvCode8)
    TextView tvCode8;
    @BindView(R.id.tvCode9)
    TextView tvCode9;
    @BindView(R.id.tvCode10)
    TextView tvCode10;
    @BindView(R.id.tvCode11)
    TextView tvCode11;
    @BindView(R.id.tvCode12)
    TextView tvCode12;
    @BindView(R.id.tvCode13)
    TextView tvCode13;
    @BindView(R.id.tvCode14)
    TextView tvCode14;
    @BindView(R.id.tvCode15)
    TextView tvCode15;
    @BindView(R.id.tvCode16)
    TextView tvCode16;
    @BindView(R.id.tvCode17)
    TextView tvCode17;
    @BindView(R.id.tvCode18)
    TextView tvCode18;
    @BindView(R.id.tvCode19)
    TextView tvCode19;
    @BindView(R.id.tvCode20)
    TextView tvCode20;
    @BindView(R.id.tvCode21)
    TextView tvCode21;
    @BindView(R.id.tvCode22)
    TextView tvCode22;
    @BindView(R.id.tvCode23)
    TextView tvCode23;
    @BindView(R.id.etCode)
    EditText etCode;

    private TextView[] textViews;
    public static final String TAG = "KeyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //不息屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏底部按钮
        new BottomUI().hideBottomUIMenu(this.getWindow());
        setContentView(R.layout.activity_key);
        ButterKnife.bind(this);
        initView();
        initEvents();
    }

    private void initView() {
        textViews = new TextView[24];
        textViews[0] = tvCode0;
        textViews[1] = tvCode1;
        textViews[2] = tvCode2;
        textViews[3] = tvCode3;
        textViews[4] = tvCode4;
        textViews[5] = tvCode5;
        textViews[6] = tvCode6;
        textViews[7] = tvCode7;
        textViews[8] = tvCode8;
        textViews[9] = tvCode9;
        textViews[10] = tvCode10;
        textViews[11] = tvCode11;
        textViews[12] = tvCode12;
        textViews[13] = tvCode13;
        textViews[14] = tvCode14;
        textViews[15] = tvCode15;
        textViews[16] = tvCode16;
        textViews[17] = tvCode17;
        textViews[18] = tvCode18;
        textViews[19] = tvCode19;
        textViews[20] = tvCode20;
        textViews[21] = tvCode21;
        textViews[22] = tvCode22;
        textViews[23] = tvCode23;
    }

    private void initEvents() {
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i(TAG, "beforeTextChanged: 输入过程中执行该方法" + start + "-" + count + "-" + after);
                if (count == 23 && after == 24) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(TAG, "onTextChanged: 输入前确认执行该方法" + start + "-" + before + "-" + count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String data = s.toString();
                if (data != null) {
                    if (data.length() > 0) {
                        for (int i = 0; i < 24; i++) {
                            if (i < data.length()) {
                                textViews[i].setText(String.valueOf(data.charAt(i)));
                            } else {
                                textViews[i].setText("");
                            }
                        }
                    } else {
                        textViews[0].setText("");
                    }
                }
            }
        });

    }

    @OnClick(R.id.btn_get)
    public void onClick() {
        tvKey.setText("");
        String inData = "";
        for (int i = 0; i < textViews.length; i++) {
            inData = inData + textViews[i].getText().toString();
        }
        if (inData != null && !inData.equals("") && inData.length() == 24) {
            String[] stringArray = new String[12];
            for (int i = 0; i<stringArray.length; i++){
                stringArray[i] = inData.substring(i*2,i*2+2);
            }
            int[] intArray = new int[stringArray.length];
            for (int i = 0; i < stringArray.length; i++) {
                intArray[i] = Integer.parseInt(stringArray[i], 16);
            }
            int count_id = ((int) ((intArray[11] >> 1) & 0x07) << 29) |
                    ((int) (intArray[10] & 0x07) << 26) |
                    ((int) ((intArray[9] >> 5) & 0x07) << 23) |
                    ((int) ((intArray[8] >> 4) & 0x07) << 20) |
                    ((int) ((intArray[7] >> 3) & 0x07) << 17) |
                    ((int) ((intArray[6] >> 2) & 0x07) << 14) |
                    ((int) ((intArray[5] >> 1) & 0x07) << 11) |
                    ((int) (intArray[4] & 0x07) << 8) |
                    ((int) ((intArray[3] >> 6) & 0x03) << 6) |
                    ((int) ((intArray[2] >> 4) & 0x03) << 4) |
                    ((int) ((intArray[1] >> 2) & 0x03) << 2) |
                    ((int) (intArray[0] & 0x03));
            linearLayout.setVisibility(View.VISIBLE);
            tvKey.setText(count_id + "");
        } else {
            Toast.makeText(this, R.string.please_write, Toast.LENGTH_SHORT).show();
        }
    }
}