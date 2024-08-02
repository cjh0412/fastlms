package com.zerobase.fastlms.admin.dto;

import com.zerobase.fastlms.admin.entity.Category;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Data
public class CategoryDto {
    private Long id;
    String categoryName;
    int sortValue;
    boolean usingYn;

    int courseCount;

    public static List<CategoryDto> of (List<Category> categories) {

        if(categories != null){
            List<CategoryDto> categoryList = new ArrayList<>();
            for(Category category : categories){
                categoryList.add(of(category));
            }

            return categoryList;
        }

        return null;
    }

    public static CategoryDto of (Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .sortValue(category.getSortValue())
                .usingYn(category.isUsingYn())
                .build();
    }

}
