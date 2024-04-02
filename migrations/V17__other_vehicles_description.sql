ALTER TABLE grid_connection
    ADD COLUMN other_vehicles_description CHARACTER VARYING(5000);

UPDATE grid_connection
    SET other_vehicles_description = '' WHERE other_vehicles_description IS NULL;

ALTER TABLE grid_connection
    ALTER COLUMN other_vehicles_description SET NOT NULL;
