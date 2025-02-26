package com.zerobase.fastlms.course.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
public class CourseInput {
    long id;
    long categoryId;
    String subject;
    String keyword;
    String summary;
    String contents;
    long price;
    long salePrice;
    String saleEndDtText;


    //첨부파일 저장
    String fileName;
    String urlFileName;

    //삭제를 위한
    String idList; // 문자열전달
    //List<String> ids; // 배열로전달


}
