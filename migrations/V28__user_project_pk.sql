ALTER TABLE user_project
    ADD CONSTRAINT pk_user_project
        PRIMARY KEY (user_id, project_id);

ALTER TABLE survey_deeplink
    ALTER COLUMN id SET DEFAULT gen_random_uuid();
