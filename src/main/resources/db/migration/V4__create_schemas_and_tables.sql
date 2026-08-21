-- 1. Schémata
CREATE SCHEMA IF NOT EXISTS catalog;
CREATE SCHEMA IF NOT EXISTS "order";
CREATE SCHEMA IF NOT EXISTS payment;
CREATE SCHEMA IF NOT EXISTS "user";
CREATE SCHEMA IF NOT EXISTS cart;

-- ============================================================
-- 2. USER schema
-- ============================================================

CREATE TABLE "user".users (
                              id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
                              email           VARCHAR(255)    NOT NULL,
                              password_hash   VARCHAR(255)    NOT NULL,
                              first_name      VARCHAR(100),
                              last_name       VARCHAR(100),
                              phone           VARCHAR(30),
                              status          VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE',   -- ACTIVE, BLOCKED
                              created_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                              updated_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                              deleted_at      TIMESTAMPTZ,

                              CONSTRAINT uq_users_email UNIQUE (email),
                              CONSTRAINT chk_users_status CHECK (status IN ('ACTIVE', 'BLOCKED'))
);

CREATE TABLE "user".addresses (
                                  id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
                                  user_id         UUID            NOT NULL REFERENCES "user".users(id) ON DELETE CASCADE,
                                  type            VARCHAR(20)     NOT NULL,   -- SHIPPING, BILLING
                                  street          VARCHAR(255)    NOT NULL,
                                  city            VARCHAR(100)    NOT NULL,
                                  zip_code        VARCHAR(20)     NOT NULL,
                                  country         VARCHAR(2)      NOT NULL,   -- ISO 3166-1 alpha-2
                                  is_default      BOOLEAN         NOT NULL DEFAULT FALSE,
                                  created_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                                  updated_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),

                                  CONSTRAINT chk_addresses_type CHECK (type IN ('SHIPPING', 'BILLING'))
);

CREATE INDEX idx_addresses_user_id ON "user".addresses(user_id);

-- ============================================================
-- 3. CATALOG schema
-- ============================================================

CREATE TABLE catalog.categories (
                                    id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
                                    name            VARCHAR(150)    NOT NULL,
                                    slug            VARCHAR(150)    NOT NULL,
                                    parent_id       UUID            REFERENCES catalog.categories(id) ON DELETE SET NULL,
                                    description     TEXT,
                                    created_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                                    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),

                                    CONSTRAINT uq_categories_slug UNIQUE (slug)
);

CREATE INDEX idx_categories_parent_id ON catalog.categories(parent_id);

CREATE TABLE catalog.products (
                                  id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
                                  name            VARCHAR(255)    NOT NULL,
                                  slug            VARCHAR(255)    NOT NULL,
                                  description     TEXT,
                                  price           NUMERIC(12,2)   NOT NULL,
                                  currency        CHAR(3)         NOT NULL DEFAULT 'CZK',
                                  category_id     UUID            REFERENCES catalog.categories(id) ON DELETE SET NULL,
                                  is_active       BOOLEAN         NOT NULL DEFAULT TRUE,
                                  created_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                                  updated_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                                  deleted_at      TIMESTAMPTZ,

                                  CONSTRAINT uq_products_slug UNIQUE (slug),
                                  CONSTRAINT chk_products_price CHECK (price >= 0)
);

CREATE INDEX idx_products_category_id ON catalog.products(category_id);
CREATE INDEX idx_products_is_active   ON catalog.products(is_active) WHERE deleted_at IS NULL;

CREATE TABLE catalog.stock (
                               product_id          UUID            PRIMARY KEY REFERENCES catalog.products(id) ON DELETE CASCADE,
                               quantity            INTEGER         NOT NULL DEFAULT 0,
                               reserved_quantity   INTEGER         NOT NULL DEFAULT 0,
                               updated_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                               version             INTEGER         NOT NULL DEFAULT 0,   -- optimistic lock

                               CONSTRAINT chk_stock_quantity CHECK (quantity >= 0),
                               CONSTRAINT chk_stock_reserved CHECK (reserved_quantity >= 0),
                               CONSTRAINT chk_stock_available CHECK (quantity >= reserved_quantity)
);

-- product_images – zatím prázdná tabulka (připravená do budoucna)
CREATE TABLE catalog.product_images (
                                        id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
                                        product_id      UUID            NOT NULL REFERENCES catalog.products(id) ON DELETE CASCADE,
                                        url             VARCHAR(500)    NOT NULL,
                                        alt_text        VARCHAR(255),
                                        sort_order      INTEGER         NOT NULL DEFAULT 0,
                                        is_primary      BOOLEAN         NOT NULL DEFAULT FALSE,
                                        created_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_product_images_product_id ON catalog.product_images(product_id);

-- ============================================================
-- 4. CART schema
-- ============================================================

CREATE TABLE cart.carts (
                            id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
                            user_id         UUID            REFERENCES "user".users(id) ON DELETE SET NULL,
                            session_id      VARCHAR(100),                       -- pro anonymní košíky
                            created_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                            updated_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                            expires_at      TIMESTAMPTZ,

                            CONSTRAINT chk_carts_owner CHECK (user_id IS NOT NULL OR session_id IS NOT NULL)
);

CREATE INDEX idx_carts_user_id    ON cart.carts(user_id);
CREATE INDEX idx_carts_session_id ON cart.carts(session_id);

CREATE TABLE cart.cart_items (
                                 id                  UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
                                 cart_id             UUID            NOT NULL REFERENCES cart.carts(id) ON DELETE CASCADE,
                                 product_id          UUID            NOT NULL REFERENCES catalog.products(id) ON DELETE CASCADE,
                                 quantity            INTEGER         NOT NULL,
                                 price_at_addition   NUMERIC(12,2)   NOT NULL,       -- snapshot ceny
                                 created_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                                 updated_at          TIMESTAMPTZ     NOT NULL DEFAULT NOW(),

                                 CONSTRAINT uq_cart_items_cart_product UNIQUE (cart_id, product_id),
                                 CONSTRAINT chk_cart_items_quantity CHECK (quantity > 0)
);

CREATE INDEX idx_cart_items_cart_id    ON cart.cart_items(cart_id);
CREATE INDEX idx_cart_items_product_id ON cart.cart_items(product_id);

-- ============================================================
-- 5. ORDER schema
-- ============================================================

CREATE TABLE "order".orders (
                                id                      UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
                                order_number            VARCHAR(30)     NOT NULL,
                                user_id                 UUID            NOT NULL REFERENCES "user".users(id),
                                status                  VARCHAR(30)     NOT NULL DEFAULT 'CREATED',
                                total_price             NUMERIC(12,2)   NOT NULL,
                                currency                CHAR(3)         NOT NULL DEFAULT 'CZK',
                                shipping_address_id     UUID            REFERENCES "user".addresses(id),
                                billing_address_id      UUID            REFERENCES "user".addresses(id),
                                created_at              TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                                updated_at              TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                                finished_at             TIMESTAMPTZ,
                                version                 INTEGER         NOT NULL DEFAULT 0,   -- optimistic lock

                                CONSTRAINT uq_orders_order_number UNIQUE (order_number),
                                CONSTRAINT chk_orders_total_price CHECK (total_price >= 0)
);

CREATE INDEX idx_orders_user_id ON "order".orders(user_id);
CREATE INDEX idx_orders_status  ON "order".orders(status);

CREATE TABLE "order".order_items (
                                     id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
                                     order_id        UUID            NOT NULL REFERENCES "order".orders(id) ON DELETE CASCADE,
                                     product_id      UUID            NOT NULL REFERENCES catalog.products(id),
                                     product_name    VARCHAR(255)    NOT NULL,           -- snapshot
                                     product_sku     VARCHAR(100),
                                     quantity        INTEGER         NOT NULL,
                                     unit_price      NUMERIC(12,2)   NOT NULL,
                                     total_price     NUMERIC(12,2)   NOT NULL,

                                     CONSTRAINT chk_order_items_quantity CHECK (quantity > 0),
                                     CONSTRAINT chk_order_items_prices   CHECK (unit_price >= 0 AND total_price >= 0)
);

CREATE INDEX idx_order_items_order_id   ON "order".order_items(order_id);
CREATE INDEX idx_order_items_product_id ON "order".order_items(product_id);

CREATE TABLE "order".order_status_history (
                                              id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
                                              order_id        UUID            NOT NULL REFERENCES "order".orders(id) ON DELETE CASCADE,
                                              from_status     VARCHAR(30),
                                              to_status       VARCHAR(30)     NOT NULL,
                                              changed_by      UUID            REFERENCES "user".users(id),   -- může být null (systém)
                                              changed_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                                              note            TEXT
);

CREATE INDEX idx_order_status_history_order_id ON "order".order_status_history(order_id);

-- ============================================================
-- 6. PAYMENT schema
-- ============================================================

CREATE TABLE payment.payments (
                                  id              UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
                                  order_id        UUID            NOT NULL REFERENCES "order".orders(id),
                                  amount          NUMERIC(12,2)   NOT NULL,
                                  currency        CHAR(3)         NOT NULL DEFAULT 'CZK',
                                  status          VARCHAR(30)     NOT NULL DEFAULT 'PENDING',  -- PENDING, COMPLETED, CANCELLED, REFUNDED...
                                  method          VARCHAR(50),                                 -- CARD, BANK_TRANSFER, ...
                                  paid_at         TIMESTAMPTZ,
                                  created_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),
                                  updated_at      TIMESTAMPTZ     NOT NULL DEFAULT NOW(),

                                  CONSTRAINT uq_payments_order_id UNIQUE (order_id),
                                  CONSTRAINT chk_payments_amount CHECK (amount >= 0)
);

CREATE INDEX idx_payments_status ON payment.payments(status);

CREATE TABLE payment.payment_attempts (
                                          id                      UUID            PRIMARY KEY DEFAULT gen_random_uuid(),
                                          payment_id              UUID            NOT NULL REFERENCES payment.payments(id) ON DELETE CASCADE,
                                          order_id                UUID            NOT NULL REFERENCES "order".orders(id),
                                          amount                  NUMERIC(12,2)   NOT NULL,
                                          status                  VARCHAR(30)     NOT NULL,           -- SUCCEEDED, FAILED, PENDING
                                          provider                VARCHAR(50),
                                          provider_payment_id     VARCHAR(255),
                                          error_code              VARCHAR(100),
                                          error_message           TEXT,
                                          created_at              TIMESTAMPTZ     NOT NULL DEFAULT NOW(),

                                          CONSTRAINT chk_payment_attempts_amount CHECK (amount >= 0)
);

CREATE INDEX idx_payment_attempts_payment_id ON payment.payment_attempts(payment_id);
CREATE INDEX idx_payment_attempts_order_id   ON payment.payment_attempts(order_id);