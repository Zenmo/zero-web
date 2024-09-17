
CREATE TABLE project (
    id uuid NOT NULL PRIMARY KEY,
    name VARCHAR(50) NOT NULL CONSTRAINT project_name_unique UNIQUE,
    energieke_regio_id INTEGER CONSTRAINT project_energieke_regio_id_unique UNIQUE,
    buurt_codes VARCHAR(50)[] DEFAULT ARRAY []::CHARACTER VARYING[] NOT NULL
);

-- Migrate projects from table project to survey

INSERT INTO project (id, name)
SELECT gen_random_uuid(), project FROM company_survey GROUP BY project;

ALTER TABLE company_survey
    ADD project_id uuid
        CONSTRAINT fk_company_survey_project_id__id
            REFERENCES project
            ON UPDATE RESTRICT ON DELETE RESTRICT;

UPDATE company_survey
SET project_id = (SELECT id FROM project WHERE name = company_survey.project);

ALTER TABLE company_survey
    ALTER COLUMN project_id SET NOT NULL;

ALTER TABLE company_survey
    DROP COLUMN project;

-- Done with project table, now users

CREATE TYPE projectscope AS ENUM ('READ', 'WRITE');

CREATE TABLE user_project (
    user_id uuid NOT NULL
        CONSTRAINT fk_user_project_user_id__id
            REFERENCES "user"
            ON UPDATE RESTRICT ON DELETE RESTRICT,
    project_id uuid NOT NULL
        CONSTRAINT fk_user_project_project_id__id
            REFERENCES project
            ON UPDATE RESTRICT ON DELETE RESTRICT,
    scopes projectscope[] DEFAULT ARRAY []::projectscope[] NOT NULL
);

INSERT INTO user_project (user_id, project_id, scopes)
SELECT
    user_id,
    project.id,
    ARRAY['WRITE', 'READ']::projectscope[]
FROM project
    JOIN (
        SELECT
            id AS user_id,
            UNNEST(projects) AS project
       FROM "user"
    ) AS usr ON usr.project = project.name;

ALTER TABLE "user"
    DROP COLUMN projects;
