package com.demo.airpollution.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.app.Service;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.airpollution.BaseActivity;
import com.demo.airpollution.Constant.AirPollutionData;
import com.demo.airpollution.Interface.IAdapter;
import com.demo.airpollution.Interface.ICtrl;
import com.demo.airpollution.MyLog;
import com.demo.airpollution.R;
import com.demo.airpollution.activity.RecyclerView.LowerRecycleAdapter;
import com.demo.airpollution.activity.RecyclerView.SpacesItemDecoration;
import com.demo.airpollution.activity.RecyclerView.UpperRecycleAdapter;

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
public class AirPollutionActivity extends BaseActivity implements View.OnClickListener,ICtrl, IAdapter {
    String TAG = this.getClass().getSimpleName();
    AirPollutionCtrl ctrl = new AirPollutionCtrl();
    ProgressDialog dialog;
    RecyclerView recycle_upper,recycle_lower;
    UpperRecycleAdapter adapter_upper;
    LowerRecycleAdapter adapter_lower;
    TextView tv_search_hint;
    boolean isEnterSearch = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyLog.i(TAG,"onCreate");
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

        recycle_lower = findViewById(R.id.recycler_lower);
        adapter_lower = new LowerRecycleAdapter(this,AirPollutionData.getInstance().getAirLowerList(),this);
        recycle_lower.setLayoutManager(new LinearLayoutManager(this));
        recycle_lower.setAdapter(adapter_lower);

        tv_search_hint = findViewById(R.id.tv_search_hint);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        //進入、離開Search監聽
        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                MyLog.d(TAG,"Search onMenuItemActionExpand");
                recycle_upper.setVisibility(View.GONE);
                tv_search_hint.setVisibility(View.VISIBLE);
                tv_search_hint.setText("輸入「站名」\n查詢該地區空汙資料");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                MyLog.d(TAG,"Search onMenuItemActionCollapse");
                adapter_lower.updateLowerList();
                recycle_upper.setVisibility(View.VISIBLE);
                tv_search_hint.setVisibility(View.GONE);
                isEnterSearch = false;
                return true;
            }
        });
        SearchView searchView = (SearchView) menuItem.getActionView();
        //更改提示字串
        ((EditText)searchView.findViewById(androidx.appcompat.R.id.search_src_text)).setHint("請輸入「站名」");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                MyLog.d(TAG,"onQueryTextSubmit = " + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                MyLog.d(TAG,"onQueryTextChange = " + newText);
                if(isEnterSearch)
                    adapter_lower.getFilter().filter(newText);
                else
                    isEnterSearch = true;
                return false;
            }
        });
        return true;
    }
    @Override
    public void update() {
        if(dialog != null)
            dialog.dismiss();
        initView();
    }
    @Override
    public void onClick(View view) {

    }
    @Override
    public void onButtonClick(View view, int position) {

    }

    @Override
    public void onEditorAction() {

    }

    @Override
    public void onSearchResult(String searchtext, int resultcount) {
        if(resultcount > 0){
            tv_search_hint.setVisibility(View.GONE);
        }else{
            tv_search_hint.setVisibility(View.VISIBLE);
            if(searchtext.length() > 0)
                tv_search_hint.setText("找不到「"+ searchtext +"」\n相關空汙資訊");
            else
                tv_search_hint.setText("輸入「站名」\n查詢該地區空汙資料");
        }
    }
}