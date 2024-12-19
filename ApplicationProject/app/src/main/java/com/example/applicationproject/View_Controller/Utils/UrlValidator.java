package com.example.applicationproject.View_Controller.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlValidator {
        private static final String URL_REGEX = "^https?://.*$"; // URL phải bắt đầu bằng http:// hoặc https://

        public static boolean isValidUrl(String url) {
            Pattern pattern = Pattern.compile(URL_REGEX);
            Matcher matcher = pattern.matcher(url);
            return matcher.matches();
        }
    }