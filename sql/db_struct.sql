CREATE TYPE Role AS ENUM ('USER', 'ADMIN');

-- Audit Logs Table
CREATE TABLE IF NOT EXISTS audit_logs (
    id UUID NOT NULL DEFAULT gen_random_uuid(),
    table_name VARCHAR(100) NOT NULL,
    record_id UUID NOT NULL,
    action VARCHAR(20) NOT NULL CHECK (action IN ('INSERT', 'UPDATE', 'DELETE')),
    old_values JSONB NULL,
    new_values JSONB NULL,
    changed_by UUID NULL,
    changed_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    ip_address VARCHAR(45) NULL,
    user_agent TEXT NULL,
    PRIMARY KEY (id)
);

-- Indexes for audit_logs
CREATE INDEX IF NOT EXISTS idx_audit_logs_table_name ON audit_logs (table_name);
CREATE INDEX IF NOT EXISTS idx_audit_logs_record_id ON audit_logs (record_id);
CREATE INDEX IF NOT EXISTS idx_audit_logs_changed_at ON audit_logs (changed_at);

-- Function to handle audit logging
CREATE OR REPLACE FUNCTION log_audit_event()
RETURNS TRIGGER AS $$
DECLARE
    v_old_data JSONB;
    v_new_data JSONB;
    v_user_id UUID;
    v_ip_address VARCHAR(45);
    v_user_agent TEXT;
BEGIN
    -- Get the current user ID from the session if available
    BEGIN
        v_user_id := current_setting('app.current_user_id', true)::UUID;
    EXCEPTION WHEN OTHERS THEN
        v_user_id := NULL;
    END;

    -- Get client IP and user agent if available
    BEGIN
        v_ip_address := current_setting('app.client_ip', true);
    EXCEPTION WHEN OTHERS THEN
        v_ip_address := NULL;
    END;

    BEGIN
        v_user_agent := current_setting('app.user_agent', true);
    EXCEPTION WHEN OTHERS THEN
        v_user_agent := NULL;
    END;

    IF (TG_OP = 'UPDATE') THEN
        v_old_data := to_jsonb(OLD);
        v_new_data := to_jsonb(NEW);
        -- Remove audit fields from the data
        v_old_data := v_old_data - 'createdat' - 'updatedat' - 'created_at' - 'updated_at';
        v_new_data := v_new_data - 'createdat' - 'updatedat' - 'created_at' - 'updated_at';
        
        -- Only log if there are actual changes (excluding audit fields)
        IF v_old_data != v_new_data THEN
            INSERT INTO audit_logs (table_name, record_id, action, old_values, new_values, changed_by, ip_address, user_agent)
            VALUES (TG_TABLE_NAME, NEW.id, 'UPDATE', v_old_data, v_new_data, v_user_id, v_ip_address, v_user_agent);
        END IF;
        RETURN NEW;
    ELSIF (TG_OP = 'DELETE') THEN
        v_old_data := to_jsonb(OLD);
        v_old_data := v_old_data - 'createdat' - 'updatedat' - 'created_at' - 'updated_at';
        INSERT INTO audit_logs (table_name, record_id, action, old_values, changed_by, ip_address, user_agent)
        VALUES (TG_TABLE_NAME, OLD.id, 'DELETE', v_old_data, v_user_id, v_ip_address, v_user_agent);
        RETURN OLD;
    ELSIF (TG_OP = 'INSERT') THEN
        v_new_data := to_jsonb(NEW);
        v_new_data := v_new_data - 'createdat' - 'updatedat' - 'created_at' - 'updated_at';
        INSERT INTO audit_logs (table_name, record_id, action, new_values, changed_by, ip_address, user_agent)
        VALUES (TG_TABLE_NAME, NEW.id, 'INSERT', v_new_data, v_user_id, v_ip_address, v_user_agent);
        RETURN NEW;
    END IF;
    RETURN NULL;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- Function to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    -- noinspection SqlResolve
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TABLE IF NOT EXISTS users
(
    id         UUID      NOT NULL DEFAULT gen_random_uuid(),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    username VARCHAR  NOT NULL UNIQUE
        CONSTRAINT users_username_valid_length CHECK ( LENGTH(username) > 0 ),
    password CHAR(60) NOT NULL,
    email    VARCHAR  NOT NULL UNIQUE
        CONSTRAINT users_email_valid_length CHECK ( LENGTH(email) > 0 AND LENGTH(email) < 128 ),
    role     Role     NOT NULL DEFAULT 'USER' CHECK ( role IN ('USER', 'ADMIN') ),
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_users_username ON users (username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users (email);

CREATE TABLE IF NOT EXISTS projects
(
    id          UUID        NOT NULL DEFAULT gen_random_uuid(),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    user_id     UUID    NOT NULL,
    title       VARCHAR NOT NULL
        CONSTRAINT projects_title_valid_length CHECK ( LENGTH(title) > 0 ),
    description TEXT    NULL
        CONSTRAINT projects_description_valid_length CHECK ( LENGTH(description) > 0 ),
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_projects_user_id ON projects (user_id);

CREATE TABLE IF NOT EXISTS projects_uploads
(
    id         UUID        NOT NULL DEFAULT gen_random_uuid(),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    project_id UUID    NOT NULL,
    url        VARCHAR NOT NULL
        CONSTRAINT valid_length CHECK ( LENGTH(url) > 0 ),
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS IF NOT EXISTS idx_projects_uploads_project_id ON projects_uploads (project_id);

CREATE TABLE IF NOT EXISTS skills
(
    id          UUID        NOT NULL DEFAULT gen_random_uuid(),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    user_id     UUID    NOT NULL,
    name        VARCHAR NOT NULL
        CONSTRAINT skills_name_valid_length CHECK ( LENGTH(name) > 0 ),
    description TEXT    NULL
        CONSTRAINT skills_description_valid_length CHECK ( LENGTH(description) > 0 ),
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_skills_user_id ON skills (user_id);

CREATE TABLE IF NOT EXISTS certificates
(
    id            UUID        NOT NULL DEFAULT gen_random_uuid(),
    created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    user_id       UUID    NOT NULL,
    title         VARCHAR NOT NULL
        CONSTRAINT certificates_title_valid_length CHECK ( LENGTH(title) > 0 ),
    issuer        VARCHAR NOT NULL
        CONSTRAINT certificates_issuer_valid_length CHECK ( LENGTH(issuer) > 0 ),
    credentialId  VARCHAR NOT NULL
        CONSTRAINT certificates_credentialId_valid_length CHECK ( LENGTH(credentialId) > 0 ),
    credentialUrl VARCHAR NOT NULL
        CONSTRAINT certificates_credentialUrl_valid_length CHECK ( LENGTH(credentialUrl) > 0 ),
    image         VARCHAR NOT NULL
        CONSTRAINT certificates_image_valid_length CHECK ( LENGTH(image) > 0 ),
    description   TEXT    NULL
        CONSTRAINT certificates_description_valid_length CHECK ( LENGTH(description) > 0 ),
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_certificates_user_id ON certificates (user_id);

CREATE TABLE IF NOT EXISTS certificates_uploads
(
    id             UUID        NOT NULL DEFAULT gen_random_uuid(),
    created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    certificate_id UUID    NOT NULL,
    url            VARCHAR NOT NULL
        CONSTRAINT certificates_uploads_url_valid_length CHECK ( LENGTH(url) > 0 ),
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_certificates_uploads_certificate_id ON certificates_uploads (certificate_id);

CREATE TABLE IF NOT EXISTS educations
(
    id          UUID        NOT NULL DEFAULT gen_random_uuid(),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    user_id     UUID    NOT NULL,
    title       VARCHAR NOT NULL
        CONSTRAINT educations_title_valid_length CHECK ( LENGTH(title) > 0 ),
    institution VARCHAR NOT NULL
        CONSTRAINT educations_institution_valid_length CHECK ( LENGTH(institution) > 0 ),
    startDate   DATE    NOT NULL,
    endDate     DATE    NOT NULL,
    description TEXT    NULL
        CONSTRAINT valid_length CHECK ( LENGTH(description) > 0 ),
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_educations_user_id ON educations (user_id);

CREATE TABLE IF NOT EXISTS experiences
(
    id          UUID        NOT NULL DEFAULT gen_random_uuid(),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    user_id     UUID    NOT NULL,
    title       VARCHAR NOT NULL
        CONSTRAINT experiences_title_valid_length CHECK ( LENGTH(title) > 0 ),
    position    VARCHAR NULL
        CONSTRAINT experiences_position_valid_length CHECK ( LENGTH(position) > 0 ),
    company     VARCHAR NULL
        CONSTRAINT experiences_company_valid_length CHECK ( LENGTH(company) > 0 ),
    startDate   DATE    NULL,
    endDate     DATE    NULL,
    description TEXT    NULL
        CONSTRAINT valid_length CHECK ( LENGTH(description) > 0 ),
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_experiences_user_id ON experiences (user_id);

CREATE TABLE IF NOT EXISTS languages
(
    id          UUID        NOT NULL DEFAULT gen_random_uuid(),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    user_id     UUID    NOT NULL,
    name        VARCHAR NOT NULL
        CONSTRAINT languages_name_valid_length CHECK ( LENGTH(name) > 0 ),
    proficiency VARCHAR NOT NULL,
    description TEXT    NULL
        CONSTRAINT languages_description_valid_length CHECK ( LENGTH(description) > 0 ),
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_languages_user_id ON languages (user_id);

CREATE TABLE IF NOT EXISTS socials
(
    id         UUID        NOT NULL DEFAULT gen_random_uuid(),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    user_id UUID    NOT NULL,
    name    VARCHAR NOT NULL
        CONSTRAINT socials_name_valid_length CHECK ( LENGTH(name) > 0 ),
    url     VARCHAR NOT NULL
        CONSTRAINT socials_url_valid_length CHECK ( LENGTH(url) > 0 ),
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_socials_user_id ON socials (user_id);

-- Create triggers for all tables

-- Users table triggers
CREATE TRIGGER users_updated_at_trigger
BEFORE UPDATE ON users
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER users_audit_trigger
AFTER INSERT OR UPDATE OR DELETE ON users
FOR EACH ROW EXECUTE FUNCTION log_audit_event();

-- Projects table triggers
CREATE TRIGGER projects_updated_at_trigger
BEFORE UPDATE ON projects
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER projects_audit_trigger
AFTER INSERT OR UPDATE OR DELETE ON projects
FOR EACH ROW EXECUTE FUNCTION log_audit_event();

-- Projects_uploads table triggers
CREATE TRIGGER projects_uploads_updated_at_trigger
BEFORE UPDATE ON projects_uploads
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER projects_uploads_audit_trigger
AFTER INSERT OR UPDATE OR DELETE ON projects_uploads
FOR EACH ROW EXECUTE FUNCTION log_audit_event();

-- Skills table triggers
CREATE TRIGGER skills_updated_at_trigger
BEFORE UPDATE ON skills
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER skills_audit_trigger
AFTER INSERT OR UPDATE OR DELETE ON skills
FOR EACH ROW EXECUTE FUNCTION log_audit_event();

-- Certificates table triggers
CREATE TRIGGER certificates_updated_at_trigger
BEFORE UPDATE ON certificates
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER certificates_audit_trigger
AFTER INSERT OR UPDATE OR DELETE ON certificates
FOR EACH ROW EXECUTE FUNCTION log_audit_event();

-- Certificates_uploads table triggers
CREATE TRIGGER certificates_uploads_updated_at_trigger
BEFORE UPDATE ON certificates_uploads
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER certificates_uploads_audit_trigger
AFTER INSERT OR UPDATE OR DELETE ON certificates_uploads
FOR EACH ROW EXECUTE FUNCTION log_audit_event();

-- Educations table triggers
CREATE TRIGGER educations_updated_at_trigger
BEFORE UPDATE ON educations
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER educations_audit_trigger
AFTER INSERT OR UPDATE OR DELETE ON educations
FOR EACH ROW EXECUTE FUNCTION log_audit_event();

-- Experiences table triggers
CREATE TRIGGER experiences_updated_at_trigger
BEFORE UPDATE ON experiences
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER experiences_audit_trigger
AFTER INSERT OR UPDATE OR DELETE ON experiences
FOR EACH ROW EXECUTE FUNCTION log_audit_event();

-- Languages table triggers
CREATE TRIGGER languages_updated_at_trigger
BEFORE UPDATE ON languages
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER languages_audit_trigger
AFTER INSERT OR UPDATE OR DELETE ON languages
FOR EACH ROW EXECUTE FUNCTION log_audit_event();

-- Socials table triggers
CREATE TRIGGER socials_updated_at_trigger
BEFORE UPDATE ON socials
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER socials_audit_trigger
AFTER INSERT OR UPDATE OR DELETE ON socials
FOR EACH ROW EXECUTE FUNCTION log_audit_event();