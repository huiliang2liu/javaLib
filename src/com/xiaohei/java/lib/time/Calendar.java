package com.xiaohei.java.lib.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Calendar {
    private static final long DAY_MILLIS = 24 * 60 * 60 * 1000;
    private int year;
    private int month;
    private List<CalendarDay> days = new ArrayList<>();
    private String string;
    Map<Long, LunarCalendar.JieQi> map;

    public Calendar(int year, int month) {
        map = LunarCalendar.jieQi(year);
        this.year = year;
        this.month = month;
        int day = TimeUtil.monthDay(month, year);
        long firstMillis = toMillis(1);
        long lastMillis = firstMillis;
        int firstWeek = Week.week(firstMillis);
        if (firstWeek > 0) {
            int lastYear = year;
            int lastMonth = month - 1;
            if (lastMonth < 0) {
                lastMonth = 12;
                lastYear--;
            }
            int lastDay = TimeUtil.monthDay(lastMonth, lastYear);
            for (int i = firstWeek; i > 0; i--) {
                CalendarDay calendarDay = new CalendarDay(lastYear, lastMonth, lastDay-i+1, lastMillis - DAY_MILLIS * i);
//                lastDay--;
                days.add(calendarDay);
            }
        }
        for (int i = 1; i <= day; i++) {
            CalendarDay calendarDay = new CalendarDay(year, month, i, lastMillis);
            days.add(calendarDay);
            lastMillis += DAY_MILLIS;
        }
//            days.add(i);
        int lastWeek = Week.week(toMillis(day));
        if (lastWeek < 6) {
            int nextMonth = month + 1;
            int nextYear = year;
            if (nextMonth > 12) {
                nextMonth = 1;
                nextYear++;
            }
            for (int i = 0; i < 6 - lastWeek; i++) {
                CalendarDay calendarDay = new CalendarDay(nextYear, nextMonth, i+1, lastMillis);
                days.add(calendarDay);
                lastMillis += DAY_MILLIS;
            }
        }
    }

    @Override
    public synchronized String toString() {
        if (string == null) {
            StringBuilder sb = new StringBuilder();
            int j = 0;
            for (int i = 0; i < days.size(); i++) {
                j++;
                sb.append(days.get(i)).append("\t");
                if (j % 7 == 0) {
                    sb.append("\n");
                }
            }
            string = sb.toString();
        }
        return string;
    }

    private long toMillis(int day) {
        StringBuilder sb = new StringBuilder();
        sb.append(year).append("-");
        if (month < 10)
            sb.append("0");
        sb.append(month);
        sb.append("-");
        if (day < 10)
            sb.append("0");
        sb.append(day);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(sb.toString()).getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return new Date().getTime();
        }
    }

    private class CalendarDay {
        private int year;
        private int month;
        private int day;
        private long millis;
        private String jieqi;
        private LunarDay lunarDay;

        CalendarDay(int year, int month, int day, long millis) {
            this.year = year;
            this.month = month;
            this.day = day;
            this.millis = millis;
            LunarCalendar.JieQi jieQi = map.get(millis);
            if (jieQi != null)
                jieqi = map.get(millis).jieQi;
            else
                jieqi = "";
            lunarDay = new LunarDay(year, month, day,millis);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(day).append("-").append(jieqi).append(" ").append(lunarDay);
            return sb.toString();
        }
    }

    private static final String[] dayNames = {
            "初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十",
            "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "廿十",
            "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十"};
   private static long time18991222=-2209881600000l;

    private class LunarDay {

        private int year;
        private int month;
        private int day;
        private boolean yangYear;
        private boolean yangMonth;
        private boolean yangDay;
        private String monthName;
        private String monthName1;
        private String monthAnimal;
        private String yearName;
        private String dayName;
        private String dayName1;
        private String dayAnimal;
        private String yearAnimal;
        private String yearWX;
        private String monthWX;
        private String dayWX;
        private boolean leap;

        private LunarDay(int year1, int month1, int day1,long millis) {
            int[] solarToLunar = LunarCalendar.solarToLunar(year1, month1, day1);
            year = solarToLunar[0];
            yangYear = year % 2 == 0;
            month = solarToLunar[1];
            day = solarToLunar[2];
            int dayYear= (int) ((millis-time18991222)/DAY_MILLIS);
//            if(day1==14){
//                System.out.println((millis-time18991222)/DAY_MILLIS);
//                System.out.println(dayYear);
//                System.out.println(millis);
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//                try {
//                     System.out.println(dateFormat.parse("2020-10-14").getTime());
//                     System.out.println(System.currentTimeMillis());
//                } catch (ParseException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }

//            for (int m=1;m<month;m++)
//                dayYear+=LunarCalendar.daysInLunarMonth(year,m);
            int dM=LunarCalendar.dLunarMonth(year,month,solarToLunar[3]);
            yangMonth=dM%2==0;
            yangDay=dayYear%2==0;
            yearAnimal = LunarCalendar.lunarYearToAnimal(year);
            monthAnimal = LunarCalendar.lunarYearToAnimal(dM+4);
            dayAnimal = LunarCalendar.lunarYearToAnimal(dayYear+4);
            monthName = LunarCalendar.lunarMonthName(month);
            yearName = LunarCalendar.lunarYearToGanZhi(year);
            yearWX = LunarCalendar.lunarYearToWuXing(year);
            monthName1 = LunarCalendar.lunarYearToGanZhi(dM+4);
            monthWX = LunarCalendar.lunarYearToWuXing(dM+4);
            dayName1 = LunarCalendar.lunarYearToGanZhi(dayYear+4);
            dayWX = LunarCalendar.lunarYearToWuXing(dayYear+4);
            leap = solarToLunar[3] == 1;
            dayName = dayNames[day - 1];
            if (month1 == 1) {
                if (day1 == 1)
                    dayName = "元旦节";
            } else if (month1 == 2) {
                if (day1 == 14)
                    dayName = "情人节";
            } else if (month1 == 3) {
                if (day1 == 8)
                    dayName = "妇女节";
                else if (day1 == 12)
                    dayName = "植树节";
                else if (day1 == 15)
                    dayName = "消费者权益日";
            }else if(month1==4){
                if(day1==1)
                    dayName="愚人节";
            }else if(month1==5){
                if(day1==1)
                    dayName="劳动节";
                else if(day1==4)
                    dayName="青年节";
                else if(day1==12)
                    dayName="护士节";
            }else if(month1==6){
                if(day1==1)
                    dayName="儿童节";
            }else if(month1==7){
                if(day1==1)
                    dayName="建党节";
            }else if(month1==8){
                if(day1==1)
                    dayName="建军节";
                else if(day1==8)
                    dayName="父亲节";
            }else if(month1==9){
                if(day1==9)
                    dayName="毛泽东逝世纪念";
                else if(day1==10)
                    dayName="教师节";
                else if(day1==28)
                    dayName="孔子诞辰";
            }else if(month1==10){
                if(day1==1)
                    dayName="国庆节";
                else if(day1==6)
                    dayName="老人节";
                else if(day1==24)
                    dayName="联合国日";
                else if(day1==31)
                    dayName="万圣夜";
            }else if(month1==11){
                if(day1==12)
                    dayName="孙中山诞辰纪念";
            }else if(month1==12){
                if(day1==20)
                    dayName="澳门回归纪念";
                else if(day1==25)
                    dayName="圣诞节";
                else if(day1==26)
                    dayName="毛泽东诞辰纪念";
            }
            if(month==1){
                if(day==1)
                    dayName="春节节";
                else if(day==15)
                    dayName="元宵节";
            }else if(month==5){
                if(day==5)
                    dayName="端午节";
            }else if(month==7){
                if(day==7)
                    dayName="七夕节";
                else if(day==15)
                    dayName="中元节";
            }else if(month==8){
                if(day==15)
                    dayName="中秋节";
            }else if(month==9){
                if(day==9)
                    dayName="重阳节";
            }else if(month==12){
                if(day==8)
                    dayName="腊八";
                else if(day==24)
                    dayName="小年";
                else if(day==30)
                    dayName="除夕";
            }
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(year).append(String.format("(%s{%s,%s,%s})", yearName, yearAnimal, yangYear ? "阳" : "阴",yearWX)).append("-")
                    .append(month).append(String.format("(%s,%s,%s,%s,%s,%s)", monthName, leap ? "闰" : "",monthName1,monthAnimal,(yangMonth?"阳":"阴"),monthWX)).append("-").
                    append(day).append(String.format("(%s,%s,%s,%s,%s)", dayName,dayName1,dayAnimal,(yangDay?"阳":"阴"),dayWX));
            return sb.toString();
        }
    }
}
