## Getting Started

This project is a small Swing-based Java application that uses JDBC to connect to MySQL. The repository now includes a Gradle build and Flyway migrations so other developers can clone and run the app with minimal setup.

What changed for sharing on GitHub:
- Added `build.gradle` and `settings.gradle` so dependencies (MySQL driver, Flyway) are managed automatically.
- Added a Flyway migration `src/resources/db/migration/V1__create_schema.sql` containing the CREATE TABLE statements.
- Added `src/resources/app.properties.example` — copy this to `src/resources/app.properties` and fill in your DB credentials.
- Updated `shop.Main` to attempt to run Flyway migrations at startup (if Flyway is present on the runtime classpath).

## Quick steps for a teammate who clones this repo

1) Copy the example properties and update credentials

```powershell
Copy-Item src\resources\app.properties.example src\resources\app.properties
# Then edit src\resources\app.properties and set db.username/db.password as needed
```

2) Build (Gradle will download dependencies including the MySQL driver and Flyway):

```powershell
gradle clean shadowJar
```

3) Run migrations + start the app

Option A — run with Gradle (recommended during development):

```powershell
gradle runApp
```

Option B — run the fat JAR created by the Shadow plugin (recommended for distribution):

```powershell
java -jar build\libs\electronics-shop-all-0.1.0.jar
```

## Notes about migrations and DB setup

- The project includes Flyway migrations under `src/resources/db/migration`. Flyway will run automatically when you build/run the app with Gradle or when you run the fat JAR produced by `shadowJar` (because Flyway is on the classpath).
- If you prefer to run the SQL manually (phpMyAdmin or mysql CLI), the migration file `V1__create_schema.sql` is plain SQL and can be applied directly.

Manual SQL example (mysql CLI):

```powershell
# create database if needed
mysql -u root -p -e "CREATE DATABASE IF NOT EXISTS electronic_shop CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# apply migration
mysql -u root -p electronic_shop < src\resources\db\migration\V1__create_schema.sql
```

## Security and best practices

- Do NOT commit `src/resources/app.properties` with real credentials. Use the example file and add your local file to `.gitignore` 
- For production or shared environments, prefer environment variables or a secrets manager rather than a plain properties file.


