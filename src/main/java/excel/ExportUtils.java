package excel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import query.QueryResult;

/**
 * Класс для експорта выгруженных данных в <strong>Excel</strong>.
 *
 * Created by o.naumov on 02.12.2015.
 */
public class ExportUtils {

	private static Logger log = Logger.getLogger("Trace");

	private CellStyle headerCellStyle;
	private CellStyle oddCellStyle;
	private CellStyle evenCellStyle;

	/**
	 * @return Новая <strong>Excel</strong> книга.
	 */
	public Workbook createExcelBook() {
		log.trace("Создаем Excel книгу(Java) \n");
		return new HSSFWorkbook();
	}

	/**
	 * Формирует выгрузку на отдельном листе книги.
	 * 
	 * @param workbook
	 *            книга, в которой будет создан лист.
	 * @param queryResult
	 *            результат запроса, данными которого будет заполнен лист.
	 */
	public void exportToSheet(Workbook workbook, QueryResult queryResult) throws IOException, SQLException {
		log.trace("Записываем данные на страницу");
		Sheet sheet = workbook.createSheet(queryResult.getDescription());
		createSheetHeader(sheet, queryResult.getColumnsName());
		writeResultData(sheet, queryResult);
		setAutoSizeColumn(sheet, queryResult.getColumnsName().size());
	}

	/**
	 * Записывает данные в конкретную книгу
	 *
	 * @param workbook
	 *            книга которую надо создать.
	 * @param title
	 *            заголовок, с которым будет создана книга.
	 */
	public void writeWorkbook(Workbook workbook, String title) throws IOException {
		FileOutputStream outputSteam;
		outputSteam = new FileOutputStream(String.format("%s.xls", title));
		log.trace(String.format("Записываем все выгруженные данные в Excel: \n\t %s.xls \n", title));
		workbook.write(outputSteam);
		outputSteam.close();
	}

	/**
	 * Создает заголовок для листа.
	 *
	 * @param sheet
	 *            лист, для которого формируется заголовок.
	 * @param columnsName
	 *            список названий столбцов.
	 */
	private void createSheetHeader(Sheet sheet, List<String> columnsName) {
		log.trace("Формируем заголовок страницы");
		Row headRow = sheet.createRow(0);
		byte cellNum = 0;
		for (String header : columnsName) {
			Cell headerCell = headRow.createCell(cellNum);
			headerCell.setCellValue(header);
			headerCell.setCellStyle(getHeaderCellStyle(sheet.getWorkbook()));
			sheet.autoSizeColumn(cellNum);
			cellNum++;
		}
	}

	/**
	 * Выгружает данные запроса на страницу книги.
	 *
	 * @param sheet
	 *            страница, на которую надо выгрузить данные.
	 * @param queryResult
	 *            результат запроса, который надо выгрузить.
	 * @throws SQLException
	 */
	private void writeResultData(Sheet sheet, QueryResult queryResult) throws IOException, SQLException {
		log.trace("Записываем результаты выгрузки \n");
		int rowNum = 1;
		while (queryResult.getResultSet().next()) {
			Row row = sheet.createRow(rowNum);
			for (byte cellNum = 0; cellNum < queryResult.getColumnsName().size(); cellNum++) {
				Cell cell = row.createCell(cellNum);
				cell.setCellValue(queryResult.getResultSet().getString(cellNum + 1));
				cell.setCellStyle(rowNum % 2 == 1 ? getOddCellStyle(sheet.getWorkbook()) : getEvenCellStyle(sheet
						.getWorkbook()));
			}
			rowNum++;
		}
	}

	/**
	 * @param workbook
	 *            книга, к кторой применяются стили.
	 * @return Стиль для ячеек, которые являюся загаловком.
	 */
	private CellStyle getHeaderCellStyle(Workbook workbook) {
		if (headerCellStyle == null) {
			headerCellStyle = workbook.createCellStyle();

			// заливка
			headerCellStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
			headerCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

			// границы
			setBorderFor(headerCellStyle, CellStyle.BORDER_MEDIUM, IndexedColors.BLACK.getIndex());
		}
		return headerCellStyle;
	}

	/**
	 * @param workbook
	 *            книга, к кторой применяются стили.
	 * @return Стиль для четных ячеек.
	 */
	private CellStyle getOddCellStyle(Workbook workbook) {
		if (oddCellStyle == null) {
			oddCellStyle = workbook.createCellStyle();

			// границы
			setBorderFor(oddCellStyle, CellStyle.BORDER_THIN, IndexedColors.BLACK.getIndex());
		}
		return oddCellStyle;
	}

	/**
	 * @param workbook
	 *            книга, к кторой применяются стили.
	 * @return Стиль для нечетных ячеек.
	 */
	private CellStyle getEvenCellStyle(Workbook workbook) {
		if (evenCellStyle == null) {
			evenCellStyle = workbook.createCellStyle();

			// Заливка
			evenCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			evenCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

			// границы
			setBorderFor(evenCellStyle, CellStyle.BORDER_THIN, IndexedColors.BLACK.getIndex());
		}
		return evenCellStyle;
	}

	/**
	 * Устанавливает границу(выделение ячейки)
	 * 
	 * @param cellStyle
	 *            стиль ячейки, к которой добавляется граница.
	 * @param borderTypeIndex
	 *            индекс типа границы(жирный, курсив и тд.)
	 * @param borderColorIndex
	 *            индекс цвета границы.
	 */
	private void setBorderFor(CellStyle cellStyle, short borderTypeIndex, short borderColorIndex) {
		cellStyle.setBorderBottom(borderTypeIndex);
		cellStyle.setBottomBorderColor(borderColorIndex);
		cellStyle.setBorderLeft(borderTypeIndex);
		cellStyle.setLeftBorderColor(borderColorIndex);
		cellStyle.setBorderRight(borderTypeIndex);
		cellStyle.setRightBorderColor(borderColorIndex);
		cellStyle.setBorderTop(borderTypeIndex);
		cellStyle.setTopBorderColor(borderColorIndex);
	}

	/**
	 * Делает ширину столбцов равной самому широкому значению в колонке.
	 * 
	 * @param sheet
	 *            страница, для которой изменяется ширина столбцов.
	 * @param countColumn
	 *            кол-во столбцов.
	 */
	private void setAutoSizeColumn(Sheet sheet, int countColumn) {
		for (int colNum = 0; colNum < countColumn; colNum++) {
			sheet.autoSizeColumn(colNum);
		}
	}

}