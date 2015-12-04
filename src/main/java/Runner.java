import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;

import query.QueryResult;
import query.QueryUtils;
import entity.QueryTemplate;
import entity.Report;
import excel.ExportUtils;

/**
 * Выполнитель
 * 
 * Created by o.naumov on 02.12.2015.
 */
public class Runner {

	private static final String PATH_TO_XML = ".\\Queries.xml";
	private static Logger log = Logger.getLogger("Trace");

	public static void main(String[] args) throws SQLException {
		ExportUtils exportUtils = new ExportUtils();
		QueryUtils queryUtils = new QueryUtils();
		Connection connection = null;
		try {

			Report report = parseXml();
			Workbook workbook = exportUtils.createExcelBook();
			connection = queryUtils.createConnection(report.getConnectionInfo());
			for (QueryTemplate template : report.getQueryTemplates()) {
				QueryResult queryResult = queryUtils.executeQuery(connection, template);
				if (queryResult.getResultSet().isBeforeFirst()) {
					exportUtils.exportToSheet(workbook, queryResult);
				} else {
					log.trace("Данных нет \n");
				}
				queryUtils.closeResultSet(queryResult.getResultSet());
			}
			exportUtils.writeWorkbook(workbook, report.getTitle());
		} catch (Exception e) {
			log.error("Возникла ошибка", e);
		} finally {
			queryUtils.closeConnection(connection);
		}

	}

	private static Report parseXml() {
		log.trace(String.format("Считываем исходные данные из: \n\t %s \n", PATH_TO_XML));
		File file = new File(PATH_TO_XML);
		JAXBContext jaxbContext;
		Report report = null;
		try {
			jaxbContext = JAXBContext.newInstance(Report.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			report = (Report) jaxbUnmarshaller.unmarshal(file);
		} catch (JAXBException e) {
			log.trace("Невозможно счтитать исходные данные", e);
		}
		return report;
	}

}