CREATE TYPE MissingPvReason AS ENUM (
    'NO_SUITABLE_ROOF',
    'NO_BACKFEED_CAPACITY',
    'NOT_INTERESTED',
    'OTHER'
);

ALTER TABLE grid_connection
    ADD COLUMN missing_pv_reason MissingPvReason NULL;
