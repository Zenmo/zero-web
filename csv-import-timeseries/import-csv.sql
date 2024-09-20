CREATE TEMPORARY TABLE kwartierwaarden
(
    value FLOAT
);

\copy kwartierwaarden FROM 'time-series-example.csv' DELIMITER ',' CSV HEADER;
-- when running on pgadmin:
-- \copy kwartierwaarden FROM '/var/lib/pgadmin/storage/erik/kwartierwaarden.csv' DELIMITER ',' CSV HEADER;

SELECT *
FROM kwartierwaarden;

INSERT INTO time_series (id, grid_connection_id, type, time_step, unit, value, start)
VALUES (gen_random_uuid(),
        (SELECT grid_connection.id
         FROM grid_connection
                  JOIN address ON grid_connection.address_id = address.id
                  JOIN company_survey ON address.survey_id = company_survey.id
                  -- next release:
                  -- JOIN project ON company_survey.project_id = project.id
         WHERE company_name = 'Atlas'
            -- AND project.name = 'Genius'
            -- AND house_number = 12
            -- Optionally add more conditions
        ),
        'ELECTRICITY_DELIVERY',
        '15 min'::INTERVAL,
        'KWH',
        ARRAY(SELECT value FROM kwartierwaarden),
        '2023-01-01T00:00:00+01');
