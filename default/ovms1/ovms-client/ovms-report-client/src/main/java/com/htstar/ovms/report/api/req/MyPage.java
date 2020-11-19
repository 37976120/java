package com.htstar.ovms.report.api.req;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

@Data
public class MyPage<T> extends Page {
    private Double costTotal = 0.00;
    private Integer useTotal = 0;
}
