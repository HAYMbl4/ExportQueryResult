package query;

import java.sql.ResultSet;
import java.util.List;

/**
 * Класс описывающий результат выполненого запроса. Содержит в себе информацию о:
 * <ul>
 * <li>названии стобцов;</li>
 * <li>результат выгрузки.</li>
 * </ul>
 *
 * Created by o.naumov on 02.12.2015.
 */
public class QueryResult {

	private String description;
	private List<String> columnsName;
	private ResultSet resultSet;

	public QueryResult(String description, List<String> columnsName, ResultSet resultSet) {
		this.description = description;
		this.columnsName = columnsName;
		this.resultSet = resultSet;
	}

	/**
	 * @return Описание запроса.
	 */
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Список названий стобцов.
	 */
	public List<String> getColumnsName() {
		return columnsName;
	}

	/**
	 * @return Результат выгрузки.
	 */
	public ResultSet getResultSet() {
		return resultSet;
	}

}