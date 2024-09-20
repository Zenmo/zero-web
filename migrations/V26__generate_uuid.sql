
ALTER TABLE company_survey
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE address
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE grid_connection
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE time_series
    ALTER COLUMN id SET DEFAULT gen_random_uuid();

ALTER TABLE project
    ALTER COLUMN id SET DEFAULT gen_random_uuid();
