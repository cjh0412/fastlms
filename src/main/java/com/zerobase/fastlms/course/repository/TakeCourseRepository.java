package com.zerobase.fastlms.course.repository;


import com.zerobase.fastlms.course.entity.TakeCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface TakeCourseRepository extends JpaRepository<TakeCourse, Long> {



    //강좌신청여부 체크
    long countByCourseIdAndUserIdAndStatusIn(Long id, String userId, Collection<String> statusList);


}
