package com.htstar.ovms.common.excel.util;


import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author HanGuJi
 * @Description:
 * @date 2020/6/179:50
 */
@Slf4j
public class ExcelWriter {
    /**
     * 简单导入  只有一行标题列的 导入
     * @param file
     */
    public static <T> List<T> onlyOneHeadingImport(MultipartFile file, Class<T> clz) {
        List<T> list= null;
        //判断文件是否为空
        if(file==null) return null;
        //进一步判断文件是否为空（即判断其大小是否为0或其名称是否为null）
        if(StringUtils.isEmpty(file.getOriginalFilename()) && file.getSize()==0) return null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(),
                    clz, new ImportParams());
        } catch (Exception e) {
            log.info(clz+"导入失败",e);
        }
        return list;
    }
}
