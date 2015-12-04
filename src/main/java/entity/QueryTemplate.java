package entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Класс описывающий шаблон отчета, по которому надо построить выгрузку и експортировать её на страничку
 * <strong>Excel</strong>. Данный класс заполняется из <strong>xml</strong>.
 *
 * Created by o.naumov on 02.12.2015.
 */
@XmlRootElement(name = "Query")
public class QueryTemplate {

	private String description;
	private String template;

	/**
	 * @return Описание запроса.
	 */
	public String getDescription() {
		return description;
	}

	@XmlElement(required = true)
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Текс запроса.
	 */
	public String getTemplate() {
		return template;
	}

	@XmlElement(required = true)
	public void setTemplate(String template) {
		this.template = template;
	}

}