package com.lyf.domain.excel;

import com.lyf.annotation.ExcelFiled;
import lombok.Data;

@Data
public class ExcelEntity {

	private String id;
	@ExcelFiled(columnName="姓名", columnNum=0)
	private String name;
	@ExcelFiled(columnName="学号", columnNum=1)
	private String uno;
	@ExcelFiled(columnName="电话", columnNum=2)
	private String phone;
	
}

