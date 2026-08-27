-- ============================================================
-- V6: Inventory schema (oddělení skladu z catalog)
-- ============================================================

-- 1. Schema
CREATE SCHEMA IF NOT EXISTS inventory;

-- 2. Nová tabulka inventory.stock
CREATE TABLE inventory.stock (
    product_id          UUID            PRIMARY KEY,  -- ID produktu z catalog (bez FK kvůli modularitě)
    quantity            INTEGER         NOT NULL DEFAULT 0,
    reserved_quantity   INTEGER         NOT NULL DEFAULT 0,
    updated_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
    version             INTEGER         NOT NULL DEFAULT 0,   -- optimistic lock

    CONSTRAINT chk_stock_quantity  CHECK (quantity >= 0),
    CONSTRAINT chk_stock_reserved  CHECK (reserved_quantity >= 0),
    CONSTRAINT chk_stock_available CHECK (quantity >= reserved_quantity)
);

CREATE INDEX idx_stock_available
    ON inventory.stock (product_id)
    WHERE quantity > reserved_quantity;

-- 3. Migrace dat ze staré catalog.stock (pokud tabulka existuje)
DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_schema = 'catalog'
          AND table_name = 'stock'
    ) THEN
        INSERT INTO inventory.stock (product_id, quantity, reserved_quantity, updated_at, version)
        SELECT product_id, quantity, reserved_quantity, updated_at, version
        FROM catalog.stock
        ON CONFLICT (product_id) DO NOTHING;

        -- 4. Smazání staré tabulky
        DROP TABLE catalog.stock;
    END IF;
END $$;