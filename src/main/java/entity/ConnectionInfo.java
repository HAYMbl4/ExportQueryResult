package entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Класс содержащий в себе информацию о параметрах подключения к БД. Заполняется из <strong>xml</strong>.
 * 
 * Created by o.naumov on 03.12.2015.
 */
@XmlRootElement(name = "Connection")
public class ConnectionInfo {

	private String ip;
	private String port;
	private String db;
	private String user;
	private String password;

	/**
	 * @return ip адрес для подключения к БД.
	 */
	public String getIp() {
		return ip;
	}

	@XmlElement(required = true)
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return порт для поюключения к БД.
	 */
	public String getPort() {
		return port;
	}

	@XmlElement(required = true)
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * @return название БД(SID).
	 */
	public String getDb() {
		return db;
	}

	@XmlElement(required = true)
	public void setDb(String db) {
		this.db = db;
	}

	/**
	 * @return пользователь, под которым будет осуществляться подключение.
	 */
	public String getUser() {
		return user;
	}

	@XmlElement(required = true)
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return пароль для подключения.
	 */
	public String getPassword() {
		return password;
	}

	@XmlElement(required = true)
	public void setPassword(String password) {
		this.password = password;
	}

}