package com.chen.Bi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chen.Bi.model.entity.Chart;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 表映射器
 *
 * @author CSY
 * @date 2023/05/29
 */
public interface ChartMapper extends BaseMapper<Chart> {
    boolean createTable(@Param("createSql") StringBuilder createSql);
    boolean insertData(@Param("insertSql") StringBuilder insertSql);
    @MapKey("")
    List<Map<String, Object>> queryChartData(@Param("querySql") String querySql);
}




