package query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import entity.ConnectionInfo;
import entity.QueryTemplate;

/**
 * Класс для работы с БД, умеет:
 * <ul>
 * <li>создавать/закрывать подключение;</li>
 * <li>выполнять запросы (<strong>select'ы</strong>)</li>
 * </ul>
 *
 * Created by o.naumov on 02.12.2015.
 */
public class QueryUtils {

	private static final String JDBC_DRIVER = "oracle.jdbc.OracleDriver";

	private static Logger log = Logger.getLogger("Trace");

	/**
	 * После выполнения запроса <strong>connection</strong> не закрывается.
	 * 
	 * @param connection
	 *            <strong>connection</strong> к БД, с которой надо сделать запрос.
	 * @param queryTemplate
	 *            запрос, который надо выполнить(расчитано на <strong>select</strong>).
	 * @return Возвращает сформированный результат по запросу в виде {@link QueryResult}.
	 * @throws SQLException
	 */
	public QueryResult executeQuery(Connection connection, QueryTemplate queryTemplate) throws SQLException {
		Statement statement;
		ResultSet resultSet = null;
		QueryResult queryResult = null;
		try {
			log.trace(String.format("Получаем данные: %s \n %s", queryTemplate.getDescription(),
					queryTemplate.getTemplate()));
			statement = connection.createStatement();
			resultSet = statement.executeQuery(queryTemplate.getTemplate());
			queryResult = new QueryResult(queryTemplate.getDescription(), getColumnsName(resultSet), resultSet);
		} catch (SQLException e) {
			closeResultSet(resultSet);
			log.error("Невозможно выполнить запрос", e);
		}
		return queryResult;
	}

	/**
	 * @param connectionInfo
	 *            параметры подключения к базе {@link ConnectionInfo}
	 * @return Созданное подключение к БД.
	 */
	public Connection createConnection(ConnectionInfo connectionInfo) {
		Connection connection = null;
		try {
			log.trace(String
					.format("Создаем подключение к БД, с параметрами:\n\t ip: %s \n\t port: %s \n\t db: %s \n\t user: %s \n\t password: %s \n",
							connectionInfo.getIp(), connectionInfo.getPort(), connectionInfo.getDb(),
							connectionInfo.getUser(), connectionInfo.getPassword()));
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(
					getConnectionUrl(connectionInfo.getIp(), connectionInfo.getPort(), connectionInfo.getDb()),
					connectionInfo.getUser(), connectionInfo.getPassword());
		} catch (ClassNotFoundException | SQLException e) {
			log.error("Невозможно создать подключение", e);
		}
		return connection;
	}

	/**
	 * Закрывает коннекшин, который был создан через {@link #createConnection}
	 * 
	 * @param connection
	 *            коннекшин, который надо закрыть.
	 * @throws SQLException
	 */
	public void closeConnection(Connection connection) throws SQLException {
		if (connection != null) {
			connection.close();
		}
		log.trace("Подключение к БД закрыто \n");
	}

	public void closeResultSet(ResultSet resultSet) throws SQLException {
		if (resultSet != null) {
			if (resultSet.getStatement() != null) {
				resultSet.getStatement().close();
			}
			resultSet.close();
		}
	}

	/**
	 * Формирует список из названий столбцов для выполненого запроса, данный список необходим для формирования шапки в
	 * выгрузке.
	 *
	 * @param resultSet
	 *            результат выполненого запроса.
	 * @return Список названий стобцов.
	 * @throws SQLException
	 */
	private List<String> getColumnsName(ResultSet resultSet) throws SQLException {
		List<String> columnsName = new ArrayList<>();
		StringBuilder sb = new StringBuilder("\t");
		for (int columnIndex = 1; columnIndex <= resultSet.getMetaData().getColumnCount(); columnIndex++) {
			columnsName.add(resultSet.getMetaData().getColumnName(columnIndex));
			sb.append(String.format("%s%s", resultSet.getMetaData().getColumnName(columnIndex),
					columnIndex != resultSet.getMetaData().getColumnCount() ? ", " : ""));
		}
		log.trace(String.format("Название стобцов: \n\t %s \n", sb.toString()));
		return columnsName;
	}

	/**
	 * @param ip
	 *            адрес для подключения.
	 * @param port
	 *            порт для подключения.
	 * @param db
	 *            база.
	 * @return <strong>URL</strong> для подключения к базе.
	 */
	private String getConnectionUrl(String ip, String port, String db) {
		log.trace(String.format("Сформирован URL для подключения: \n\t jdbc:oracle:thin:@//%s:%s/%s \n", ip, port, db));
		return String.format("jdbc:oracle:thin:@//%s:%s/%s", ip, port, db);
	}

}