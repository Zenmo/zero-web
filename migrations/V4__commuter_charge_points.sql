ALTER TABLE grid_connection
    ADD COLUMN num_commuter_charge_points bigint NULL,
    ADD CONSTRAINT chk_grid_connection_unsigned_num_commuter_charge_points CHECK (num_commuter_charge_points BETWEEN 0 AND 4294967295);

