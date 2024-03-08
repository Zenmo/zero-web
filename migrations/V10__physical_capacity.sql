ALTER TABLE grid_connection
    ADD COLUMN grootverbruik_physical_capacity_kw BIGINT NULL,
    ADD CONSTRAINT chk_grid_connection_unsigned_grootverbruik_physical_capacity_kw CHECK (grootverbruik_physical_capacity_kw BETWEEN 0 AND 4294967295);
