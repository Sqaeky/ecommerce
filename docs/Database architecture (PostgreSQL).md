# Database architecture (PostgreSQL)

## E-Commerce

## Vazby

```text
users 1 - * adresses
users 1 - * orders
users 1 - * carts

categories 1 - * products
products 1 - 1 stock
products 1 - * product_images

carts 1 - * cart_items
cart_items * - 1 products

orders 1 - * order_items
orders 1 - * order_status_history
orders 1 - 1 payment
payments 1 - * payment_attempts
```

## Schemas

### Catalog

#### products

- id (PK)
- name
- slug
- description
- price
- currency
- category_id
- is_active
- created_at
- updated_at
- deleted_at

#### categories

- id
- name
- slug (Unique)
- parent_id (FK -> categories.id - strom)
- description

#### stock

- product_id (PK + FK)
- quantity
- reserved_quantity (košík nebo vytváření objednávky)
- updated_at
- version (Optimistic lock)

#### product_images (do budoucna)

- zatím nic

### Order

#### orders

- id
- order_number
- user_id
- status
- total_price
- currency
- shipping_address_id
- billing_address_id
- created_at
- updated_at
- finished_at
- version (Optimistic lock)

#### order_items

- id
- order_id
- product_id
- product_name
- product_sku
- quantity
- unit_price
- total_price

#### order_status_history

- id
- order_id
- from_status
- to_status
- changed_by
- changed_at
- note

### Payment

#### payments

- id
- order_id
- amount
- currency
- status (Pending, completed, cancelled, refunded,...)
- method
- paid_at

#### payments_attempts

- id
- payment_id
- order_id
- amount
- status (succeeded, failed, Pending)
- provider
- provider_payment_id
- error_code
- error_message
- created_at

### User

#### users

- id (PK)
- email (unique, NotNull)
- password_hash
- first_name
- last_name
- phone
- status (Active, blocked)
- created_at
- updated_at
- deleted_at

#### adresses

- id (PK)
- user_id (FK -> users.id)
- type (Shipping/billing)
- street
- city
- zip_code
- country
- is_default

#### roles (zatím optional)

### Cart

#### carts

- id (PK)
- user_id (nullable)
- session_id (nullable)
- created_at
- updated_at
- expires_at

#### cart_items

- id
- cart_id
- product_id
- quantity
- price_at_addition