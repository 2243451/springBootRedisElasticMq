package com.lyf.util;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lyf.annotation.ExcelFiled;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * excel解析工具
 *
 * @author fujian
 * https://blog.csdn.net/linfujian1999/article/details/86594177
 */
public class ExcelUtil {

    /**
     * 根据clazz定义的column解析excel文件
     *
     * @param excel
     * @param clazz
     */

    public static <T> List<T> parse(MultipartFile excel, Class<T> clazz) {
        try {
            File tmpFile = File.createTempFile(excel.getOriginalFilename().substring(0,
                    excel.getOriginalFilename().lastIndexOf(".")),
                    excel.getOriginalFilename().substring(excel.getOriginalFilename().lastIndexOf(".") + 1));
            excel.transferTo(tmpFile);
            Workbook workbook = getWorkBook(tmpFile);
            Sheet sheet = workbook.getSheetAt(0);
            return parseSheet(sheet, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Workbook getWorkBook(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            if (file.getName().endsWith("xls")) {
                return new HSSFWorkbook(fis);
            }
            if (file.getName().endsWith("xlsx")) {
                return new XSSFWorkbook(fis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fis);
        }
        return null;
    }

    private static <T> List<T> parseSheet(Sheet sheet, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        Map<String, Integer> field2ColNum = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            ExcelFiled anno = field.getDeclaredAnnotation(ExcelFiled.class);
            if (null != anno) {
                field2ColNum.put(field.getName(), anno.columnNum());
            }
        }
        for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            try {
                T object = clazz.newInstance();
                for (String filedName : field2ColNum.keySet()) {
                    int cellNum = field2ColNum.get(filedName);
                    Method method = clazz.getDeclaredMethod("set" + filedName.replaceAll("^\\w",
							String.valueOf(filedName.charAt(0)).toUpperCase()), String.class);
                    if (CellType.STRING != row.getCell(cellNum).getCellTypeEnum()) {
                        method.invoke(object, String.valueOf(row.getCell(cellNum).getNumericCellValue()).replaceAll("\\.\\d+", ""));
                    } else {
                        method.invoke(object, row.getCell(cellNum).getStringCellValue());
                    }
                }
                result.add(object);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    /**
     * 将list中的数据写入excel
     *
     * @param list
     * @return
     */
    public static <T> InputStream exportExcel(List<T> list) {
        InputStream excel = null;
        if (list.isEmpty()) {
            return excel;
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        Row headerRow = sheet.createRow(0);
        Class<?> clazz = list.get(0).getClass();

        Map<String, Integer> field2ColNum = new HashMap<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            ExcelFiled anno = field.getDeclaredAnnotation(ExcelFiled.class);
            if (null != anno) {
                field2ColNum.put(field.getName(), anno.columnNum());
                headerRow.createCell(anno.columnNum()).setCellValue(anno.columnName());
            }
        }
        Set<String> fileds = field2ColNum.keySet();
        for (int i = 0; i < list.size(); i++) {
            Row row = sheet.createRow(i + 1);
            for (String field : fileds) {
                try {
                    Method method = clazz.getDeclaredMethod("get" + field.replaceAll("^\\w", String.valueOf(field.charAt(0)).toUpperCase()));
                    String object = String.valueOf(method.invoke(list.get(i)));
                    row.createCell(field2ColNum.get(field)).setCellValue(object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        for (int i = 0; i < fields.length; i++) {
            sheet.autoSizeColumn(i);
        }

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            excel = new ByteArrayInputStream(outputStream.toByteArray());
            workbook.close();
            return excel;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return excel;
    }

    public static void exportExcel(HttpServletResponse response, List list, String fielname) {

        InputStream in = ExcelUtil.exportExcel(list);
        response.reset();
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("UTF-8");
        try {
            fielname = URLEncoder.encode(fielname+".xlsx", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setHeader("Content-Disposition", "attachment; filename=" + fielname);
        byte[] buffer = new byte[1024];
        int length;
        try {
            while((length = in.read(buffer)) >0) {
                response.getOutputStream().write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
}

