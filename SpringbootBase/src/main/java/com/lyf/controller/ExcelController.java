package com.lyf.controller;

import com.lyf.model.ResultInfo;
import com.lyf.service.ExcelService;
import com.lyf.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("excel")
public class ExcelController {

    @Autowired
    private ExcelService excelService;

    @PostMapping("/upload")
    public ResultInfo uploadExcel(MultipartFile file) {
        excelService.importExcel(file);
        return Result.success();
    }

    @RequestMapping("/download")
    public void downloadExcel(HttpServletResponse response){
        excelService.exportExcel(response);
    }


}
