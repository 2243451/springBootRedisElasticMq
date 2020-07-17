package com.lyf.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lyf.base.BaseService;
import com.lyf.dao.ExcelMapper;
import com.lyf.domain.excel.ExcelEntity;
import com.lyf.util.ExcelUtil;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ExcelService extends BaseService<ExcelEntity> {

	@Autowired
	private ExcelMapper dao;
	/**
	 * excel内容导入db
	 * @param excel
	 * @return
	 */
	public void importExcel(MultipartFile excel) {
		List<ExcelEntity> entities = ExcelUtil.parse(excel, ExcelEntity.class);
		dao.saveBatch(entities);
	}
	/**
	 * db内容写入excel
	 * @throws UnsupportedEncodingException
	 */
	public void exportExcel(HttpServletResponse response) {
		List<ExcelEntity> list = dao.all();
		ExcelUtil.exportExcel(response, list, "学生档案");
	}	 
}

