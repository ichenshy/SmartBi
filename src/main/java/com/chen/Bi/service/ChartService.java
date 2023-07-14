package com.chen.Bi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.Bi.model.entity.Chart;

/**
 * @author galax
 * @description 针对表【chart(图表信息表)】的数据库操作Service
 * @createDate 2023-05-21 17:23:58
 */
public interface ChartService extends IService<Chart> {

    void saveOriginalData(String csvData, Chart chart);
    /**
     * 处理错误表更新
     *
     * @param chartId     表id
     * @param execMessage 执行信息
     */
    public void handleChartUpdateError(long chartId, String execMessage);
}
