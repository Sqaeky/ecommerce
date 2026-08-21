ALTER TABLE event_publication
    ADD COLUMN IF NOT EXISTS id                     UUID,
    ADD COLUMN IF NOT EXISTS listener_id            TEXT,
    ADD COLUMN IF NOT EXISTS event_type             TEXT,
    ADD COLUMN IF NOT EXISTS serialized_event       TEXT,
    ADD COLUMN IF NOT EXISTS publication_date       TIMESTAMP WITH TIME ZONE,
    ADD COLUMN IF NOT EXISTS completion_date        TIMESTAMP WITH TIME ZONE,
    ADD COLUMN IF NOT EXISTS status                 TEXT,
    ADD COLUMN IF NOT EXISTS completion_attempts    INT,
    ADD COLUMN IF NOT EXISTS last_resubmission_date TIMESTAMP WITH TIME ZONE;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint
        WHERE conname = 'event_publication_pkey'
    ) THEN
ALTER TABLE event_publication
    ADD CONSTRAINT event_publication_pkey PRIMARY KEY (id);
END IF;
END $$;

-- Indexy
CREATE INDEX IF NOT EXISTS event_publication_by_completion_date_idx
    ON event_publication (completion_date);

CREATE INDEX IF NOT EXISTS event_publication_serialized_event_hash_idx
    ON event_publication USING hash (serialized_event);

CREATE INDEX IF NOT EXISTS event_publication_by_listener_id_and_serialized_event_idx
    ON event_publication (listener_id, serialized_event);