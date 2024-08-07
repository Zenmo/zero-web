CREATE TYPE TimeSeriesType AS ENUM ('ELECTRICITY_DELIVERY', 'ELECTRICITY_FEED_IN', 'ELECTRICITY_PRODUCTION', 'GAS_DELIVERY');
CREATE TYPE TimeSeriesUnit AS ENUM ('KWH', 'M3');

CREATE TABLE IF NOT EXISTS time_series
(
    id uuid PRIMARY KEY,
    grid_connection_id uuid NOT NULL,
    "type" TimeSeriesType NOT NULL,
    "timestamp" TIMESTAMP WITH TIME ZONE NOT NULL,
    time_step INTERVAL DEFAULT '15 minutes' NOT NULL,
    unit TimeSeriesUnit NOT NULL,
    "value" REAL[] NOT NULL,
    CONSTRAINT fk_time_series_grid_connection_id__id FOREIGN KEY (grid_connection_id)
        REFERENCES grid_connection (id)
        ON DELETE CASCADE
        ON UPDATE RESTRICT
);

ALTER TABLE "user"
    ADD PRIMARY KEY (id);
