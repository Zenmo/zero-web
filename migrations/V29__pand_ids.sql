ALTER TABLE grid_connection
    ADD pand_ids VARCHAR(16)[] DEFAULT ARRAY []::CHARACTER VARYING[] NOT NULL;
