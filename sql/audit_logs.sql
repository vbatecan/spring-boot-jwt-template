-- Audit Logs Table
CREATE TABLE IF NOT EXISTS audit_logs
(
    id         UUID         NOT NULL DEFAULT gen_random_uuid(),
    table_name VARCHAR(100) NOT NULL,
    record_id  UUID         NOT NULL,
    action     VARCHAR(20)  NOT NULL CHECK (action IN ('INSERT', 'UPDATE', 'DELETE')),
    old_values JSONB        NULL,
    new_values JSONB        NULL,
    changed_by UUID         NULL,
    changed_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    ip_address VARCHAR(45)  NULL,
    user_agent TEXT         NULL,
    PRIMARY KEY (id)
);

-- Indexes for audit_logs
CREATE INDEX IF NOT EXISTS idx_audit_logs_table_name ON audit_logs (table_name);
CREATE INDEX IF NOT EXISTS idx_audit_logs_record_id ON audit_logs (record_id);
CREATE INDEX IF NOT EXISTS idx_audit_logs_changed_at ON audit_logs (changed_at);

-- Function to handle audit logging
CREATE OR REPLACE FUNCTION log_audit_event()
    RETURNS TRIGGER AS
$$
DECLARE
    v_old_data   JSONB;
    v_new_data   JSONB;
    v_user_id    UUID;
    v_ip_address VARCHAR(45);
    v_user_agent TEXT;
BEGIN
    -- Get the current user ID from the session if available
    BEGIN
        v_user_id := current_setting('app.current_user_id', true)::UUID;
    EXCEPTION
        WHEN OTHERS THEN
            v_user_id := NULL;
    END;

    -- Get client IP and user agent if available
    BEGIN
        v_ip_address := current_setting('app.client_ip', true);
    EXCEPTION
        WHEN OTHERS THEN
            v_ip_address := NULL;
    END;

    BEGIN
        v_user_agent := current_setting('app.user_agent', true);
    EXCEPTION
        WHEN OTHERS THEN
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
            INSERT INTO audit_logs (table_name, record_id, action, old_values, new_values, changed_by, ip_address,
                                    user_agent)
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
    RETURNS TRIGGER AS
$$
BEGIN
    -- noinspection SqlResolve
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Users table triggers
CREATE TRIGGER users_updated_at_trigger
    BEFORE UPDATE
    ON users
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER users_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE
    ON users
    FOR EACH ROW
EXECUTE FUNCTION log_audit_event();

-- Projects table triggers
CREATE TRIGGER projects_updated_at_trigger
    BEFORE UPDATE
    ON projects
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER projects_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE
    ON projects
    FOR EACH ROW
EXECUTE FUNCTION log_audit_event();

-- Projects_uploads table triggers
CREATE TRIGGER projects_uploads_updated_at_trigger
    BEFORE UPDATE
    ON projects_uploads
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER projects_uploads_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE
    ON projects_uploads
    FOR EACH ROW
EXECUTE FUNCTION log_audit_event();

-- Skills table triggers
CREATE TRIGGER skills_updated_at_trigger
    BEFORE UPDATE
    ON skills
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER skills_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE
    ON skills
    FOR EACH ROW
EXECUTE FUNCTION log_audit_event();

-- Certificates table triggers
CREATE TRIGGER certificates_updated_at_trigger
    BEFORE UPDATE
    ON certificates
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER certificates_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE
    ON certificates
    FOR EACH ROW
EXECUTE FUNCTION log_audit_event();

-- Certificates_uploads table triggers
CREATE TRIGGER certificates_uploads_updated_at_trigger
    BEFORE UPDATE
    ON certificates_uploads
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER certificates_uploads_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE
    ON certificates_uploads
    FOR EACH ROW
EXECUTE FUNCTION log_audit_event();

-- Educations table triggers
CREATE TRIGGER educations_updated_at_trigger
    BEFORE UPDATE
    ON educations
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER educations_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE
    ON educations
    FOR EACH ROW
EXECUTE FUNCTION log_audit_event();

-- Experiences table triggers
CREATE TRIGGER experiences_updated_at_trigger
    BEFORE UPDATE
    ON experiences
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER experiences_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE
    ON experiences
    FOR EACH ROW
EXECUTE FUNCTION log_audit_event();

-- Languages table triggers
CREATE TRIGGER languages_updated_at_trigger
    BEFORE UPDATE
    ON languages
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER languages_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE
    ON languages
    FOR EACH ROW
EXECUTE FUNCTION log_audit_event();

-- Socials table triggers
CREATE TRIGGER socials_updated_at_trigger
    BEFORE UPDATE
    ON socials
    FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER socials_audit_trigger
    AFTER INSERT OR UPDATE OR DELETE
    ON socials
    FOR EACH ROW
EXECUTE FUNCTION log_audit_event();
