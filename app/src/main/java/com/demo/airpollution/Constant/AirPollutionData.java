package com.demo.airpollution.Constant;

import com.demo.airpollution.MyLog;
import com.demo.airpollution.activity.AirPollution;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Project : air demo
 * Class : AirPollutionData
 * Create by Ives 2022
 * 取得的空汙資料
 */
public class AirPollutionData {
    private static AirPollutionData mInstance;
    public static synchronized AirPollutionData getInstance() {
        if (null == mInstance) {
            mInstance = new AirPollutionData();
            mInstance.airList = new ArrayList<AirPollution>();
        }
        return mInstance;
    }
    public static void release(){
        if(mInstance != null) {
            mInstance.airList.clear();
            mInstance.airList = null;
            mInstance = null;
        }
    }
    private String TAG = this.getClass().getSimpleName();
    ArrayList<AirPollution> airList;
    public void parserData(String jsonarray){
        try {
            airList.clear();
            JSONArray array = new JSONArray(jsonarray);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                AirPollution data = new AirPollution();
                if (obj.has("Status"))
                    data.setStatus(obj.getString("Status"));
                if (obj.has("SiteId"))
                    data.setSiteId(obj.getString("SiteId"));
                if (obj.has("SiteName"))
                    data.setSiteName(obj.getString("SiteName"));
                if (obj.has("County"))
                    data.setCountry(obj.getString("County"));
                if (obj.has("PM2.5"))
                    data.setPM25(obj.getString("PM2.5"));
                airList.add(data);
            }
        }catch(JSONException e){
            MyLog.e(TAG, "json format error : " + e.getMessage());
        }
    }
}
