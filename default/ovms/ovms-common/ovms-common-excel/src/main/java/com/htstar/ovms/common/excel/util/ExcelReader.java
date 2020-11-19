package com.htstar.ovms.common.excel.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author HanGuJi
 * @Description: 导出工具类
 * @date 2020/6/1617:26
 */
@Slf4j
public class ExcelReader {

    /**
     * 获取导出流
     * @param fileName
     * @param response
     * @return
     * @throws IOException
     */
    private static OutputStream getOutputStream(String fileName, HttpServletResponse response) throws IOException {
        response.setContentType("application/msexcel");
        response.setHeader("Content-Disposition", "attachment;filename="
                .concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));
        return response.getOutputStream();
    }

    /**
     * 获取导出excel文件
     * @param sheetName
     * @param claz
     * @param list
     * @return
     */
    private static Workbook getWorkbook(String sheetName, Class<?> claz, List<?> list) {
        return ExcelExportUtil.exportExcel(new ExportParams(null, sheetName),
                claz, list);
    }

    /**
     * 只有一行标题列的导出
     * @param fileName
     * @param response
     * @param sheetName
     * @param claz
     * @param list
     */
    public static void onlyOneHeadingExport(String fileName, HttpServletResponse response, String sheetName, Class<?> claz, List<?> list) {
        OutputStream out = null;
        try {
            out = getOutputStream(fileName, response);
            Workbook workbook = getWorkbook(sheetName, claz, list);
            workbook.write(out);
        } catch (IOException e) {
            log.error("导出出现错误", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("关闭流错误", e);
                }
            }
        }
    }




}
