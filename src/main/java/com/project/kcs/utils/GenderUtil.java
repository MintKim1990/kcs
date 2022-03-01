package com.project.kcs.utils;

public class GenderUtil {

    public static String convertGender(String gender) {

        if (gender.equals("male"))
            return "1";

        if (gender.equals("female"))
            return "2";

        if (gender.equals("0"))
            return "1";

        if (gender.equals("1"))
            return "2";

        return "";
    }
}
