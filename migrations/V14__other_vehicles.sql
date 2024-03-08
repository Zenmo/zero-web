ALTER TABLE grid_connection
    ADD COLUMN has_other_vehicles boolean NULL,
    ADD COLUMN other_vehicles_electric_ratio real NULL;