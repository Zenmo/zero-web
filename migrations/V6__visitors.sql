ALTER TABLE grid_connection
    RENAME COLUMN num_daily_car_commuters TO num_daily_car_and_van_commuters;

ALTER TABLE grid_connection
    RENAME COLUMN num_commuter_charge_points TO num_commuter_and_visitor_charge_points;

ALTER TABLE grid_connection
    ADD num_daily_car_visitors BIGINT;

ALTER TABLE grid_connection
    ADD CONSTRAINT chk_grid_connection_unsigned_num_daily_car_visitors CHECK ((num_daily_car_visitors >= 0) AND (num_daily_car_visitors <= '4294967295'::bigint));