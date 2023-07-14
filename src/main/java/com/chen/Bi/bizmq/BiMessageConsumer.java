
package com.chen.Bi.bizmq;

import com.chen.Bi.common.ErrorCode;
import com.chen.Bi.common.StateEnum;
import com.chen.Bi.exception.BusinessException;
import com.chen.Bi.manager.AiManager;
import com.chen.Bi.model.entity.Chart;
import com.chen.Bi.service.ChartService;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.chen.Bi.constant.CommonConstant.BI_MODEL_ID;

/**
 * mq 消费者
 *
 * @author CSY
 * @date 2023/06/26
 */
@Component
@Slf4j
public class BiMessageConsumer {
    @Resource
    private ChartService chartService;
    @Resource
    private AiManager aiManager;

    @SneakyThrows //代替try catch
    //queues 队列
    @RabbitListener(queues = {BiMqConstant.BI_QUEUE_NAME}, ackMode = "MANUAL")
    public void receiveMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        if (StringUtils.isBlank(message)) {
            // nack 消息拒绝
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息为空");
        }
        long charId = Long.parseLong(message);
        Chart chart = chartService.getById(charId);
        if (chart == null) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "图表为空");
        }
        Chart updateChart = new Chart();
        updateChart.setId(chart.getId());
        updateChart.setStatus(StateEnum.RUNNING.getValue());
        boolean b = chartService.updateById(updateChart);
        if (!b) {
            channel.basicNack(deliveryTag, false, false);
            chartService.handleChartUpdateError(chart.getId(), "更新图表执行中状态失败");
            return;
        }
        // 调用ai服务
        String result = aiManager.doChat(BI_MODEL_ID, buildUserInput(chart));
        String[] splits = result.split("【【【【【");
        if (splits.length < 3) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 生成错误");
        }
        String genResult = splits[2].trim();
        String genChart = splits[1].trim();
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chart.getId());
        updateChartResult.setGenChart(genChart);
        updateChartResult.setGenResult(genResult);
        updateChartResult.setStatus(StateEnum.SUCCEED.getValue());
        boolean updateResult = chartService.updateById(updateChartResult);
        if (!updateResult) {
            channel.basicNack(deliveryTag, false, false);
            chartService.handleChartUpdateError(chart.getId(), "更新图表成功状态失败");
        }
        log.info("receiveMessage message = {}", message);
        // ack 消息确认
        channel.basicAck(deliveryTag, false);
    }
    private String buildUserInput(Chart chart) {
        String goal = chart.getGoal();
        String csvData = chart.getChartData();
        String chartType = chart.getChartType();
        StringBuilder userInput = new StringBuilder();
        userInput.append("分析需求：").append("\n");
        String userGoal = goal;
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        userInput.append("数据：").append(csvData).append("\n").append("请注意，开头不要给我任何文字解释，直接上代码，图表要有title").append("\n");
        return userInput.toString();
    }
}
