package com.kcirqueapps.laundryapp.utils;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BanglaUtils {
    //private String[] bengaliDigits = new String[]{"০", "১", "২", "৩", "৪", "৫", "৬", "৭", "৮", "৯"};
    // private String[] englishDigits = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private static String[] monthsBengaliFont = new String[]{"জানুয়ারী", "ফেব্রুয়ারী", "মার্চ", "এপ্রিল", "মে", "জুন", "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"};
    private static String[] monthsEnglishFont = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private static String[] daysBengaliFont = new String[]{"শনিবার", "রবিবার", "সোমবার", "মঙ্গলবার", "বুধবার", "বৃহষ্পতিবার", "শুক্রবার"};
    private static String[] daysEnglishFont = new String[]{"Saturday", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

    @NonNull
    public static String toString(double value) {
        String stringValue = String.valueOf(value);
        String stringValue0 = stringValue.replaceAll("0", "০");
        String stringValue1 = stringValue0.replaceAll("1", "১");
        String stringValue2 = stringValue1.replaceAll("2", "২");
        String stringValue3 = stringValue2.replaceAll("3", "৩");
        String stringValue4 = stringValue3.replaceAll("4", "৪");
        String stringValue5 = stringValue4.replaceAll("5", "৫");
        String stringValue6 = stringValue5.replaceAll("6", "৬");
        String stringValue7 = stringValue6.replaceAll("7", "৭");
        String stringValue8 = stringValue7.replaceAll("8", "৮");
        return stringValue8.replaceAll("9", "৯");
    }

    public static String toString(int value) {
        String stringValue = String.valueOf(value);
        String stringValue0 = stringValue.replaceAll("0", "০");
        String stringValue1 = stringValue0.replaceAll("1", "১");
        String stringValue2 = stringValue1.replaceAll("2", "২");
        String stringValue3 = stringValue2.replaceAll("3", "৩");
        String stringValue4 = stringValue3.replaceAll("4", "৪");
        String stringValue5 = stringValue4.replaceAll("5", "৫");
        String stringValue6 = stringValue5.replaceAll("6", "৬");
        String stringValue7 = stringValue6.replaceAll("7", "৭");
        String stringValue8 = stringValue7.replaceAll("8", "৮");
        return stringValue8.replaceAll("9", "৯");
    }

    public static double toDouble(String value) {
        String stringValue = String.valueOf(value);
        String stringValue0 = stringValue.replaceAll("০", "0");
        String stringValue1 = stringValue0.replaceAll("১", "1");
        String stringValue2 = stringValue1.replaceAll("২", "2");
        String stringValue3 = stringValue2.replaceAll("৩", "3");
        String stringValue4 = stringValue3.replaceAll("৪", "4");
        String stringValue5 = stringValue4.replaceAll("৫", "5");
        String stringValue6 = stringValue5.replaceAll("৬", "6");
        String stringValue7 = stringValue6.replaceAll("৭", "7");
        String stringValue8 = stringValue7.replaceAll("৮", "8");
        String stringValue9 = stringValue8.replaceAll("৯", "9");
        String finalValue = stringValue9.replaceAll(" ", "");
        try {
            return Double.parseDouble(finalValue);
        } catch (Exception e) {
            return 0;
        }

    }

    public static int toInt(String value) {
        String stringValue = String.valueOf(value);
        String stringValue0 = stringValue.replaceAll("০", "0");
        String stringValue1 = stringValue0.replaceAll("১", "1");
        String stringValue2 = stringValue1.replaceAll("২", "2");
        String stringValue3 = stringValue2.replaceAll("৩", "3");
        String stringValue4 = stringValue3.replaceAll("৪", "4");
        String stringValue5 = stringValue4.replaceAll("৫", "5");
        String stringValue6 = stringValue5.replaceAll("৬", "6");
        String stringValue7 = stringValue6.replaceAll("৭", "7");
        String stringValue8 = stringValue7.replaceAll("৮", "8");
        String stringValue9 = stringValue8.replaceAll("৯", "9");
        String finalValue = stringValue9.replaceAll(" ", "");
        try {
            return Integer.parseInt(finalValue);
        } catch (Exception e) {
            return 0;
        }

    }

    public static String todayDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.SATURDAY);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String dateInEnglishFont = dayFromDate(calendar.getTime());
        return (dayInBengaliFont(dateInEnglishFont) + " " + toString(dayOfMonth) + ", " + monthsBengaliFont[month] + " " + toString(year));
    }

    public static String tomorrowDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.setFirstDayOfWeek(Calendar.SATURDAY);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String dateInEnglishFont = dayFromDate(calendar.getTime());
        return (dayInBengaliFont(dateInEnglishFont) + " " + toString(dayOfMonth) + ", " + monthsBengaliFont[month] + " " + toString(year));
    }

    public static String[] collectionDays() {
        Calendar calendar = Calendar.getInstance();
        String[] days = new String[7];
        calendar.setFirstDayOfWeek(Calendar.SATURDAY);
        for (int i = 0; i < 7; i++) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            String dateInEnglishFont = dayFromDate(calendar.getTime());
            if (i == 0) {
                days[i] = "আজ";
            } else if (i == 1) {
                days[i] = "আগামিকাল";
            } else {
                days[i] = (dayInBengaliFont(dateInEnglishFont) + " " + toString(dayOfMonth) + ", " + monthsBengaliFont[month] + " " + toString(year));
            }
            calendar.add(Calendar.DATE, 1);
        }
        return days;
    }

    private static String monthEnglishFont(String month) {
        String monthEnglish = null;
        for (int i = 0; i < 12; i++) {
            if (month.equals(monthsBengaliFont[i])) {
                monthEnglish = monthsEnglishFont[i];
            }
        }
        return monthEnglish;
    }

    public static String[] deliveryDays(String dateStr) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        String collectionDate;
        if (dateStr.equals("আজ")) {
            collectionDate = todayDate();
        } else if (dateStr.equals("আগামিকাল")) {
            collectionDate = tomorrowDate();
        } else {
            collectionDate = dateStr;
        }

        int dateStartPos = collectionDate.indexOf(" ");
        collectionDate = collectionDate.substring(dateStartPos + 1);
        int dayEndPos = collectionDate.indexOf(", ");
        String dayBengali = collectionDate.substring(0, dayEndPos);
        collectionDate = collectionDate.substring(dayEndPos + 2);
        int monthEndPos = collectionDate.indexOf(" ");
        String monthBengali = collectionDate.substring(0, monthEndPos);
        String yearBengali = collectionDate.substring(monthEndPos + 1);

        int dayEnglish = toInt(dayBengali);
        String monthEnglish = monthEnglishFont(monthBengali);
        int yearEnglish = toInt(yearBengali);
        String dateEnglish = monthEnglish + " " + dayEnglish + ", " + yearEnglish;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM dd, yyyy", Locale.ENGLISH);
        Date date = dateFormat.parse(dateEnglish);

        String[] days = new String[7];
        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.SATURDAY);
        calendar.add(Calendar.DATE, 1);
        for (int i = 0; i < 7; i++) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            String dayEnglishFont = dayFromDate(calendar.getTime());
            days[i] = (dayInBengaliFont(dayEnglishFont) + " " + toString(dayOfMonth) + ", " + monthsBengaliFont[month] + " " + toString(year));
            calendar.add(Calendar.DATE, 1);
        }
        return days;
    }

    public static String packageEndDate(String dateStr) {
        Calendar calendar = Calendar.getInstance();
        String collectionDate;
        if (dateStr.equals("আজ")) {
            collectionDate = todayDate();
        } else if (dateStr.equals("আগামিকাল")) {
            collectionDate = tomorrowDate();
        } else {
            collectionDate = dateStr;
        }

        int dateStartPos = collectionDate.indexOf(" ");
        collectionDate = collectionDate.substring(dateStartPos + 1);
        int dayEndPos = collectionDate.indexOf(", ");
        String dayBengali = collectionDate.substring(0, dayEndPos);
        collectionDate = collectionDate.substring(dayEndPos + 2);
        int monthEndPos = collectionDate.indexOf(" ");
        String monthBengali = collectionDate.substring(0, monthEndPos);
        String yearBengali = collectionDate.substring(monthEndPos + 1);

        int dayEnglish = toInt(dayBengali);
        String monthEnglish = monthEnglishFont(monthBengali);
        int yearEnglish = toInt(yearBengali);
        String dateEnglish = monthEnglish + " " + dayEnglish + ", " + yearEnglish;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM dd, yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = dateFormat.parse(dateEnglish);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.setTime(date);
        calendar.setFirstDayOfWeek(Calendar.SATURDAY);
        calendar.add(Calendar.MONTH, 1);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        String dayEnglishFont = dayFromDate(calendar.getTime());
        return (dayInBengaliFont(dayEnglishFont) + " " + toString(dayOfMonth) + ", " + monthsBengaliFont[month] + " " + toString(year));
    }

    private static String dayFromDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        return dateFormat.format(date);
    }

    private static String dayInBengaliFont(String dayEnglishFont) {
        String day = null;
        for (int i = 0; i < 7; i++) {
            if (dayEnglishFont.equals(daysEnglishFont[i])) {
                day = daysBengaliFont[i];
            }
        }
        return day;
    }

    public static String dayRemaining(String endDateStr) {
        long endDate = getDate(endDateStr);
        int dateCount = (int) getDayCount(endDate);
        if (dateCount < 0)
            return "প্যাকেজ এর সময়কাল শেষ";
        else
            return toString(dateCount);
    }

    private static long getDate(String date) {
        Calendar calendar = Calendar.getInstance();
        int dateStartPos = date.indexOf(" ");
        date = date.substring(dateStartPos + 1);
        int dayEndPos = date.indexOf(", ");
        String dayBengali = date.substring(0, dayEndPos);
        date = date.substring(dayEndPos + 2);
        int monthEndPos = date.indexOf(" ");
        String monthBengali = date.substring(0, monthEndPos);
        String yearBengali = date.substring(monthEndPos + 1);

        int dayEnglish = toInt(dayBengali);
        String monthEnglish = monthEnglishFont(monthBengali);
        int yearEnglish = toInt(yearBengali);
        String dateEnglish = monthEnglish + " " + dayEnglish + ", " + yearEnglish;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM dd, yyyy", Locale.ENGLISH);

        try {
            Date parseDate = dateFormat.parse(dateEnglish);
            return parseDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long getDayCount(long endDate) {
        Calendar calendar = Calendar.getInstance();
        long diff = endDate - calendar.getTimeInMillis();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}
