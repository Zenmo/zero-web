## script to import timeseries (kwartierwaarden)

PGPASSWORD=welkom123 \
    psql \
    --host=holon-webapp-database.postgres.database.azure.com \
    --username=erik \
    --dbname=zero_test \
    --file=import-csv.sql
