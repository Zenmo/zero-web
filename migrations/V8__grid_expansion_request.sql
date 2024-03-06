ALTER TABLE grid_connection
    ADD COLUMN has_expansion_request_at_grid_operator BOOLEAN NULL,
    ADD COLUMN requested_kw BIGINT NULL,
    ADD COLUMN expansion_request_reason CHARACTER VARYING(1000),
    ADD CONSTRAINT chk_grid_connection_unsigned_requested_kw CHECK (requested_kw >= 0 AND requested_kw <= '4294967295'::bigint);
