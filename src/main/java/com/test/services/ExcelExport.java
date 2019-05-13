package com.test.services;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.test.vms.ExportVM;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

@Component
public class ExcelExport {


    public void export(List<ExportVM> date, HttpServletResponse response) throws Exception {
        ServletOutputStream outputStream = response.getOutputStream();
        ExcelWriter writer = new ExcelWriter(outputStream, ExcelTypeEnum.XLSX);
        try {
            //写第一个sheet, sheet1  数据全是List<String> 无模型映射关系
            Sheet sheet1 = new Sheet(1, 0, ExportVM.class);
            writer.write(date, sheet1);
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=" + URLEncoder.encode("账单" + ".xlsx", "utf-8"));
            outputStream.flush();
        } catch (Exception e) {
            throw new Exception("导出失败");
        } finally {
            writer.finish();
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
