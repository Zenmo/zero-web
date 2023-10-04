## script to import the full dataset to PostgreSQL

PGPASSWORD=welkom123 \
    psql \
    --host=localhost \
    --username=postgres \
    --dbname=postgres \
    --file=import-csv.sql
