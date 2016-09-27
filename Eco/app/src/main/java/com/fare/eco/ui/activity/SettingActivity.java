package com.fare.eco.ui.activity;

import com.fare.eco.config.Constants;
import com.fare.eco.config.ServerUrl;
import com.fare.eco.ui.R;
import com.fare.eco.ui.util.ShowUtil;
import com.fare.eco.ui.view.TitleBarLayout;
import com.fare.eco.ui.view.TitleBarLayout.LeftAction;
import com.fare.eco.ui.view.TitleBarLayout.OnTitleBarLeftClickListener;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * 设置界面
 *
 * @author Xiao_V
 * @since 2015/8/19
 */
public class SettingActivity extends Activity {

    private EditText et_ip, et_port;
    private Button submit;
    private TitleBarLayout titleBar;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
        setListener();

        titleBar.setTitle("设置");
        titleBar.setLeftAction(LeftAction.BACK);

        et_ip.setText(sp.getString("ip", ServerUrl.IP));
        et_port.setText(sp.getString("port", ServerUrl.PORT));
    }

    private void init() {
        et_ip = (EditText) findViewById(R.id.ip);
        et_port = (EditText) findViewById(R.id.port);
        submit = (Button) findViewById(R.id.submit);
        titleBar = (TitleBarLayout) findViewById(R.id.titleBar);

        sp = SettingActivity.this.getSharedPreferences(Constants.SHARE_NAME_IP, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    private void setListener() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("ip", et_ip.getText().toString().trim());
                editor.putString("port", et_port.getText().toString().trim());
                commitResult("数据保存成功", "数据保存失败");
            }
        });

        titleBar.setOnTitleBarLeftClickListener(new OnTitleBarLeftClickListener() {

            @Override
            public void onLeftClickListener(View view) {
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
    }

    /**
     * editor提交并判断提交结果
     */
    private void commitResult(String success, String fail) {
        boolean commit = editor.commit();
        if (commit) {
            ShowUtil.showToast(this, success);
        } else {
            ShowUtil.showToast(this, fail);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        editor = null;
        sp = null;
    }
}
