package com.commonUtils.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.commonUtils.ExceptionHandler.ColumnNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ExcelSplitter {

    public static Map<String, Workbook> splitFileByColumn(MultipartFile file, String coulumnsDelimaterSeparted, String outputType) throws IOException {
        Workbook originalWorkbook = WorkbookFactory.create(file.getInputStream());
        Sheet originalSheet = originalWorkbook.getSheetAt(0);
        
        
        //extract column indexes
        Map<String,Integer> columns = Arrays.stream(coulumnsDelimaterSeparted.split(","))
        		  .map(String::trim)
        		  .collect(Collectors.toMap(key->key, value->-1));
        
		Map<String,Integer> columnIndexes = getColumnIndexes(columns,originalSheet);
		
        // Map to hold workbooks for each unique column value
        Map<String, Workbook> splitFiles = new LinkedHashMap<>();

        
        // Iterate through rows and split by column value
        for (Row row : originalSheet) {
        		String uniqueRow = columnIndexes.values().stream().map(e->getCellValue(row.getCell(e))).collect(Collectors.joining("-"));
        		splitFiles.computeIfAbsent(uniqueRow, k -> {
                    Workbook wb = new XSSFWorkbook();
                    wb.createSheet("Sheet1");
                    return wb;
                });

                Workbook targetWorkbook = splitFiles.get(uniqueRow);
                Sheet targetSheet = targetWorkbook.getSheetAt(0);
                copyRow(row, targetSheet);
        }
        
        originalWorkbook.close();
        return splitFiles;
    }

    public static ByteArrayOutputStream createZipFromWorkbooks(Map<String, Workbook> workbooks) throws IOException {
        ByteArrayOutputStream zipOutputStream = new ByteArrayOutputStream();
        try (java.util.zip.ZipOutputStream zos = new java.util.zip.ZipOutputStream(zipOutputStream)) {
            for (Map.Entry<String, Workbook> entry : workbooks.entrySet()) {
                String fileName = entry.getKey() + ".xlsx";
                ByteArrayOutputStream workbookStream = new ByteArrayOutputStream();
                entry.getValue().write(workbookStream);

                // Add to ZIP
                zos.putNextEntry(new java.util.zip.ZipEntry(fileName));
                zos.write(workbookStream.toByteArray());
                zos.closeEntry();
            }
        }
        return zipOutputStream;
    }

    private static void copyRow(Row sourceRow, Sheet targetSheet) {
        Row targetRow = targetSheet.createRow(targetSheet.getLastRowNum() + 1);

        for (Cell sourceCell : sourceRow) {
            Cell targetCell = targetRow.createCell(sourceCell.getColumnIndex());
            switch (sourceCell.getCellType()) {
                case STRING -> targetCell.setCellValue(sourceCell.getStringCellValue());
                case NUMERIC -> targetCell.setCellValue(sourceCell.getNumericCellValue());
                case BOOLEAN -> targetCell.setCellValue(sourceCell.getBooleanCellValue());
                case FORMULA -> targetCell.setCellFormula(sourceCell.getCellFormula());
                default -> targetCell.setCellValue(sourceCell.toString());
            }
        }
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "Unknown";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "Unknown";
        }
    }
    
   public static Map<String,Integer> getColumnIndexes(Map<String,Integer> columnsMap,Sheet originalSheet) throws ColumnNotFoundException{
    	Row headerRow = originalSheet.getRow(0);
    	Map<String,Integer> columnsWithIndexes = new HashMap<>();
    	
    	for (Cell sourceCell : headerRow) {
            switch (sourceCell.getCellType()) {
                case STRING  -> columnsWithIndexes.put(sourceCell.getStringCellValue(), sourceCell.getColumnIndex());
                case NUMERIC -> columnsWithIndexes.put(String.valueOf(sourceCell.getNumericCellValue()), sourceCell.getColumnIndex());
                case BOOLEAN -> columnsWithIndexes.put(String.valueOf(sourceCell.getBooleanCellValue()), sourceCell.getColumnIndex());
                case FORMULA -> columnsWithIndexes.put(String.valueOf(sourceCell.getCellFormula()), sourceCell.getColumnIndex());
                default -> throw new IllegalArgumentException("Unexpected value: " + sourceCell.getCellType());
            }
        }
    	
    	columnsMap.forEach((k,v)->{
    		if(columnsWithIndexes.containsKey(k)) {
    			columnsMap.put(k, columnsWithIndexes.get(k));
    		}else {
    			throw new ColumnNotFoundException(k);
    		}
    	});
    	
    	return columnsMap;
    	
    }
}