## Start PostgresQL database in Docker

- Inspect `Dockerfile`, update username/password if desired
- Build image and give it a tag `docker build -t <image-tag>` 
- Database will be initialised based on `init.sql`, update if desired
- Run container (with volume mount to persist data) `docker run --name <container-name> -p 5432:5432 -v postgres-data:/var/lib/postgresql/data <image-tag>`

## Restart PostgresQL container
You can restart a previously started container using
`docker container restart <container-name>`

