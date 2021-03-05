package com.example.storage.modules.sys.pojo.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用户关联的人车关系
 */
@Data
@Accessors(chain = true)
public class SelectVehicleListByUserPO {

    /**
     * 车架号
     */
    private String vin;

    /**
     * 车型
     */
    private String brand;

    /**
     * 钥匙类型
     */
    private String keyType;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 可用次数
     */
    private int times;

    /**
     * 钥匙权限
     */
    private Long permissions;

    /**
     * 人车关系id
     */
    private String userVehicleId;

}
