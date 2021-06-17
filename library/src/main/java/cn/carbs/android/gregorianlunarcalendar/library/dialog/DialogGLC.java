package cn.carbs.android.gregorianlunarcalendar.library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import cn.carbs.android.gregorianlunarcalendar.library.R;
import cn.carbs.android.gregorianlunarcalendar.library.data.ChineseCalendar;
import cn.carbs.android.gregorianlunarcalendar.library.indicator.IndicatorView;
import cn.carbs.android.gregorianlunarcalendar.library.view.GregorianLunarCalendarView;


/**
 * Created by carbs on 2016/7/12.
 */

public class DialogGLC extends Dialog implements View.OnClickListener, IndicatorView.OnIndicatorChangedListener {
    public static final int DEFAULT_CALENDAR_TYPE = 0;
    public static final int CHINESE_CALENDAR_TYPE = 1;


    private Context mContext;
    private IndicatorView mIndicatorView;
    private GregorianLunarCalendarView mGLCView;
    private Button mButtonGetData;
    private TextView tvCancel;
    private TextView tvConfirm;
    public Calendar customCalendar;
    public int calendarType = DEFAULT_CALENDAR_TYPE;
    public DateYMD dateYMD;


    public DialogGLC(Context context) {
        super(context, R.style.dialog);
        mContext = context;
    }

    public DialogGLC(Context context, DateYMD dateYMD, int calendarType) {
        super(context, R.style.dialog);
        mContext = context;
        this.dateYMD = dateYMD;
        this.calendarType = calendarType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_glc);
        initWindow();
        mGLCView = (GregorianLunarCalendarView) this.findViewById(R.id.calendar_view);
//        mGLCView.init();//init has no scroll effect, to today
        mIndicatorView = (IndicatorView) this.findViewById(R.id.indicator_view);
        mIndicatorView.setOnIndicatorChangedListener(this);

        mButtonGetData = (Button) this.findViewById(R.id.button_get_data);
        tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvConfirm = (TextView) findViewById(R.id.tv_confirm);
        tvCancel.setOnClickListener(this);
        tvConfirm.setOnClickListener(this);
        mButtonGetData.setOnClickListener(this);
    }

    public interface OnCancelListener {
        void cancel();
    }

    public interface OnConfirmListener {
        void confirm(SelectDate selectDate);
    }

    OnCancelListener onCancelListener;
    OnConfirmListener onConfirmListener;

    public void setAsListener(OnConfirmListener onConfirmListener, OnCancelListener onCancelListener) {
        this.onConfirmListener = onConfirmListener;
        this.onCancelListener = onCancelListener;
    }


    public static class Build {
        private Context context;

        public Build(Context context) {
            this.context = context;
        }

        /**
         * @param onConfirmListener 确认监听
         * @param onCancelListener  取消监听
         * @param dateYMD           日期 新历 例如
         * @param calendarType      dialog弹出的时候默认选中的类型
         * @return
         */

        public DialogGLC asListener(OnConfirmListener onConfirmListener, OnCancelListener onCancelListener, DateYMD dateYMD, int calendarType) {
            DialogGLC dialogGLC = new DialogGLC(context, dateYMD, calendarType);
            dialogGLC.setAsListener(onConfirmListener, onCancelListener);
            return dialogGLC;
        }

    }

    int getRealMonth(int month) {
        if (month - 1 == 0) {
            return 12;
        } else {
            return month - 1;
        }
    }

    public SelectDate getSelectDate() {
        GregorianLunarCalendarView.CalendarData calendarData = mGLCView.getCalendarData();

        Calendar calendar = calendarData.getCalendar();
        SelectDate selectDate = new SelectDate();
        selectDate.setCalendar(calendar.get(Calendar.YEAR) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH));
        selectDate.setChineseCalendar(calendar.get(ChineseCalendar.CHINESE_YEAR) + "-"
                + (calendar.get(ChineseCalendar.CHINESE_MONTH)) + "-"
                + calendar.get(ChineseCalendar.CHINESE_DATE));
        Object[] ret = mIndicatorView.getCurrIndexAndOffset();
        selectDate.setSelectDateIndex(Integer.parseInt(String.valueOf(ret[0])));
        return selectDate;
    }

    public void initCalendar(DateYMD dateYMD, int calendarType) {
        // mGLCView.init();
        //  Calendar cal =Calendar.getInstance();
       /* cal.setTime(new Date());

        final int last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        cal.set(Calendar.DAY_OF_MONTH, last);*/
        ChineseCalendar instance = new ChineseCalendar(dateYMD.getYear(), getRealMonth(dateYMD.getMonth()), dateYMD.getDay());

        if (calendarType == CHINESE_CALENDAR_TYPE) {
            mGLCView.init(instance, false);
            //设置上面移动那个唯一
            mIndicatorView.setmIndicatorSelectedIndex(1);

            toLunarMode();
        } else {
            mIndicatorView.setmIndicatorSelectedIndex(0);
            mGLCView.init(instance);
            toGregorianMode();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.button_get_data) {
            /*GregorianLunarCalendarView.CalendarData calendarData = mGLCView.getCalendarData();
                Calendar calendar = calendarData.getCalendar();
                String showToast = "Gregorian : " + calendar.get(Calendar.YEAR) + "-"
                        + (calendar.get(Calendar.MONTH) + 1) + "-"
                        + calendar.get(Calendar.DAY_OF_MONTH) + "\n"
                        + "Lunar     : " + calendar.get(ChineseCalendar.CHINESE_YEAR) + "-"
                        + (calendar.get(ChineseCalendar.CHINESE_MONTH)) + "-"
                        + calendar.get(ChineseCalendar.CHINESE_DATE);
                Toast.makeText(mContext.getApplicationContext(), showToast, Toast.LENGTH_LONG).show();*/
        } else if (id == R.id.tv_confirm) {
            GregorianLunarCalendarView.CalendarData calendarData = mGLCView.getCalendarData();

            Calendar calendar = calendarData.getCalendar();
            SelectDate selectDate = new SelectDate();
            selectDate.setCalendar(calendar.get(Calendar.YEAR) + "-"
                    + (calendar.get(Calendar.MONTH) + 1) + "-"
                    + calendar.get(Calendar.DAY_OF_MONTH));
            selectDate.setChineseCalendar(calendar.get(ChineseCalendar.CHINESE_YEAR) + "-"
                    + (calendar.get(ChineseCalendar.CHINESE_MONTH)) + "-"
                    + calendar.get(ChineseCalendar.CHINESE_DATE));
            Object[] ret = mIndicatorView.getCurrIndexAndOffset();
            selectDate.setSelectDateIndex(Integer.parseInt(String.valueOf(ret[0])));
            onConfirmListener.confirm(selectDate);
            dismiss();
        } else if (id == R.id.tv_cancel) {
            dismiss();
            onCancelListener.cancel();
        }
    }

    @Override
    public void show() {
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);
        super.show();
        this.initCalendar(dateYMD, calendarType);

    }


    @Override
    public void onIndicatorChanged(int oldSelectedIndex, int newSelectedIndex) {
        if (newSelectedIndex == 0) {
            toGregorianMode();
        } else if (newSelectedIndex == 1) {
            toLunarMode();
        }
    }

    private void toGregorianMode() {
        mGLCView.toGregorianMode();
    }

    private void toLunarMode() {
        mGLCView.toLunarMode();
    }

    private void initWindow() {
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = (int) (0.90 * getScreenWidth(getContext()));
        if (lp.width > dp2px(getContext(), 480)) {
            lp.width = dp2px(getContext(), 480);
        }
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        win.setAttributes(lp);
    }

    private int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    private int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}