package com.cs200.checkers;

import java.util.ArrayList;

public class StringUtils {
    public static ArrayList<String> StringSplit(String str, char c) {
        ArrayList<String> result = new ArrayList<String>();
        String s = "";  // for temporarily holding strings
        for (int i = 0; i <  str.length(); i++) {
            if (str.charAt(i) == c) {
                result.add(s);
                s = "";
            } else {
                s += str.charAt(i);
            }
        }

        result.add(s);

        return result;
    }
}
