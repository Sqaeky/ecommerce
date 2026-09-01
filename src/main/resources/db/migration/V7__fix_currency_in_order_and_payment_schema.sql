ALTER TABLE "order".orders
    ALTER COLUMN currency TYPE VARCHAR(3);

ALTER TABLE payment.payments
    ALTER COLUMN currency TYPE VARCHAR(3);
