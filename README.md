# Data Migration in Faulty Storage
Faulty Storage is an awful file storage, but it's the only one we have in our Faulty Inc., so you'll have face the reality and work with what you have.

The link above contains the jar with the storage server. It uses java 8, so you'll have to install at least jre somewhere to run it:
```bash
# java -jar faulty-server.jar
```
after start, it listens for http requests on `http://localhost:8080`.

## API Faulty Storage

It also has some OpenAPI docs which you can get from [the link](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config), but for convenience,

### API

#### GET <base-url>/storage/files lists all files
#### GET <base-url>/storage/files/{filename} returns the content of {filename}
#### DELETE <base-url>/storage/files/{filename} deletes the file {filename}
#### POST <base-url>/storage/files multipart/form-data creates new file (it's awful, I told you)

## Task
You need to also support the storage in S3 without changing the server code.

The most convenient way to submit the solutuon is to fork the repo and create pull-request. The solution should have adequate set of tests, including E2E and it would be nice if we could build it ourselves and run it without any errors.


## Hint

* You can use [minio](https://min.io/) for storage, it's fully S3 compatible
* Running E2E tests without errors might mean that S3 should somehow be available during tests
