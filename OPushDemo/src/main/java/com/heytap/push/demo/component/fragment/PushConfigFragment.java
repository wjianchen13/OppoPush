package com.heytap.push.demo.component.fragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.heytap.push.demo.R;
import com.heytap.push.demo.component.DemoActivity;
import com.heytap.push.demo.util.LogUtil;
import com.heytap.push.demo.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <p>Title:${Title} </p>
 * <p>Description: PushInfoFragment</p>
 * <p>Copyright (c) 2016 www.oppo.com Inc. All rights reserved.</p>
 * <p>Company: OPPO</p>
 *
 * @author QuWanxin
 * @version 1.0
 *          2017/7/27
 */

public class PushConfigFragment extends BaseFragment {

    private static final String PREF_KEY_PUSH_DAYS_IN_WEEK = "week_days";
    private static final String PREF_KEY_PUSH_TIME_SLOT = "time_slot";

    private List<Integer> pushTime = Arrays.asList(0, 0, 23, 59);
    // private List<Integer> weekDays = Arrays.asList(1, 2, 3, 4, 5);
    private TextView startClock;
    private TextView endClock;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_push_config, null);
    }


    private void saveIntList(String key, List<Integer> list) {
        PreferenceUtil.putString(getActivity(), key, Arrays.toString(list.toArray()));
    }

    private List<Integer> loadIntList(String key) {
        List<Integer> list = new ArrayList<>();
        Matcher match = Pattern.compile("[\\d]+").matcher(PreferenceUtil.getString(getActivity(), key, ""));
        while (match.find()) {
            list.add(Integer.parseInt(match.group()));
        }
        return list;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startClock = getView().findViewById(R.id.tv_push_time_start);
        endClock = getView().findViewById(R.id.tv_push_time_end);
        //?????????????????????
        pushTime = loadIntList(PREF_KEY_PUSH_TIME_SLOT);//??????????????????????????????
        if (pushTime.isEmpty()) pushTime = Arrays.asList(0, 0, 23, 59);
        startClock.setText(String.format(Locale.CHINA, "%02d:%02d", pushTime.get(0), pushTime.get(1)));
        endClock.setText(String.format(Locale.CHINA, "%02d:%02d", pushTime.get(2), pushTime.get(3)));
        ViewGroup viewGroup = getView().findViewById(R.id.ll_weekday);//should not be null
        List<Integer> weekDays = loadIntList(PREF_KEY_PUSH_DAYS_IN_WEEK);
        if (!weekDays.isEmpty()) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof CompoundButton) {
                    ((CompoundButton) child).setChecked(weekDays.contains(Integer.parseInt(child.getTag().toString())));
                }
            }
        }

        startClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        updatePushTime(hour, min, pushTime.get(2), pushTime.get(3));
                    }
                }, pushTime.get(0), pushTime.get(1), true).show();
            }
        });
        endClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        updatePushTime(pushTime.get(0), pushTime.get(1), hour, min);
                    }
                }, pushTime.get(2), pushTime.get(3), true).show();
            }
        });

    }

    private void notifyPushTimeChange(boolean changeByUser) {
        startClock.setText(String.format(Locale.CHINA, "%02d:%02d", pushTime.get(0), pushTime.get(1)));
        endClock.setText(String.format(Locale.CHINA, "%02d:%02d", pushTime.get(2), pushTime.get(3)));
        saveIntList(PREF_KEY_PUSH_TIME_SLOT, pushTime);
        if (changeByUser) {//??????push sdk?????????????????????????????????????????????
            if (getActivity() instanceof DemoActivity) {
                ((DemoActivity) getActivity()).onPushTimeConfigChange(null);
            }
        }
    }

    private void updatePushTime(int startHour, int startMin, int endHour, int endMin) {
        LogUtil.d(String.format(Locale.CHINA, "push time set to :%02d:%02d -> %02d:%02d", startHour, startMin, endHour, endMin));
        if (endHour * 60 + endMin > startHour * 60 + startMin) {//??????????????????????????????????????????????????????
            if (startHour * 60 + startMin >= 0 && endHour * 60 + endMin < 24 * 60 + 59) {//?????????????????????0-23:59?????????
                pushTime.set(0, startHour);
                pushTime.set(1, startMin);
                pushTime.set(2, endHour);
                pushTime.set(3, endMin);
                notifyPushTimeChange(true);
            } else {
                //should not reach here !
                Toast.makeText(getContext(), "????????????????????????", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "????????????????????????????????????", Toast.LENGTH_SHORT).show();
        }
    }

    public void onPushDaysChange() {
        saveIntList(PREF_KEY_PUSH_DAYS_IN_WEEK, getPushWeekdays());
    }

    public List<Integer> getPushWeekdays() {
        List<Integer> weekDays = new ArrayList<>();
        ViewGroup viewGroup = getView().findViewById(R.id.ll_weekday);
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ToggleButton && ((ToggleButton) child).isChecked()) {
                weekDays.add(Integer.parseInt(child.getTag().toString()));
            }
        }
        return weekDays;
    }

    public List<Integer> getPushTime() {
        return pushTime;
    }


}
