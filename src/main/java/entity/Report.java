package entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Класс содержащий в себе список {@link QueryTemplate}. Заполняется из <strong>xml</strong>.
 *
 * Created by o.naumov on 02.12.2015.
 */
@XmlRootElement(name = "Report")
public class Report {

	private String title;
	private ConnectionInfo connectionInfo;
	private List<QueryTemplate> queryTemplates;

	/**
	 * @return Название для отчета, приписывая в начало текущую дату.
	 */
	public String getTitle() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
		return String.format("%s - %s", dateFormat.format(new Date()), title);
	}

	@XmlAttribute(required = true)
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return класс описывающий параметры подключения к БД.
	 */
	public ConnectionInfo getConnectionInfo() {
		return connectionInfo;
	}

	@XmlElement(name = "Connection", required = true)
	public void setConnectionInfo(ConnectionInfo connectionInfo) {
		this.connectionInfo = connectionInfo;
	}

	/**
	 * @return Список {@link QueryTemplate} заполненный из <strong>xml</strong> если данных нет, то пустую коллекцию.
	 */
	public List<QueryTemplate> getQueryTemplates() {
		if (queryTemplates == null) {
			queryTemplates = new ArrayList<QueryTemplate>();
		}
		return queryTemplates;
	}

	@XmlElementWrapper(name = "Queries")
	@XmlElement(name = "Query", required = true)
	public void setQueryTemplates(List<QueryTemplate> queryTemplates) {
		this.queryTemplates = queryTemplates;
	}

}