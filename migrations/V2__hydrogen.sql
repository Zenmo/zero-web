ALTER TABLE grid_connection
    ADD COLUMN num_planned_hydrogen_trucks bigint NULL,
    ADD COLUMN num_planned_hydrogen_vans bigint NULL,
    ADD COLUMN num_planned_hydrogen_cars bigint NULL,
    ADD CONSTRAINT chk_grid_connection_unsigned_num_planned_hydrogen_trucks CHECK (num_planned_hydrogen_trucks BETWEEN 0 AND 4294967295),
    ADD CONSTRAINT chk_grid_connection_unsigned_num_planned_hydrogen_vans CHECK (num_planned_hydrogen_vans BETWEEN 0 AND 4294967295),
    ADD CONSTRAINT chk_grid_connection_unsigned_num_planned_hydrogen_cars CHECK (num_planned_hydrogen_cars BETWEEN 0 AND 4294967295)
;
