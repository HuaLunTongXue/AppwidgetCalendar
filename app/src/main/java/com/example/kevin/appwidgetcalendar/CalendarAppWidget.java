package com.example.kevin.appwidgetcalendar;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kevin
 * on 2017/4/27 0027.
 */

public class CalendarAppWidget extends AppWidgetProvider {

    private static final String TAG = CalendarAppWidget.class.getSimpleName();
    private static final String UPDATE_ACTION = "com.example.kevin.appwidgetcalendar.update";
    private SimpleDateFormat mSimpleDateFormat;
    private Date mDate;
    private Date mCurrentDate;
    private RemoteViews mRemoteViewsRoot;
    private RemoteViews mRemoteViewsTitle;
    private RemoteViews remoteViewsDay;
    private RemoteViews remoteViewsWeek;
    private int[] tvId={
            R.id.tv1,
            R.id.tv2,
            R.id.tv3,
            R.id.tv4,
            R.id.tv5,
            R.id.tv6,
            R.id.tv7};

    private int[] tvId_bg={
            R.id.tv1_bg,
            R.id.tv2_bg,
            R.id.tv3_bg,
            R.id.tv4_bg,
            R.id.tv5_bg,
            R.id.tv6_bg,
            R.id.tv7_bg};

    private int i = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Log.e(TAG,"onReceive getAction="+intent.getAction());
        if(UPDATE_ACTION.equals(intent.getAction())){
            //// TODO: 2017/5/4 0004 后续有时间做吧
        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.e(TAG,"onUpdate()");

        super.onUpdate(context, appWidgetManager, appWidgetIds);

        mSimpleDateFormat = new SimpleDateFormat("yyyy年MM月");
        mDate = new Date();
        mCurrentDate = new Date();

        //根布局
        if(mRemoteViewsRoot==null){
            mRemoteViewsRoot = new RemoteViews(context.getPackageName(),R.layout.widget_layout);
        }

        //日期 年 月
        if(mRemoteViewsTitle==null){
            mRemoteViewsTitle = new RemoteViews(context.getPackageName(),R.layout.title_calendar);
            mRemoteViewsTitle.setTextViewText(R.id.tvTitle,mSimpleDateFormat.format(mDate));
        }

        // 天
        if(remoteViewsDay==null){
            remoteViewsDay = new RemoteViews(context.getPackageName(),R.layout.day_calendar);
        }

        // 星期
        if(remoteViewsWeek==null){
            remoteViewsWeek = new RemoteViews(context.getPackageName(),R.layout.week_calendar);
        }
        if(mRemoteViewsRoot!=null){
            mRemoteViewsRoot.removeAllViews(R.id.tableLayoutCalendar);
        }
        mRemoteViewsTitle.setTextViewText(R.id.tvShow,String.valueOf(i++));
        mRemoteViewsRoot.addView(R.id.tableLayoutCalendar,mRemoteViewsTitle);
        getDate(mDate,context);
        ComponentName componentName = new ComponentName(context,CalendarAppWidget.class);
        appWidgetManager.updateAppWidget(componentName,mRemoteViewsRoot);

    }

    private void getDate(Date nowDate,Context context) {

        Calendar cad = Calendar.getInstance();
        cad.setTime(nowDate);
        int day_month = cad.getActualMaximum(Calendar.DAY_OF_MONTH); //获取当月天数
        int[][] array = new int[6][7];
        for (int i = 0; i <= day_month - 1; i++) {               //循环遍历每天
            cad.set(Calendar.DAY_OF_MONTH, i + 1);
            int week_month = cad.get(Calendar.WEEK_OF_MONTH);  //获取改天在本月的第几个星期，也就是第几行
            int now_day_month = cad.get(Calendar.DAY_OF_WEEK);   //获取该天在本星期的第几天  ，也就是第几列
            array[week_month - 1][now_day_month - 1] = i + 1;           //将该天存放到二位数组中
        }

        String[] weeks = {"日", "一", "二", "三", "四", "五", "六"};
        for (int i = 0; i < weeks.length; i++) {
            remoteViewsWeek.setTextViewText(tvId[i],weeks[i]);
        }
        mRemoteViewsRoot.addView(R.id.tableLayoutCalendar,remoteViewsWeek);

        remoteViewsDay = new RemoteViews(context.getPackageName(),R.layout.day_calendar);
        //显示背景
        remoteViewsDay.setViewVisibility(R.id.frameLayoutBg, View.VISIBLE);
        for (int i = 0; i <= array.length - 1; i++) {

            for (int j = 0; j <= array[i].length - 1; j++) {
                if (array[i][j] != 0) {
                    remoteViewsDay.setTextViewText(tvId[j],String.valueOf(array[i][j]));
                    remoteViewsDay.setViewVisibility(tvId_bg[j], View.INVISIBLE);
                    if (array[i][j] == mCurrentDate.getDate() &&
                            nowDate.getMonth() == mCurrentDate.getMonth() &&
                            nowDate.getYear() == mCurrentDate.getYear()) {
                        //如果是当前日期，显示背景
                        remoteViewsDay.setViewVisibility(tvId_bg[j], View.VISIBLE);
                    }

                } else {
                    remoteViewsDay.setTextViewText(tvId[j],"");
                    //如果不是当前日期，不显示
                    remoteViewsDay.setViewVisibility(tvId_bg[j], View.INVISIBLE);
                }

                if ((j + 1) % 7 == 0) {
                    if(array[i][0]==0&&array[i][1]==0&&array[i][2]==0&&array[i][3]==0&&array[i][4]==0&&array[i][5]==0&&array[i][6]==0){
                        return;
                    }
                    mRemoteViewsRoot.addView(R.id.tableLayoutCalendar,remoteViewsDay);
                    remoteViewsDay = new RemoteViews(context.getPackageName(),R.layout.day_calendar);
                    remoteViewsDay.setViewVisibility(R.id.frameLayoutBg, View.VISIBLE);
                }
            }

        }

    }


}
