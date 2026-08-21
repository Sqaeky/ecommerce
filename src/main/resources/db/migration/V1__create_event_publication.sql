CREATE TABLE event_publication (
    id UUID NOT NULL,
    completion_date TIMESTAMP WITH TIME ZONE,
    publication_date TIMESTAMP WITH TIME ZONE NOT NULL,
    listener_id VARCHAR(512) NOT NULL,
    event_type VARCHAR(512) NOT NULL,
    serialized_event TEXT NOT NULL,
    event_payload TEXT NOT NULL,
    CONSTRAINT event_publication_pkey PRIMARY KEY (id)
);

CREATE INDEX event_publication_by_completion_date
    ON event_publication (completion_date);