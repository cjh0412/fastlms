package com.zerobase.fastlms.admin.service.impl;

import com.zerobase.fastlms.admin.dto.CategoryDto;
import com.zerobase.fastlms.admin.entity.Category;
import com.zerobase.fastlms.admin.model.CategoryInput;
import com.zerobase.fastlms.admin.repository.CategoryRepository;
import com.zerobase.fastlms.admin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    // findAll에서 사용할 정렬 메소드
    private Sort getSortBySortValueDesc(){
        return Sort.by(Sort.Direction.DESC, "sortValue");
    };

    @Override
    public List<CategoryDto> list() {
        List<Category> categories = categoryRepository.findAll(getSortBySortValueDesc());
        return CategoryDto.of(categories);
    }

    /**
     * 신규추가
     *
     * @param categoryName
     */
    @Override
    public boolean add(String categoryName) {
        //추가
        Category category = Category.builder()
                .categoryName(categoryName)
                .usingYn(true)
                .sortValue(0)
                .build();
        categoryRepository.save(category);

        //카테고리명 중복 체크

        return true;
    }

    /**
     * 수정
     *
     * @param param
     */
    @Override
    public boolean update(CategoryInput param) {
        Optional<Category> optionalCategory = categoryRepository.findById(param.getId());
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setCategoryName(param.getCategoryName());
            category.setSortValue(param.getSortValue());
            category.setUsingYn(param.isUsingYn());
            categoryRepository.save(category);
        }
        return true;
    }

    /**
     * 삭제
     *
     * @param id
     */
    @Override
    public boolean del(long id) {
        categoryRepository.deleteById(id);
        return true;
    }
}
