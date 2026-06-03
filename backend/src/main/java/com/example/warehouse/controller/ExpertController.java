package com.example.warehouse.controller;

import com.example.warehouse.entity.AuditLog;
import com.example.warehouse.service.AuditLogService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/audit-log")
public class ExpertController {

    @Resource
    private AuditLogService auditLogService;

    /**
     * Excel 导出接口
     * 前端 GET 请求，携带筛选参数
     */
    @GetMapping("/export")
    public void exportExcel(
            @RequestParam(value = "module", required = false) String module,
            @RequestParam(value = "operator", required = false) String operator,
            HttpServletResponse response
    ) {
        // 1. 查询数据库数据
        List<AuditLog> logList = auditLogService.getLogList(module, operator);

        // 2. 创建 Excel 工作簿
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("操作日志");

        // 3. 创建表头
        Row headRow = sheet.createRow(0);
        headRow.createCell(0).setCellValue("ID");
        headRow.createCell(1).setCellValue("操作人");
        headRow.createCell(2).setCellValue("操作");
        headRow.createCell(3).setCellValue("模块");
        headRow.createCell(4).setCellValue("操作详情");
        headRow.createCell(5).setCellValue("操作时间");

        // 4. 填充数据行
        int rowIndex = 1;
        for (AuditLog log : logList) {
            Row row = sheet.createRow(rowIndex);
            row.createCell(0).setCellValue(log.getId());
            row.createCell(1).setCellValue(log.getUserId());
            row.createCell(2).setCellValue(log.getUsername());
            row.createCell(3).setCellValue(log.getModule());
            row.createCell(4).setCellValue(log.getDetail());
            row.createCell(5).setCellValue(log.getCreatedAt());
            rowIndex++;
        }

        try {
            // 5. 设置响应头，告诉前端这是文件下载
            String fileName = "操作日志.xlsx";
            // 解决中文文件名乱码
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

            // 6. 写出文件流到前端
            OutputStream os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            os.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}