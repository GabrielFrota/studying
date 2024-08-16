package org.example;

import java.util.ArrayList;
import java.util.List;

public class BreakIn50Max {
    public static void main(String[] args) {
        var splits = breakIn50Max(" Lorem    ipsum   dolor    sit amet, consectetur    adipiscing elit, sed do eiusmod tempor incididunt ut labore    et    dolore magna    aliqua.   Ut enim   ad minim veniam,   ");
        for (var s : splits) {
            System.out.println(s);
        }
    }

    private static List<String> breakIn50Max(String s) {
        var ret = new ArrayList<String>();
        var words = s.split("\\s+");
        var sb = new StringBuilder();
        for (var w : words) {
            if (w.isBlank())
                continue;
            if (sb.isEmpty()) {
                sb.append(w);
            } else {
                if (sb.length() + 1 + w.length() <= 50) {
                    sb.append(' ').append(w);
                } else {
                    ret.add(sb.toString());
                    sb.setLength(0);
                    sb.append(w);
                }
            }
        }
        if (!sb.isEmpty()) {
            ret.add(sb.toString());
        }
        return ret;
    }
}
