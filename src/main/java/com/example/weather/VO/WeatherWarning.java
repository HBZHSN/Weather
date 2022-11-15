package com.example.weather.VO;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @description：
 * @author： hanxu
 * @create： 2022/11/15 16:30
 */
@Data
public class WeatherWarning {
    Long locate;
    Integer sendStatus;
    String id;
    String sender;//预警发布单位
    String pubTime;//预警发布时间
    String title;//预警标题
    String startTime;//预警开始时间
    String endTime;//预警结束时间
    String status;//预警发布状态
    String severity;//预警严重等级
    String severityColor;//预警严重等级颜色
    String type;//预警类型id
    String typeName;//预警类型名称
    String urgency;//预警紧迫程度
    String certainty;//预警确定性
    String text;//预警文字描述
    String related;//关联预警id
}
