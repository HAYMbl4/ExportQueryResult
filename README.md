###Назвачение

Данная программа позволяет выгрузить несколько запросов описаных в XML в одну книгу, расположив результат каждого запроса на отдельном листе.

###Используемые библиотеки

* ***Maven*** - для сборки проекта;
* ***JAXB*** - для считывания данных из исходной ***XML***'ки;
* ***Apache POI*** - для выгрузки данных в ***Excel***
* ***JDBC**** - для выполнения запросов;
* ***Log4j*** - для логирования.

###Релизы

Для удобства работы выпущен [релиз ver-1.0.0](https://github.com/HAYMbl4/ExportQueryResult/releases/tag/ver-1.0.0), к которому прикреплен архив с ***runner.bat***, который запускает работу программы.

###Как с ней работать

* Программа работает только с базой данных ***Oracle***, но давольно просто адаптируется под остальные БД.
* Все иходныхе данные берутся из ***Queries.xml***

####Структура

```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<Report title="">
	<Connection>
		<ip></ip>
		<port></port>
		<db></db>
		<user></user>
		<password></password>
	</Connection>
	<Queries>
		<Query>
			<description></description>
			<template></template>
		</Query>			
	</Queries>
</Report>
```

####Описание

<ul>
  <li>
    тэг <b><i>Report</b></i> - подразумевает под собой весь отчет в целом(книгу Excel), его вложенные тэги и атрибуты:
  </li>
  <ul>
    <li>
      атрибут <b><i>title</b></i> - заголовок, который используется для названя книги  
    </li>
    <li>
      тэг <b><i>Connection</b></i> - тэг описывающий параметры подключения к БД, на которой будут выполняться запросы      
      <ul>
        <li>      
          тэг <b><i>ip</b></i> - ip адрес для подключения к БД;
        </li>
        <li>      
          тэг <b><i>port</b></i> - порт для подключения к БД;
        </li>
        <li>      
          тэг <b><i>db</b></i> - навзвание БД;
        </li>
        <li>      
          тэг <b><i>user</b></i> - логин для подключения к БД;
        </li>
        <li>      
          тэг <b><i>password</b></i> - пароль для подключения к БД под указаным логином.
        </li>        
      </ul>  
    </li> 
    <li>
      тэг <b><i>Queries</b></i> - тэг оборачивающий в себя N кол-во запросов, которые описываются в ***Query***
      <ul>
        <li>
          тэг <b><i>description</b></i> - описание запроса(используется для наименования листа в книге);
        </li>
        <li>
          тэг <b><i>template</b></i> - текс запроса, без ';' на конце.
        </li>        
      </ul>
    </li>
  </ul>
</ul>

