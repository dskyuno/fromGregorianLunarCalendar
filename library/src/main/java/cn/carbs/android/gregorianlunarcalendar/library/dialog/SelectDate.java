package cn.carbs.android.gregorianlunarcalendar.library.dialog;

public class SelectDate {
    //新历的字符串
    private String calendar;
    //农历的字符串
    private String chineseCalendar;
    //选择日期导航的下标 0代表新历 1代表农历
    private int selectDateIndex;

    public String getCalendar() {
        return calendar;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    public String getChineseCalendar() {
        return chineseCalendar;
    }

    public void setChineseCalendar(String chineseCalendar) {
        this.chineseCalendar = chineseCalendar;
    }

    public int getSelectDateIndex() {
        return selectDateIndex;
    }

    public void setSelectDateIndex(int selectDateIndex) {
        this.selectDateIndex = selectDateIndex;
    }
}
