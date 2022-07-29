package com.demo.airpollution.Interface;

import android.view.View;

/**
 * Project : MKH inventory management
 * Class : IAdapter
 * Create by Ives 2020
 */
public interface IAdapter {
    void onButtonClick(View view , int position);
    void onEditorAction();
    void onSearchResult(String searchtext,int resultcount);
}
