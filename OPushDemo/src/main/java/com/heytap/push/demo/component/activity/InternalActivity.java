package com.heytap.push.demo.component.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.heytap.push.demo.R;
import com.heytap.push.demo.util.LogUtil;
import com.heytap.push.demo.util.TestModeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InternalActivity extends Activity {
    private ArrayList<Map<String, String>> paramList = new ArrayList<>();
    private ParamAdapter paramAdapter;
    ListView mListView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("onCreate");
        setContentView(R.layout.activity_target_page);
        mListView = findViewById(R.id.listview);
        paramAdapter = new ParamAdapter(this, paramList);
        mListView.setAdapter(paramAdapter);
        parseIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        LogUtil.d("onNewIntent");
        parseIntent(intent);
        super.onNewIntent(intent);
    }


    private void parseIntent(Intent intent) {
        TestModeUtil.addLogString("parseIntent", "intent = " + intent);
        paramList.clear();
        HashMap<String, String> actionMap = new HashMap<>();
        actionMap.put("key", "action");
        actionMap.put("value", intent.getAction());
        paramList.add(actionMap);
        if (intent.getExtras() != null) {
            for (String key : intent.getExtras().keySet()) {
                String value = intent.getStringExtra(key);
                HashMap<String, String> map = new HashMap<>();
                map.put("key", key);
                map.put("value", value);
                TestModeUtil.addLogString("parseIntent", "key = " + key + " , value = " + value);
                paramList.add(map);
            }
        }
        if (paramAdapter != null) {
            paramAdapter.notifyDataSetChanged();
        }
    }

    class ParamAdapter extends SimpleAdapter {

        ParamAdapter(Context context, List<? extends Map<String, ?>> data) {
            super(context, data, android.R.layout.simple_list_item_2, new String[]{"key",
                    "value"}, new int[]{android.R.id.text1, android
                    .R.id.text2});
        }
    }
}
