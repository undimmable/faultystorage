# Миграция данных в Faulty Storage
[Faulty Storage](https://teamcity.jetbrains.com/repository/download/TeamCityPluginsByJetBrains_Unsorted_FaultyStorage_BuildServer/.lastSuccessful/faulty-server.jar?guest=true) – это бедовый сервер, но другого в некоторой воображаемой компании Faulty Inc., к сожалению, не предвидится, так что сегодня придётся поработать с таким.

По ссылке выше находится jar-файл Faulty-Storage сервера. Приложение написано на восьмой java, поэтому для того, чтобы его запустить, нужно установить jdk версии не меньше 1.8. После запуска командой
```bash
# java -jar faulty-server.jar
```
приложение начинает слушать http-реквесты по адресу `http://localhost:8080`.
 
 OpenAPI документация (описание доступных URL'ов) после старта приложения доступна [по ссылке](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config).
Вам нужно, используя API этого сервера, переместить файлы из oldStorage в newStorage. Стоит помнить о том, что сервер бедовый, так что, все endpoint'ы могут неожиданно ответить ошибкой или зависнуть на некоторое, пусть и не очень большое, время.

## API Faulty Storage

### API Старого хранилища
#### GET <base-url>/oldStorage/files возвращает список файлов в старом хранилище
#### GET <base-url>/oldStorage/files/{filename} возвращает контент файла с указанным именем
#### DELETE <base-url>/oldStorage/files/{filename} удаляет файл с указанным именем

### API Нового хранилища
#### GET <base-url>/newStorage/files возвращает список файлов в новом хранилище
#### GET <base-url>/newStorage/files/{filename} возвращает контент файла с указанным именем
#### DELETE <base-url>/newStorage/files/{filename} удаляет файл с указанным именем
#### POST <base-url>/newStorage/files multipart/form-data загружает файл в новый storage 

## Советы

* Мы хотим понять, как вы решите задачу с точки зрения архитектуры, поэтому не стоит самому писать низкоуровневое сетевое взаимодействие – лучше возьмите какую-нибудь библиотеку, например, Apache HttpClient, это проще и быстрее.