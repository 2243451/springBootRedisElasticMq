package com.lyf.controller;

import com.lyf.util.PdfUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/pdf")
public class PdfController {
	
	@RequestMapping("/export")
	public void exportPdf(HttpServletResponse response) throws Exception{
		PdfUtil.exportPdf(response);
	}

}
