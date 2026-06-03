package com.example.warehouse.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.warehouse.entity.Goods;
import com.example.warehouse.entity.Stock;
import com.example.warehouse.entity.StockRecord;
import com.example.warehouse.service.GoodsService;
import com.example.warehouse.service.StockRecordService;
import com.example.warehouse.service.StockService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/export")
public class ExportController {

    @Autowired
    private StockService stockService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private StockRecordService stockRecordService;

    @Data
    public static class StockExcelRow {
        @ExcelProperty("商品名称")
        private String goodsName;
        @ExcelProperty("单位")
        private String unit;
        @ExcelProperty("库存数量")
        private Integer num;
        @ExcelProperty("单价")
        private String price;
    }

    @Data
    public static class RecordExcelRow {
        @ExcelProperty("类型")
        private String type;
        @ExcelProperty("商品名称")
        private String goodsName;
        @ExcelProperty("数量")
        private Integer num;
        @ExcelProperty("备注")
        private String remark;
        @ExcelProperty("时间")
        private String createdAt;
    }

    @GetMapping("/stock")
    public void exportStock(HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("库存报表", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        List<Stock> stocks = stockService.list();
        List<Goods> goodsList = goodsService.list();
        Map<Long, Goods> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getId, g -> g));

        List<StockExcelRow> rows = stocks.stream().map(s -> {
            StockExcelRow row = new StockExcelRow();
            Goods g = goodsMap.get(s.getGoodsId());
            row.setGoodsName(g != null ? g.getName() : "未知");
            row.setUnit(g != null ? g.getUnit() : "");
            row.setNum(s.getNum());
            row.setPrice(g != null ? "¥" + g.getPrice() : "");
            return row;
        }).collect(Collectors.toList());

        EasyExcel.write(response.getOutputStream(), StockExcelRow.class).sheet("库存报表").doWrite(rows);
    }

    @GetMapping("/records")
    public void exportRecords(HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("出入库记录", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

        List<StockRecord> records = stockRecordService.list(new QueryWrapper<StockRecord>().orderByDesc("created_at"));
        List<Goods> goodsList = goodsService.list();
        Map<Long, String> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getId, Goods::getName));

        List<RecordExcelRow> rows = records.stream().map(r -> {
            RecordExcelRow row = new RecordExcelRow();
            row.setType(r.getType() == 1 ? "入库" : "出库");
            row.setGoodsName(goodsMap.getOrDefault(r.getGoodsId(), "未知"));
            row.setNum(r.getNum());
            row.setRemark(r.getRemark());
            row.setCreatedAt(r.getCreatedAt() != null ? r.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
            return row;
        }).collect(Collectors.toList());

        EasyExcel.write(response.getOutputStream(), RecordExcelRow.class).sheet("出入库记录").doWrite(rows);
    }
}