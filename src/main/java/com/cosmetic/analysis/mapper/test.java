package com.cosmetic.analysis.mapper;

import com.cosmetic.analysis.entity.SubstanceDescription;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface test {
    @Select("SELECT substance_detected, description FROM substance_description ")
    List<SubstanceDescription> testQuery();
}
