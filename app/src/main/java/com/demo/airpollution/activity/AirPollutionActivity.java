package com.demo.airpollution.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.demo.airpollution.BaseActivity;
import com.demo.airpollution.Constant.AirPollutionData;
import com.demo.airpollution.Interface.IAdapter;
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
public class AirPollutionActivity extends BaseActivity implements View.OnClickListener, ICtrl, IAdapter {
    AirPollutionCtrl ctrl = new AirPollutionCtrl();
    ProgressDialog dialog;
    RecyclerView recycle_upper,recycle_lower;
    UpperRecycleAdapter adapter_upper;
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
    private void initView(){
        recycle_upper  = findViewById(R.id.recycler_upper);
        adapter_upper = new UpperRecycleAdapter(this, AirPollutionData.getInstance().getAirUpperList(),this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        recycle_upper.setLayoutManager(manager);
        recycle_upper.setAdapter(adapter_upper);
        recycle_upper.addItemDecoration(new SpacesItemDecoration(20));
    }
    @Override
    public void onClick(View view) {

    }

    @Override
    public void update() {
        if(dialog != null)
            dialog.dismiss();
        initView();
    }

    @Override
    public void onButtonClick(View view, int position) {

    }

    @Override
    public void onEditorAction() {

    }
}