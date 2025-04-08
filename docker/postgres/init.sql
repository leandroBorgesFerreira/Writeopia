CREATE TABLE document_entity (
  id TEXT PRIMARY KEY,
  title TEXT NOT NULL,
  created_at BIGINT NOT NULL,
  last_synced BIGINT NOT NULL,
  user_id TEXT NOT NULL,
  favorite BOOLEAN NOT NULL,
  parent_document_id TEXT NOT NULL,
  icon TEXT,
  icon_tint INTEGER,
  is_locked BOOLEAN NOT NULL
);

CREATE TABLE story_step_entity (
  id TEXT PRIMARY KEY,
  local_id TEXT NOT NULL,
  type INTEGER NOT NULL,
  parent_id TEXT,
  url TEXT,
  path TEXT,
  text TEXT,
  checked BOOLEAN NOT NULL,
  position INTEGER NOT NULL,
  document_id TEXT NOT NULL,
  is_group BOOLEAN NOT NULL,
  has_inner_steps BOOLEAN NOT NULL,
  background_color INTEGER,
  tags TEXT NOT NULL,
  spans TEXT NOT NULL,
  link_to_document TEXT
);
