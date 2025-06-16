package com.realeyez.trademart.request;

public class ContentRange {

    private long start;
    private long end;
    private long size;

    public ContentRange(long start, long end, long size){
        this.start = start;
        this.end = end;
        this.size = size;
    }

    public ContentRange(long start, long end){
        this.start = start;
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public long getSize() {
        return size;
    }
    
    public static ContentRange parse(String range){
        if(range == null || range.isEmpty()){
            return null;
        }
        StringBuilder startBuilder = new StringBuilder();
        StringBuilder endBuilder = new StringBuilder();
        for (int i = 6; i < range.length(); i++) {
            if(range.charAt(i) == '-'){
                break;
            }
            startBuilder.append(range.charAt(i));
        }
        for(int i = startBuilder.length()+7; i < range.length(); i++){
            if(range.charAt(i) == '/'){
                break;
            }
            endBuilder.append(range.charAt(i));
        }
        if(startBuilder.length() + endBuilder.length()+7 == range.length()){
            return new ContentRange(
                    Long.parseLong(startBuilder.toString()),
                    Long.parseLong(endBuilder.toString()));
        }
        StringBuilder sizeBuilder = new StringBuilder();
        for(int i = startBuilder.length() + endBuilder.length()+8; i < range.length(); i++){
            if(range.charAt(i) == '\0' || range.charAt(i) == '\n'){
                break;
            }
            sizeBuilder.append(range.charAt(i));
        }
        return new ContentRange(
                Long.parseLong(startBuilder.toString()),
                Long.parseLong(endBuilder.toString()),
                Long.parseLong(sizeBuilder.toString()));
    }

}
