package com.chen.Bi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.Bi.common.ErrorCode;
import com.chen.Bi.common.StateEnum;
import com.chen.Bi.exception.BusinessException;
import com.chen.Bi.mapper.ChartMapper;
import com.chen.Bi.model.entity.Chart;
import com.chen.Bi.service.ChartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author galax
 * @description 针对表【chart(图表信息表)】的数据库操作Service实现
 * @createDate 2023-05-21 17:23:58
 */
@Service
@Slf4j
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
        implements ChartService {
    @Override
    @Transactional
    public void saveOriginalData(String csvData, Chart chart) {
        Long chartId = chart.getId();
        //1、解析CreateTable语句
        try {
            StringBuilder createSql = getCrateTableSql(csvData, chartId);
            baseMapper.createTable(createSql);
            log.debug("执行的创建sql语句" + createSql);
            log.info("创建数据库成功");
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "执行创建sql失败");
        }
        //2、解析Insert语句
        try {
            StringBuilder insertSql = getInsertSql(csvData, chartId);
            baseMapper.insertData(insertSql);
            log.debug("执行的插入sql语句" + insertSql);
            log.info("原始数据插入成功");
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "执行插入sql失败");
        }
    }

    /**
     * 解析数据得到创建表格语句
     *
     * @param csvData csv数据
     * @param chartId 表id
     * @return {@link StringBuilder}
     */
    private StringBuilder getCrateTableSql(String csvData, Long chartId) {
        StringBuilder createSql = new StringBuilder();
        String[] line = csvData.split("\n");
        String[] header = line[0].split(",");
        createSql.append(" CREATE TABLE IF NOT EXISTS chart_").append(chartId).append("(");
        for (int i = 0; i < header.length; i++) {
            createSql.append(header[i]).append(" ").append("varchar(128)").append(" charset utf8 null");
            if (i < header.length - 1) {
                createSql.append(",");
            }
        }
        createSql.append(");");
        return createSql;
    }

    /**
     * 解析数据得到插入数据语句
     *
     * @param csvData 图表数据
     * @param chartId id
     * @return {@link StringBuilder}
     */
    private StringBuilder getInsertSql(String csvData, Long chartId) {
        StringBuilder sql = new StringBuilder();
        String[] line = csvData.split("\n");
        String[] head = line[0].split(",");
        sql.append("INSERT INTO chart_").append(chartId).append("(");
        for (int j = 0; j < head.length; j++) {
            sql.append(head[j]);
            if (j < head.length - 1) {
                sql.append(", ");
            }
        }
        sql.append(") VALUES ");
        for (int i = 1; i < line.length; i++) {
            String[] temp = line[i].split(",");
            sql.append("(");
            for (int j = 0; j < temp.length; j++) {
                sql.append("'").append(temp[j]).append("'");
                if (j < temp.length - 1) {
                    sql.append(", ");
                }
            }
            sql.append(")");
            if (i < line.length - 1) {
                sql.append(", ");
            }
        }
        return sql;
    }

    /**
     * 处理错误表更新
     *
     * @param chartId     表id
     * @param execMessage 执行信息
     */
    public void handleChartUpdateError(long chartId, String execMessage) {
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setStatus(StateEnum.FAILED.getValue());
        updateChartResult.setExecMessage(execMessage);
        boolean updateResult = this.updateById(updateChartResult);
        if (!updateResult) {
            log.error("更新图表失败状态失败" + chartId + "," + execMessage);
        }
    }
}