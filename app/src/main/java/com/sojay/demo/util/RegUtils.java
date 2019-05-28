package com.sojay.demo.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegUtils {
    private RegUtils() {
        throw new Error("Do not need instantiate!");
    }

    public static boolean isUserName(String data) {
        return length(data) <= 20;
    }

    public static boolean isPassword(String data) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9!@#$%^&*()_+~ ]{6,20}$");
        Matcher matcher = pattern.matcher(data);
        return matcher.matches();
    }

    public static boolean isPassword_input(String data) {
        Pattern pattern = Pattern.compile("^[\\w!@#$%^&*()_+~ ]+$");
        Matcher matcher = pattern.matcher(data);
        return matcher.matches();
    }

    public static boolean isEmail(String email) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isMobileNumber(String data) {
        if (TextUtils.isEmpty(data)) {
            return false;
        } else {
//            String expr = "^((1[35678][0-9])|(14[5678])|(19[89]))\\d{8}$";
            String expr = "^(1[3456789][0-9])\\d{8}$";
            return data.matches(expr);
        }
    }

    public static boolean isMobileNumber2(String data) {
        if (TextUtils.isEmpty(data)) {
            return false;
        } else {
            String expr = "^((1[135678][0-9])|(14[5678])|(19[89]))\\d{8}$";
            return data.matches(expr);
        }
    }

    public static boolean isNumberLetter(String data) {
        String expr = "^[A-Za-z0-9]+$";
        return data.matches(expr);
    }

    public static boolean isSymbol(String data) {
        String expr = "[`/~!@#$%^&*()+=|{}\':;',\\[\\]\".<>/?~！@#￥%……&*（）——+|{}【】『』「」‘；：”“'。，、？《》-]";
        return data.matches(expr);
    }

    public static boolean isNumber(String data) {
        String expr = "^[0-9]+$";
        return data.matches(expr);
    }

    public static boolean isLetter(String data) {
        String expr = "^[A-Za-z]+$";
        return data.matches(expr);
    }

    public static boolean isChinese(String data) {
        String expr = "^[Α-￥]+$";
        return data.matches(expr);
    }

    public static boolean isContainChinese(String data) {
        String chinese = "[Α-￥]";
        if (TextUtils.isEmpty(data)) {
            for(int i = 0; i < data.length(); ++i) {
                String temp = data.substring(i, i + 1);
                boolean flag = temp.matches(chinese);
                if (flag) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isDianWeiShu(String data, int length) {
        String expr = "^[1-9][0-9]+\\.[0-9]{" + length + "}$";
        return data.matches(expr);
    }

    public static boolean isCard(String data) {
        String expr = "^[0-9]{17}[0-9xX]$";
        return data.matches(expr);
    }

    public static boolean isPostCode(String data) {
        String expr = "^[0-9]{6,10}";
        return data.matches(expr);
    }

    public static boolean isUrl(String data) {
        String expr = "((http|ftp|https)://)(([a-zA-Z0-9\\._-]+\\.[a-zA-Z]{2,6})|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,4})*(/[a-zA-Z0-9\\&%_\\./-~-]*)?";
        return data.matches(expr);
    }

    public static int length(String data) {
        if (data == null) {
            return 0;
        } else {
            char[] c = data.toCharArray();
            int len = 0;

            for(int i = 0; i < c.length; ++i) {
                ++len;
                if (!isLetter(c[i])) {
                    ++len;
                }
            }

            return len;
        }
    }

    public static boolean isLetter(char c) {
        int k = 128;
        return c / k == 0;
    }
}
