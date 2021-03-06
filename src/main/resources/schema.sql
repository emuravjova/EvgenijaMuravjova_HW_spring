DROP TABLE IF EXISTS car;
DROP TABLE IF EXISTS seller;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS offer;
DROP TABLE IF EXISTS deal;

CREATE TABLE IF NOT EXISTS car (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  number VARCHAR(20) NOT NULL UNIQUE,
  brand  VARCHAR(20) NOT NULL,
  year INTEGER NOT NULL,
  color VARCHAR(15) NOT NULL
);

CREATE TABLE IF NOT EXISTS seller (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(20) NOT NULL,
  contacts  VARCHAR(70) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS customer (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(20) NOT NULL,
  contacts  VARCHAR(70) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS offer (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  car_id BIGINT NOT NULL,
  seller_id  BIGINT NOT NULL,
  price INTEGER check (price > 0),
  accepted_deal_id BIGINT,
FOREIGN KEY (car_id) REFERENCES car(id) ON DELETE CASCADE,
FOREIGN KEY (seller_id) REFERENCES seller(id) ON DELETE CASCADE,
UNIQUE (car_id, accepted_deal_id)
);

CREATE TABLE IF NOT EXISTS deal (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  customer_id  BIGINT NOT NULL,
  offer_id BIGINT NOT NULL,
  price INTEGER check (price > 0),
  state ENUM('active', 'rejected', 'accepted'),
FOREIGN KEY (customer_id) REFERENCES customer(id),
FOREIGN KEY (offer_id) REFERENCES offer(id) ON DELETE CASCADE,
);