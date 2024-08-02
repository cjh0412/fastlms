package com.zerobase.fastlms.admin.model;

import lombok.Data;

@Data
public class CommonParam {
    String SearchType;
    String SearchValue;
    
    long pageIndex;
    long pageSize; //한 페이지의 개수

    public long getPageStart(){
        init();
        return (pageIndex - 1) * pageSize ;
    }

    public long getPageEnd(){
        init();
        return pageSize ;
    }

    public void init(){
        if(pageIndex < 1){
            pageIndex = 1;
        }

        if(pageSize < 10){
            pageSize = 10;
        }

    }

    public String getQueryString() {
        init();
        StringBuilder sb = new StringBuilder();

        if(SearchType != null && SearchType.length() > 0){
            sb.append(String.format("searchType=%s",SearchType));
        }

        if(SearchValue != null && SearchValue.length() > 0){
            if(sb.length() > 0){
                sb.append("&");
            }
            sb.append(String.format("SearchValue=%s",SearchValue));
        }

        return sb.toString();
    }
}
