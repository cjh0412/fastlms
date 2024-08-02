package com.zerobase.fastlms.admin.service;

import com.zerobase.fastlms.admin.dto.CategoryDto;
import com.zerobase.fastlms.admin.model.CategoryInput;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> list();

    /**
     * 신규추가
     */
    boolean add(String categoryName);

    /**
     * 수정
     */
    boolean update(CategoryInput param);

    /**
     * 삭제
     */
    boolean del(long id);
}
