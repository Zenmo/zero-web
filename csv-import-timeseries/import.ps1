<#
script to import timeseries (kwartierwaarden)
may need to change the script policy:
Set-ExecutionPolicy -ExecutionPolicy Unrestricted
#>

$env:PGPASSWORD = 'welkom123'

& "C:\Program Files\PostgreSQL\16\bin\psql.exe" `
    --host=holon-webapp-database.postgres.database.azure.com `
    --username=erik `
    --dbname=zero_test `
    --file=import-csv.sql
