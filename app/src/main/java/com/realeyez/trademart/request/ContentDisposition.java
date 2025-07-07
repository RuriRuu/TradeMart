package com.realeyez.trademart.request;

import com.realeyez.trademart.util.Logger;
import com.realeyez.trademart.util.Logger.LogLevel;

public class ContentDisposition {

    private String data;

    private ContentDisposition() {
    }

    public ContentDisposition(String data) {
        this.data = data;
    }

    public String getField(String key) {
        return extractSection(key);
    }

    public String getHeader(){
        return data;
    }

    public ContentDisposition addDisposition(String key, String value){
        data = new StringBuilder()
            .append(data)
            .append(data.length() > 1 ? "; " : "")
            .append(key)
            .append("=\"")
            .append(value)
            .append("\"")
            .toString();
        Logger.log("disposition updated: ".concat(data), LogLevel.INFO);
        return this;
    }

    private String extractSection(String key) {
        int foundIndex = -1;
        for (int i = 0; i < data.length(); i++) {
            if (data.charAt(i) != key.charAt(0)) {
                continue;
            }
            for (int j = 1; j < key.length(); j++) {
                if (data.charAt(i + j) == key.charAt(j)) {
                    if (j == key.length() - 1) {
                        foundIndex = j + i + 1;
                        break;
                    }
                    continue;
                }
                break;
            }
            break;
        }
        if (foundIndex == -1) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = foundIndex + 2; i < data.length(); i++) {
            if (data.charAt(i) != '"')
                builder.append(data.charAt(i));
            if (i == data.length() - 1 && data.charAt(i) != '"')
                return "";
        }
        return builder.toString();
    }

    public static ContentDisposition attachment(){
        ContentDisposition disposition = new ContentDisposition();
        disposition.data = "attachment";
        return disposition;
    }

}
