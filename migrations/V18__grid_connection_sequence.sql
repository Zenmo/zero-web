CREATE SEQUENCE IF NOT EXISTS grid_connection_sequence;

ALTER TABLE IF EXISTS grid_connection
    ADD COLUMN sequence integer NOT NULL DEFAULT nextval('grid_connection_sequence'::regclass);
