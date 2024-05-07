CREATE TABLE IF NOT EXISTS survey_deeplink (
    id uuid PRIMARY KEY,
    survey_id uuid NOT NULL,
    created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    bcrypt_secret VARCHAR(1000) NOT NULL,
    CONSTRAINT fk_survey_deeplink_survey_id__id FOREIGN KEY (survey_id) REFERENCES company_survey (id) ON DELETE CASCADE ON UPDATE RESTRICT
);
