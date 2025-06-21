CREATE TYPE Role AS ENUM ('USER', 'ADMIN');

CREATE TABLE IF NOT EXISTS users
(
    id         UUID        NOT NULL DEFAULT gen_random_uuid(),
    username   VARCHAR     NOT NULL UNIQUE
        CONSTRAINT users_username_valid_length CHECK ( LENGTH(username) > 0 ),
    password   CHAR(60)    NOT NULL,
    email      VARCHAR     NOT NULL UNIQUE
        CONSTRAINT users_email_valid_length CHECK ( LENGTH(email) > 0 AND LENGTH(email) < 128 ),
    role       Role        NOT NULL DEFAULT 'USER' CHECK ( role IN ('USER', 'ADMIN') ),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_users_username ON users (username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users (email);

CREATE TABLE IF NOT EXISTS projects
(
    id          UUID        NOT NULL DEFAULT gen_random_uuid(),
    user_id     UUID        NOT NULL,
    title       VARCHAR     NOT NULL UNIQUE
        CONSTRAINT projects_title_valid_length CHECK ( LENGTH(title) > 0 ),
    description TEXT        NULL
        CONSTRAINT projects_description_valid_length CHECK ( LENGTH(description) > 0 ),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_projects_user_id ON projects (user_id);

CREATE TABLE IF NOT EXISTS projects_uploads
(
    id         UUID        NOT NULL DEFAULT gen_random_uuid(),
    project_id UUID        NOT NULL,
    url        VARCHAR     NOT NULL
        CONSTRAINT valid_length CHECK ( LENGTH(url) > 0 ),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_projects_uploads_project_id ON projects_uploads (project_id);

CREATE TABLE IF NOT EXISTS skills
(
    id          UUID        NOT NULL DEFAULT gen_random_uuid(),
    user_id     UUID        NOT NULL,
    name        VARCHAR     NOT NULL
        CONSTRAINT skills_name_valid_length CHECK ( LENGTH(name) > 0 ),
    description TEXT        NULL
        CONSTRAINT skills_description_valid_length CHECK ( LENGTH(description) > 0 ),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_skills_user_id ON skills (user_id);

CREATE TABLE IF NOT EXISTS certificates
(
    id             UUID        NOT NULL DEFAULT gen_random_uuid(),
    user_id        UUID        NOT NULL,
    title          VARCHAR     NOT NULL UNIQUE
        CONSTRAINT certificates_title_valid_length CHECK ( LENGTH(title) > 0 ),
    issuer         VARCHAR     NOT NULL
        CONSTRAINT certificates_issuer_valid_length CHECK ( LENGTH(issuer) > 0 ),
    credential_id  VARCHAR     NOT NULL
        CONSTRAINT certificates_credentialId_valid_length CHECK ( LENGTH(credential_id) > 0 ),
    credential_url VARCHAR     NULL
        CONSTRAINT certificates_credentialUrl_valid_length CHECK ( LENGTH(credential_url) > 0 ),
    image          VARCHAR     NULL
        CONSTRAINT certificates_image_valid_length CHECK ( LENGTH(image) > 0 ),
    description    TEXT        NULL
        CONSTRAINT certificates_description_valid_length CHECK ( LENGTH(description) > 0 ),
    created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_certificates_user_id ON certificates (user_id);

CREATE TABLE IF NOT EXISTS certificates_uploads
(
    id             UUID        NOT NULL DEFAULT gen_random_uuid(),
    certificate_id UUID        NOT NULL,
    url            VARCHAR     NOT NULL
        CONSTRAINT certificates_uploads_url_valid_length CHECK ( LENGTH(url) > 0 ),
    created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_certificates_uploads_certificate_id ON certificates_uploads (certificate_id);

CREATE TABLE IF NOT EXISTS educations
(
    id          UUID        NOT NULL DEFAULT gen_random_uuid(),
    user_id     UUID        NOT NULL,
    title       VARCHAR     NOT NULL UNIQUE
        CONSTRAINT educations_title_valid_length CHECK ( LENGTH(title) > 0 ),
    institution VARCHAR     NOT NULL
        CONSTRAINT educations_institution_valid_length CHECK ( LENGTH(institution) > 0 ),
    start_date  DATE        NOT NULL,
    end_date    DATE        NOT NULL,
    description TEXT        NULL
        CONSTRAINT valid_length CHECK ( LENGTH(description) > 0 ),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_educations_user_id ON educations (user_id);

CREATE TABLE IF NOT EXISTS experiences
(
    id          UUID        NOT NULL DEFAULT gen_random_uuid(),
    user_id     UUID        NOT NULL,
    title       VARCHAR     NOT NULL UNIQUE
        CONSTRAINT experiences_title_valid_length CHECK ( LENGTH(title) > 0 ),
    position    VARCHAR     NULL
        CONSTRAINT experiences_position_valid_length CHECK ( LENGTH(position) > 0 ),
    company     VARCHAR     NULL
        CONSTRAINT experiences_company_valid_length CHECK ( LENGTH(company) > 0 ),
    start_date  DATE        NULL,
    end_date    DATE        NULL,
    description TEXT        NULL,
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT valid_length CHECK ( LENGTH(description) > 0 ),
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_experiences_user_id ON experiences (user_id);

CREATE TABLE IF NOT EXISTS languages
(
    id          UUID        NOT NULL DEFAULT gen_random_uuid(),
    user_id     UUID        NOT NULL,
    name        VARCHAR     NOT NULL
        CONSTRAINT languages_name_valid_length CHECK ( LENGTH(name) > 0 ),
    proficiency VARCHAR     NOT NULL,
    description TEXT        NULL
        CONSTRAINT languages_description_valid_length CHECK ( LENGTH(description) > 0 ),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_languages_user_id ON languages (user_id);

CREATE TABLE IF NOT EXISTS socials
(
    id         UUID        NOT NULL DEFAULT gen_random_uuid(),
    user_id    UUID        NOT NULL,
    name       VARCHAR     NOT NULL
        CONSTRAINT socials_name_valid_length CHECK ( LENGTH(name) > 0 ),
    url        VARCHAR     NOT NULL
        CONSTRAINT socials_url_valid_length CHECK ( LENGTH(url) > 0 ),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_socials_user_id ON socials (user_id);
