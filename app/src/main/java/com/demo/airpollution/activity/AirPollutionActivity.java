package com.demo.airpollution.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.demo.airpollution.BaseActivity;
import com.demo.airpollution.Interface.ICtrl;
import com.demo.airpollution.R;

/**
 * Project : air demo
 * Class : MainActivity
 * Create by Ives 2022
 *
 * 主畫面
 * view 處理畫面
 * model
 * control 檢查 權限設定 與 Api資料抓取
 */
public class AirPollutionActivity extends BaseActivity implements View.OnClickListener, ICtrl {
    AirPollutionCtrl ctrl = new AirPollutionCtrl();
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //先只呼叫一次，之後可改為定時呼叫使用 AsyncTack
        dialog = new ProgressDialog(this);
        dialog.setMessage("獲取資料中");
        dialog.show();
        ctrl.setCallback(this);
        ctrl.getDataFromAPI();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void update() {
        if(dialog != null)
            dialog.dismiss();
    }
}