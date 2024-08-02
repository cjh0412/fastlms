package com.zerobase.fastlms.admin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {


    @Id // pk 매핑 (@GeneratedValue 가 없는 경우 pk 수동 생성 )
    @GeneratedValue(strategy = GenerationType.IDENTITY) // pk 자동 생성
    private Long id;

    String categoryName;
    int sortValue;
    boolean usingYn;
}
