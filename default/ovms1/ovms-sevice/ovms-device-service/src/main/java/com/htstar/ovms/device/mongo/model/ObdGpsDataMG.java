package com.htstar.ovms.device.mongo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Description: OBD GPS数据MONGDB模型
 * Author: flr
 * Date: Created in 2020/6/16
 * Company: 航通星空
 * Modified By:
 */
@Data
@Document("obdGpsData")
public class ObdGpsDataMG {

    /**
     * 设备编号
     */
    private String deviceSn;
    /**
     * GPS时间，标准时间
     */
    private LocalDateTime gpsTime;

    /**
     * 接收时间，计算机本地时间
     */
    private LocalDateTime rcvTime;

    /**
     * 纬度 保留7位小数点（正负）
     */
    private Double lat;
    /**
     * 经度 保留7位小数点（正负）
     */
    private Double lng;
    /**
     * 速度 公里/小时 保留2位小数点
     */
    private Float speed;
    /**
     * 方向 保留2位小数点
     */
    private Float direction;
    /**
     * 定位标志：0=未定位；1=2D定位；2=3D定位；
     */
    private Integer gpsFlag;
    /**
     * 0：东经；1：西经
     */
    private Integer longitudeWay;
    /**
     * 0：北纬；1：南纬
     */
    private Integer latitudeWay;

    /**
     * 定位星数
     */
    private Integer PositionStarNum;



    //必须强制命名为location,否则mongTemplate不识别,会报错,还有就是point的包别导错了.
    private Point location;

}
