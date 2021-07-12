## Start PostgresQL database in Docker

- inspect `Dockerfile` and update properties if desired
- `docker build -t <image-tag>` build image and give it a tag
- `docker run --name <container-name> -p 5432:5432 -v postgres-data:/var/lib/postgresql/data <image-tag>`
- database will be initialised with content of `init.sql`
