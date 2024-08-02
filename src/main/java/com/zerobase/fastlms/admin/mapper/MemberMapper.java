package com.zerobase.fastlms.admin.mapper;

import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.admin.model.MemberParam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

// resources/mybatis 매핑
@Mapper
public interface MemberMapper {

    //    List<MemberDto>selectList(MemberDto param);
    List<MemberDto>selectList(MemberParam param);
    long selectListCount(MemberParam param);

    
}
