/*┌──────────────────────────────────────────────────────────┬──────────────────────────────────────────────────────────┐
│ 1 Electronics                                               │ 2 Clothing & Footwear                                               │
│ ├─7 Mobile Phones & Telecom  ├─8 Computers & Office  ├─9 Digital Accessories  └─10 Smart Devices    │ ├─11 Men''s Apparel    ├─12 Women''s Apparel    ├─13 Men''s Shoes    └─14 Women''s Shoes           │
├──────────────────────────────────────────────────────────┼──────────────────────────────────────────────────────────┤
│ 3 Food & Beverages                                               │ 4 Home & Living                                               │
│ ├─15 Snacks ├─16 Fresh Produce ├─17 Drinks & Beverages └─18 Cooking Oil & Seasonings   │ ├─19 Furniture    ├─20 Home Textiles    ├─21 Kitchenware    └─22 Lighting & Lamps           │
├──────────────────────────────────────────────────────────┼──────────────────────────────────────────────────────────┤
│ 5 Beauty & Skincare                                               │ 6 Sports & Outdoors                                               │
│ ├─23 Skincare      ├─24 Cosmetics      ├─25 Perfume & Fragrance      └─26 Personal Care      │ ├─27 Fitness Equipment ├─28 Outdoor Gear ├─29 Gym Accessories └─30 Sportswear    │
└──────────────────────────────────────────────────────────┴──────────────────────────────────────────────────────────┘
====================== 1. Product Category Table ====================== */
DROP TABLE IF EXISTS pms_product_category;
CREATE TABLE pms_product_category (
 id bigint NOT NULL AUTO_INCREMENT COMMENT 'Category ID',
 parent_id bigint DEFAULT NULL COMMENT 'Parent category ID: 0-Root category',
 name varchar(50) NOT NULL COMMENT 'Category name',
 sort int DEFAULT NULL COMMENT 'Sort order',
 icon varchar(255) DEFAULT NULL COMMENT 'Category icon URL',
 PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Product category table';
INSERT INTO pms_product_category (id, parent_id, name, sort, icon) VALUES
-- Level 1 categories: parent_id = 0
(1, 0, 'Electronics', 1, ''),
(2, 0, 'Clothing & Footwear', 2, ''),
(3, 0, 'Food & Beverages', 3, ''),
(4, 0, 'Home & Living', 4, ''),
(5, 0, 'Beauty & Skincare', 5, ''),
(6, 0, 'Sports & Outdoors', 6, ''),

-- Electronics Subcategories
(7, 1, 'Mobile Phones & Telecom', 1, ''),
(8, 1, 'Computers & Office', 2, ''),
(9, 1, 'Digital Accessories', 3, ''),
(10, 1, 'Smart Devices', 4, ''),

-- Clothing & Footwear Subcategories
(11, 2, 'Men''s Apparel', 1, ''),
(12, 2, 'Women''s Apparel', 2, ''),
(13, 2, 'Men''s Shoes', 3, ''),
(14, 2, 'Women''s Shoes', 4, ''),

-- Food & Beverages Subcategories
(15, 3, 'Snacks', 1, ''),
(16, 3, 'Fresh Produce', 2, ''),
(17, 3, 'Drinks & Beverages', 3, ''),
(18, 3, 'Cooking Oil & Seasonings', 4, '');

-- Home & Living(4) Subcategories
INSERT INTO pms_product_category (id, parent_id, name, sort, icon) VALUES
(19, 4, 'Furniture', 1, ''),
(20, 4, 'Home Textiles', 2, ''),
(21, 4, 'Kitchenware', 3, ''),
(22, 4, 'Lighting & Lamps', 4, '');

-- Beauty & Skincare(5) Subcategories
INSERT INTO pms_product_category (id, parent_id, name, sort, icon) VALUES
 (23, 5, 'Skincare', 1, ''),
 (24, 5, 'Cosmetics', 2, ''),
 (25, 5, 'Perfume & Fragrance', 3, ''),
 (26, 5, 'Personal Care', 4, '');
INSERT INTO pms_product_category (id, parent_id, name, sort, icon) VALUES
  (27, 6, 'Fitness Equipment', 1, ''),
  (28, 6, 'Outdoor Gear', 2, ''),
  (29, 6, 'Gym Accessories', 3, ''),
  (30, 6, 'Sportswear', 4, '');
/* ====================== 2. Product Table (Final Version) ====================== */
DROP TABLE IF EXISTS pms_product;
CREATE TABLE pms_product (
  id bigint NOT NULL AUTO_INCREMENT COMMENT 'Product ID',
  name varchar(200) NOT NULL COMMENT 'Product name',
  category_id bigint NOT NULL COMMENT 'Category ID',
  price decimal(10,2) NOT NULL COMMENT 'Sale price',
  stock int NOT NULL DEFAULT 0 COMMENT 'Stock inventory',
  pic varchar(255) DEFAULT NULL COMMENT 'Main product image',
  sort int DEFAULT NULL COMMENT 'Sort order',
  publish_status int DEFAULT 1 COMMENT 'Publish status: 0-Off-shelf 1-On-shelf',
  promote_weight int DEFAULT NULL COMMENT 'Promote weight priority',
  is_promotion tinyint DEFAULT 1 COMMENT 'Allow promotion: 1=Allowed, 0=Forbidden',
  create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
  PRIMARY KEY (id),
  KEY idx_category_id (category_id),
  KEY idx_publish_status (publish_status),
  KEY idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Product table';

INSERT INTO pms_product (id, name, category_id, price, stock, pic, sort, publish_status, promote_weight, create_time, is_promotion) VALUES
-- Electronics -> Mobile Phones & Telecom (category_id=7)
(1, 'Apple iPhone 16', 7, 7999.00, 50, '', 1, 1, 100, NOW(), 0),
(2, 'Huawei Mate 70', 7, 6999.00, 80, '', 2, 1, 95, NOW(), 0),
(3, 'Xiaomi 14', 7, 3999.00, 100, '', 3, 1, 90, NOW(), 0),

-- Electronics -> Computers & Office (category_id=8)
(4, 'Lenovo Legion', 8, 7999.00, 30, '', 4, 1, 85, NOW(), 1),
(5, 'Apple Mac', 8, 7999.00, 40, '', 5, 1, 80, NOW(), 1),

-- Electronics -> Digital Accessories (category_id=9)
(6, 'Wireless Bluetooth Earbuds', 9, 299.00, 200, '', 6, 1, 75, NOW(), 1),
(7, 'Mechanical Keyboard', 9, 259.00, 150, '', 7, 1, 70, NOW(), 1),
(8, 'Tablet Computer', 9, 3999.00, 60, '', 8, 1, 85, NOW(), 1),

-- Clothing & Footwear -> Men''s Apparel (category_id=11)
(9, 'Pure Cotton Short Sleeve T-Shirt', 11, 59.00, 500, '', 9, 1, 60, NOW(), 1),
(10, 'Straight Leg Denim Jeans', 11, 89.00, 300, '', 10, 1, 55, NOW(), 1),
(11, 'Athletic Sports T-Shirt', 11, 89.00, 400, '', 11, 1, 50, NOW(), 1),
(12, 'Casual Jacket', 11, 159.00, 200, '', 12, 1, 65, NOW(), 1),

-- Clothing & Footwear -> Women''s Apparel (category_id=12)
(13, 'Breathable Mesh Sneakers', 12, 299.00, 150, '', 13, 1, 70, NOW(), 1),

-- Clothing & Footwear -> Men''s Shoes (category_id=13)
(14, 'Commuter Backpack', 13, 119.00, 250, '', 14, 1, 60, NOW(), 1),
(15, 'Classic Baseball Cap', 13, 39.00, 300, '', 15, 1, 45, NOW(), 1),

-- Food & Beverages -> Snacks (category_id=15)
(16, 'Premium Coffee Beans', 15, 68.00, 100, '', 16, 1, 50, NOW(), 1),
(17, 'Pure Whole Milk', 16, 69.90, 200, '', 17, 1, 55, NOW(), 1),
(18, 'Crispy Potato Chips Gift Pack', 15, 39.90, 300, '', 18, 1, 45, NOW(), 1),
(19, 'Natural Mineral Water', 16, 29.90, 500, '', 19, 1, 40, NOW(), 1),
(20, 'Assorted Nuts Gift Box', 15, 99.00, 150, '', 20, 1, 60, NOW(), 1);


-- Home & Living -> Furniture(19)
INSERT INTO pms_product (id, name, category_id, price, stock, pic, sort, publish_status, promote_weight, create_time, is_promotion) VALUES
   (21, 'Solid Wood Sofa', 19, 2999.00, 20, '', 21, 1, 70, NOW(), 1),
   (22, 'Dining Table and Chairs Set', 19, 1599.00, 30, '', 22, 1, 65, NOW(), 0);

-- Home & Living -> Home Textiles(20)
INSERT INTO pms_product (id, name, category_id, price, stock, pic, sort, publish_status, promote_weight, create_time, is_promotion) VALUES
        (23, 'Pure Cotton 4-Piece Bedding Set', 20, 299.00, 100, '', 23, 1, 60, NOW(), 1),
        (24, 'Natural Latex Mattress', 20, 899.00, 50, '', 24, 1, 75, NOW(), 0);

-- Beauty & Skincare -> Skincare(23)
INSERT INTO pms_product (id, name, category_id, price, stock, pic, sort, publish_status, promote_weight, create_time, is_promotion) VALUES
(25, 'Hydrating Skincare Lotion Set', 23, 199.00, 200, '', 25, 1, 80, NOW(), 1),
(26, 'SPF50+ Sunscreen Lotion', 23, 89.00, 300, '', 26, 1, 70, NOW(), 1);

-- Beauty & Skincare -> Cosmetics(24)
INSERT INTO pms_product (id, name, category_id, price, stock, pic, sort, publish_status, promote_weight, create_time, is_promotion) VALUES
  (27, 'Cushion BB Cream', 24, 159.00, 150, '', 27, 1, 75, NOW(), 1),
  (28, 'Lipstick Gift Set', 24, 299.00, 100, '', 28, 1, 85, NOW(), 0);

-- Sports & Outdoors -> Sportswear(30)
INSERT INTO pms_product (id, name, category_id, price, stock, pic, sort, publish_status, promote_weight, create_time, is_promotion) VALUES
 (29, 'Quick-Dry Performance T-Shirt', 30, 79.00, 200, '', 29, 1, 65, NOW(), 1),
 (30, 'Athletic Sports Shorts', 30, 59.00, 250, '', 30, 1, 60, NOW(), 1);

/* ====================== 3. User Table ====================== */
DROP TABLE IF EXISTS ums_user;
CREATE TABLE ums_user (
 id bigint NOT NULL AUTO_INCREMENT COMMENT 'User ID',
 username varchar(64) DEFAULT NULL,
 password varchar(100) DEFAULT NULL,
 nickname varchar(64) DEFAULT NULL,
 phone varchar(16) DEFAULT NULL,
 age int DEFAULT NULL COMMENT 'Age',
 gender tinyint DEFAULT 1 COMMENT '1=Male 0=Female',
 create_time datetime DEFAULT CURRENT_TIMESTAMP,
 PRIMARY KEY (id),
 KEY idx_phone (phone),
 KEY idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User table';

INSERT INTO ums_user (id,username,password,nickname,phone,age,gender) VALUES
 (1001,'user1001','$2a$10$fWsw88GQgxOEdWTxxoL5tuBm9rW/jPDvN6rRRMMSGUNg4Jyl4OQgq','Alex Miller','13800138000',27,1),
 (1002,'user1002','$2a$10$fWsw88GQgxOEdWTxxoL5tuBm9rW/jPDvN6rRRMMSGUNg4Jyl4OQgq','Brian Davis','13900139000',22,1),
 (1003,'user1003','$2a$10$fWsw88GQgxOEdWTxxoL5tuBm9rW/jPDvN6rRRMMSGUNg4Jyl4OQgq','David Wilson','13700137000',31,1),
 (1004,'user1004','$2a$10$fWsw88GQgxOEdWTxxoL5tuBm9rW/jPDvN6rRRMMSGUNg4Jyl4OQgq','Ethan Taylor','13600136000',42,1),
 (1005,'user1005','$2a$10$fWsw88GQgxOEdWTxxoL5tuBm9rW/jPDvN6rRRMMSGUNg4Jyl4OQgq','Grace Johnson','13500135000',24,0),
 (1006,'user1006','$2a$10$fWsw88GQgxOEdWTxxoL5tuBm9rW/jPDvN6rRRMMSGUNg4Jyl4OQgq','Henry Clark','13400134000',29,1),
 (1007,'user1007','$2a$10$fWsw88GQgxOEdWTxxoL5tuBm9rW/jPDvN6rRRMMSGUNg4Jyl4OQgq','Ian Wright','13300133000',20,0),
 (1008,'user1008','$2a$10$fWsw88GQgxOEdWTxxoL5tuBm9rW/jPDvN6rRRMMSGUNg4Jyl4OQgq','Jack Roberts','13200132000',34,1),
 (1009,'user1009','$2a$10$fWsw88GQgxOEdWTxxoL5tuBm9rW/jPDvN6rRRMMSGUNg4Jyl4OQgq','Kevin Adams','13100131000',19,0),
 (1010,'user1010','$2a$10$fWsw88GQgxOEdWTxxoL5tuBm9rW/jPDvN6rRRMMSGUNg4Jyl4OQgq','Leo Scott','13000130000',37,1),
 (1011,'user1011','$2a$10$fWsw88GQgxOEdWTxxoL5tuBm9rW/jPDvN6rRRMMSGUNg4Jyl4OQgq','Mia Nelson','13010130100',25,0),
 (1012,'user1012','$2a$10$fWsw88GQgxOEdWTxxoL5tuBm9rW/jPDvN6rRRMMSGUNg4Jyl4OQgq','Nathan White','13020130200',32,1),
 (1013,'user1013','$2a$10$fWsw88GQgxOEdWTxxoL5tuBm9rW/jPDvN6rRRMMSGUNg4Jyl4OQgq','Oliver Harris','13030130300',41,0),
 (1014,'user1014','$2a$10$fWsw88GQgxOEdWTxxoL5tuBm9rW/jPDvN6rRRMMSGUNg4Jyl4OQgq','Peter Martin','13040130400',28,1),
 (1015,'user1015','$2a$10$fWsw88GQgxOEdWTxxoL5tuBm9rW/jPDvN6rRRMMSGUNg4Jyl4OQgq','Quinn Evans','13050130500',23,0);

/* ====================== 4. Order Table ====================== */
DROP TABLE IF EXISTS oms_order;
CREATE TABLE oms_order (
  id bigint NOT NULL AUTO_INCREMENT,
  order_no varchar(64) DEFAULT NULL COMMENT 'Order number',
  user_id bigint DEFAULT NULL COMMENT 'User ID',
  total_amount decimal(10,2) DEFAULT NULL COMMENT 'Order total amount',
  pay_amount decimal(10,2) DEFAULT NULL COMMENT 'Actual paid amount',
  freight_amount decimal(10,2) DEFAULT NULL COMMENT 'Freight fee amount',
  pay_type tinyint DEFAULT NULL COMMENT 'Payment method: 0-Unpaid 1-WeChat 2-Alipay',
  status tinyint DEFAULT NULL COMMENT 'Order status: 0-Pending payment 1-Pending dispatch 2-Pending delivery 3-Completed 4-Closed',
  receiver_name varchar(100) DEFAULT NULL COMMENT 'Receiver name',
  receiver_phone varchar(16) DEFAULT NULL COMMENT 'Receiver phone',
  receiver_address varchar(255) DEFAULT NULL COMMENT 'Receiver address',
  pay_time datetime DEFAULT NULL COMMENT 'Payment timestamp',
  delivery_time datetime DEFAULT NULL COMMENT 'Delivery timestamp',
  receive_time datetime DEFAULT NULL COMMENT 'User confirmation timestamp',
  finish_time datetime DEFAULT NULL COMMENT 'Order completed timestamp',
  cancel_time datetime DEFAULT NULL COMMENT 'Order cancelled timestamp',
  remark varchar(255) DEFAULT NULL,
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_order_no (order_no),
  KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Order table';

INSERT INTO oms_order (order_no,user_id,total_amount,pay_amount,freight_amount,pay_type,status,receiver_name,receiver_phone,receiver_address,create_time) VALUES
   ('ORDER20260330001',1001,8298.00,8298.00,0.00,1,3,'Alex Miller','13800138000','Haidian District, Beijing','2026-03-30 09:15:00'),
   ('ORDER20260330002',1002,148.00,148.00,0.00,2,3,'Brian Davis','13900139000','Pudong New Area, Shanghai','2026-03-30 13:20:00'),
   ('ORDER20260330003',1003,3999.00,3999.00,0.00,1,3,'David Wilson','13700137000','Tianhe District, Guangzhou','2026-03-30 10:30:00'),
   ('ORDER20260330004',1004,68.00,68.00,0.00,2,3,'Ethan Taylor','13600136000','Nanshan District, Shenzhen','2026-03-30 16:50:00'),
   ('ORDER20260404001',1005,199.00,199.00,0.00,1,1,'Grace Johnson','13500135000','West Lake District, Hangzhou','2026-04-04 10:38:35'),
   ('ORDER20260404002',1006,299.00,299.00,0.00,2,0,'Henry Clark','13400134000','Hi-Tech Zone, Chengdu','2026-04-04 10:38:35'),
   ('ORDER20260404003',1007,156.00,156.00,6.00,1,3,'Ian Wright','13300133000','Yubei District, Chongqing','2026-04-04 10:38:35'),
   ('ORDER20260404004',1008,337.00,337.00,0.00,2,1,'Jack Roberts','13200132000','Hongshan District, Wuhan','2026-04-04 10:38:35'),
   ('ORDER20260404005',1009,89.00,89.00,0.00,1,2,'Kevin Adams','13100131000','Xuanwu District, Nanjing','2026-04-04 10:38:35'),
   ('ORDER20260404006',1010,318.00,318.00,8.00,2,3,'Leo Scott','13000130000','Yanta District, Xi''an','2026-04-04 10:38:35'),
   ('ORDER20260404007',1011,96.00,96.00,0.00,1,0,'Mia Nelson','13010130100','Heping District, Tianjin','2026-04-04 10:38:35'),
   ('ORDER20260404008',1012,518.00,518.00,10.00,2,1,'Nathan White','13020130200','Lixia District, Jinan','2026-04-04 10:38:35'),
   ('ORDER20260404009',1013,238.00,238.00,0.00,1,3,'Oliver Harris','13030130300','Yuelu District, Changsha','2026-04-04 10:38:35'),
   ('ORDER20260404010',1014,799.00,799.00,12.00,2,2,'Peter Martin','13040130400','Jinshui District, Zhengzhou','2026-04-04 10:38:35'),
   ('ORDER20260404011',1015,168.90,168.90,0.00,1,3,'Quinn Evans','13050130500','Heping District, Shenyang','2026-04-04 10:38:35');

DROP TABLE IF EXISTS oms_order_item;
CREATE TABLE oms_order_item (
   id bigint NOT NULL AUTO_INCREMENT,
   order_id bigint NOT NULL COMMENT 'Order ID',
   order_no varchar(64) NOT NULL COMMENT 'Order number',
   product_id bigint NOT NULL COMMENT 'Product ID',
   product_name varchar(255) NOT NULL COMMENT 'Product name',
   product_price decimal(10,2) NOT NULL COMMENT 'Product unit price',
   product_quantity int NOT NULL COMMENT 'Purchase quantity',
   product_total_amount decimal(10,2) NOT NULL COMMENT 'Subtotal amount',
   create_time datetime DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (id),
   KEY idx_order_id (order_id),
   KEY idx_product_id (product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Order Items Details';

INSERT INTO oms_order_item (order_id,order_no,product_id,product_name,product_price,product_quantity,product_total_amount,create_time) VALUES
 (1,'ORDER20260330001',6,'Wireless Bluetooth Earbuds',299.00,3,897.00,'2026-03-30 09:15:00'),
 (1,'ORDER20260330001',3,'Xiaomi 14',3999.00,1,3999.00,'2026-03-30 09:15:00'),
 (3,'ORDER20260330003',3,'Xiaomi 14',3999.00,1,3999.00,'2026-03-30 10:30:00'),
  (15,'ORDER20260404011',19,'Natural Mineral Water',29.90,2,59.80,'2026-04-04 10:38:35');
/* ====================== 5. Order Delivery Table ====================== */
DROP TABLE IF EXISTS oms_order_delivery;
CREATE TABLE oms_order_delivery (
 id bigint NOT NULL AUTO_INCREMENT COMMENT 'Delivery record ID',
 order_id bigint NOT NULL COMMENT 'Order ID',
 order_no varchar(64) DEFAULT NULL COMMENT 'Order Number',
 delivery_company varchar(50) DEFAULT NULL COMMENT 'Carrier Name',
 delivery_company_id int DEFAULT NULL COMMENT 'Carrier ID',
 delivery_user varchar(50) DEFAULT NULL COMMENT 'Sender name' ,
 delivery_user_phone varchar(20) DEFAULT NULL COMMENT 'Sender phone' ,
 delivery_no varchar(50) DEFAULT NULL COMMENT 'Tracking Number',
 delivery_status tinyint DEFAULT 0 COMMENT '0-Pending dispatch 1-Dispatched 2-In transit 3-Signed',
 delivery_time datetime DEFAULT NULL COMMENT 'Dispatch Time',
 sign_time datetime DEFAULT NULL COMMENT 'Delivered timestamp',
 operator varchar(64) DEFAULT NULL COMMENT 'Operator',
 remark varchar(255) DEFAULT NULL COMMENT 'Remark',
 del_flag tinyint DEFAULT 0 COMMENT 'Delete flag',
 sort int DEFAULT 0,
 create_time datetime DEFAULT CURRENT_TIMESTAMP,
 update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 PRIMARY KEY (id),
 KEY idx_order_id (order_id),
 KEY idx_order_no (order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Order delivery table';

/* ====================== 6. Data Statistics Table ====================== */
DROP TABLE IF EXISTS pms_data_stat;
CREATE TABLE pms_data_stat (
id bigint NOT NULL AUTO_INCREMENT,
stat_date date NOT NULL COMMENT 'Statistic date',
sales_amount decimal(12,2) DEFAULT NULL COMMENT 'Total sales revenue',
order_count int DEFAULT NULL COMMENT 'Order count',
user_count int DEFAULT NULL COMMENT 'User count',
product_count int DEFAULT NULL COMMENT 'Product units sold',
create_time datetime DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (id),
UNIQUE KEY uk_stat_date (stat_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Data statistics table';

INSERT INTO pms_data_stat (stat_date,sales_amount,order_count,user_count,product_count) VALUES
 ('2026-03-30',12513.00,4,4,8),
 ('2026-03-31',8920.00,3,3,6),
 ('2026-04-01',5680.00,2,2,4),
 ('2026-04-02',7890.00,3,3,5),
('2026-04-03',9230.00,3,3,6),
 ('2026-04-04',3266.00,11,9,26);

INSERT INTO oms_order_delivery (order_id, order_no, delivery_company, delivery_no, delivery_status, delivery_time, sign_time)
VALUES
    (1,'ORDER20260330001','SF Express','SF202603300001',3,'2026-03-30 09:30:00','2026-03-31 11:20:00'),
    (2,'ORDER20260330002','YTO Express','YTO202603300002',3,'2026-03-30 13:40:00','2026-04-01 09:10:00'),
    (3,'ORDER20260330003','SF Express','SF202603300003',3,'2026-03-30 11:00:00','2026-03-31 16:30:00'),
    (4,'ORDER20260330004','ZTO Express','ZTO202603300004',3,'2026-03-30 17:20:00','2026-04-01 10:00:00'),
    (5,'ORDER20260404001','JD Logistics','JD202604040005',2,'2026-04-04 11:00:00',NULL),
    (6,'ORDER20260404002',NULL,NULL,0,NULL,NULL),
    (7,'ORDER20260404003','STO Express','STO202604040007',2,'2026-04-04 11:00:00',NULL),
    (8,'ORDER20260404004','SF Express','SF202604040008',2,'2026-04-04 11:00:00',NULL),
    (9,'ORDER20260404005','YTO Express','YTO202604040009',2,'2026-04-04 11:00:00',NULL),
    (10,'ORDER20260404006','SF Express','SF202604040010',3,'2026-04-04 11:00:00','2026-04-05 09:40:00'),
    (11,'ORDER20260404007',NULL,NULL,0,NULL,NULL),
    (12,'ORDER20260404008','JD Logistics','JD202604040012',2,'2026-04-04 11:00:00',NULL),
    (13,'ORDER20260404009','ZTO Express','ZTO202604040013',3,'2026-04-04 11:00:00','2026-04-05 14:20:00'),
    (14,'ORDER20260404010','STO Express','STO202604040014',2,'2026-04-04 11:00:00',NULL),
    (15,'ORDER20260404011','SF Express','SF202604040015',3,'2026-04-04 11:00:00','2026-04-05 16:10:00');

-- Order delivery trace table DDL
DROP TABLE IF EXISTS oms_order_delivery_trace;
CREATE TABLE oms_order_delivery_trace (
  id bigint NOT NULL AUTO_INCREMENT COMMENT 'Trace record ID',
  delivery_id bigint NOT NULL COMMENT 'Delivery master ID',
  trace_time datetime NOT NULL COMMENT 'Trace timestamp',
  trace_status int NOT NULL DEFAULT 1 COMMENT 'Trace status: 1-Picked Up, 2-In Transit, 3-Delivered',
  trace_address varchar(255) DEFAULT NULL COMMENT 'Trace checkpoint address',
  sort int DEFAULT 0 COMMENT 'Sort order',
  create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
  PRIMARY KEY (id),
  KEY idx_delivery_id (delivery_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Order delivery trace table';



-- Promotion recharge records table DDL
DROP TABLE IF EXISTS sms_promotion;
CREATE TABLE sms_promotion (
id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary key ID',
admin_id BIGINT NOT NULL COMMENT 'Admin ID (foreign key ums_admin.id)',
promotion_type TINYINT(1) NOT NULL COMMENT 'Promotion type: 1-Basic (100/7d), 2-Standard (200/30d), 3-Premium (500/90d)',
category_id BIGINT DEFAULT NULL COMMENT 'Category ID',
product_id BIGINT DEFAULT NULL COMMENT 'Product ID',
promotion_name VARCHAR(100) NOT NULL COMMENT 'Promotion campaign name',
price DECIMAL(10,2) NOT NULL COMMENT 'Promotion amount',
days INT NOT NULL COMMENT 'Promotion duration in days',
start_time DATETIME NOT NULL COMMENT 'Start time',
end_time DATETIME NOT NULL COMMENT 'End time',
status TINYINT(1) DEFAULT 1 COMMENT 'Status: 1-In progress 2-Ended 3-Cancelled',
pay_amount DECIMAL(10,2) NOT NULL COMMENT 'Actual paid amount',
pay_time DATETIME NOT NULL COMMENT 'Payment timestamp',
quota INT DEFAULT 0 COMMENT 'Promotion quota allowance',
is_category TINYINT(1) DEFAULT 0 COMMENT '1=Category promotion, 0=Product promotion',
create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
INDEX idx_admin_id (admin_id),
INDEX idx_product_id (product_id),
INDEX idx_category_id (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'Product promotion table';

-- Promotion recharge ledger table DDL
DROP TABLE IF EXISTS sms_promotion_recharge;
CREATE TABLE sms_promotion_recharge (
id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Primary key ID',
admin_id BIGINT NOT NULL COMMENT 'Admin ID (foreign key ums_admin.id)',
promotion_type TINYINT(1) NOT NULL COMMENT 'Promotion package type',
package_name VARCHAR(50) NOT NULL COMMENT 'Package name',
amount DECIMAL(10,2) NOT NULL COMMENT 'Recharge amount',
status TINYINT(1) DEFAULT 1 COMMENT '0=Pending payment 1=Paid 2=Cancelled',
quota INT DEFAULT 0 COMMENT 'Promotion quota allowance',
recharge_time DATETIME NOT NULL COMMENT 'Recharge timestamp',
create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
INDEX idx_admin_id (admin_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT 'Promotion recharge history table';



INSERT INTO sms_promotion
(admin_id, promotion_type, category_id, product_id, promotion_name, price, days, start_time, end_time, status, pay_amount, pay_time, is_category)
VALUES
    (1, 1, NULL, 1, 'Product 1 Standard Promotion', 100.00, 7, '2026-04-05 00:00:00', '2026-04-12 00:00:00', 1, 100.00, '2026-04-05 14:00:00', 0),
    (1, 2, 2, NULL, 'Category 2 Featured Promotion', 200.00, 30, '2026-04-05 00:00:00', '2026-05-05 00:00:00', 1, 200.00, '2026-04-05 14:05:00', 1),
    (1, 3, NULL, 3, 'Product 3 Premium Promotion', 500.00, 90, '2026-04-05 00:00:00', '2026-07-04 00:00:00', 1, 500.00, '2026-04-05 14:08:00', 0);

INSERT INTO sms_promotion_recharge
(admin_id, promotion_type, package_name, amount, status, recharge_time)
VALUES
    (1, 3, 'Premium Promotion Package', 500.00, 1, '2026-04-05 14:08:48'),
    (1, 1, 'Standard Promotion Package', 100.00, 1, '2026-04-05 14:08:45'),
    (1, 2, 'Featured Promotion Package', 200.00, 1, '2024-03-20 10:30:00'),
    (1, 1, 'Standard Promotion Package', 100.00, 1, '2024-03-15 14:20:00');


INSERT INTO oms_order_delivery_trace (delivery_id, trace_time, trace_status, trace_address, sort)
VALUES
-- Order 1 (delivery_id=1)
(1,'2026-03-30 09:30:00',1,'Beijing Haidian Branch Station',1),
(1,'2026-03-30 18:20:00',2,'Beijing Transit Distribution Center',2),
(1,'2026-03-31 10:00:00',3,'Haidian District, Beijing',3),
-- Order 2 (delivery_id=2)
(2,'2026-03-30 13:30:00',1,'Shanghai Pudong Branch Station',1),
(2,'2026-03-31 08:10:00',2,'Shanghai Sorting Hub',2),
(2,'2026-03-31 14:00:00',3,'Pudong New Area, Shanghai',3),
-- Order 3 (delivery_id=3)
(3,'2026-03-30 10:30:00',1,'Guangzhou Tianhe Branch Station',1),
(3,'2026-03-30 22:40:00',2,'Guangzhou Transit Distribution Center',2),
(3,'2026-03-31 11:00:00',3,'Tianhe District, Guangzhou',3),
-- Order 4 (delivery_id=4)
(4,'2026-03-30 17:00:00',1,'Shenzhen Nanshan Branch Station',1),
(4,'2026-03-31 09:30:00',2,'Shenzhen Sorting Hub',2),
(4,'2026-03-31 15:00:00',3,'Nanshan District, Shenzhen',3),
-- Order 5 (delivery_id=5)
(5,'2026-04-04 10:40:00',1,'Hangzhou West Lake Branch Station',1),
(5,'2026-04-04 15:40:00',2,'Hangzhou Sorting Hub',2),
-- Order 7 (delivery_id=7)
(7,'2026-04-04 10:40:00',1,'Chongqing Yubei Branch Station',1),
(7,'2026-04-04 16:20:00',2,'Chongqing Sorting Hub',2),
(7,'2026-04-05 10:00:00',3,'Yubei District, Chongqing',3),
-- Order 8 (delivery_id=8)
(8,'2026-04-04 10:40:00',1,'Wuhan Hongshan Branch Station',1),
(8,'2026-04-04 14:30:00',2,'Wuhan Transit Distribution Center',2),
-- Order 9 (delivery_id=9)
(9,'2026-04-04 10:40:00',1,'Nanjing Xuanwu Branch Station',1),
(9,'2026-04-04 17:10:00',2,'Nanjing Sorting Hub',2),
-- Order 10 (delivery_id=10)
(10,'2026-04-04 10:40:00',1,'Xi''an Yanta Branch Station',1),
(10,'2026-04-04 19:40:00',2,'Xi''an Transit Distribution Center',2),
(10,'2026-04-05 11:00:00',3,'Yanta District, Xi''an',3),
-- Order 12 (delivery_id=12)
(12,'2026-04-04 10:40:00',1,'Jinan Lixia Branch Station',1),
(12,'2026-04-04 15:10:00',2,'Jinan Sorting Hub',2),
-- Order 13 (delivery_id=13)
(13,'2026-04-04 10:40:00',1,'Changsha Yuelu Branch Station',1),
(13,'2026-04-04 18:30:00',2,'Changsha Sorting Hub',2),
(13,'2026-04-05 12:00:00',3,'Yuelu District, Changsha',3),
-- Order 14 (delivery_id=14)
(14,'2026-04-04 10:40:00',1,'Zhengzhou Jinshui Branch Station',1),
(14,'2026-04-04 16:40:00',2,'Zhengzhou Sorting Hub',2),
-- Order 15 (delivery_id=15)
(15,'2026-04-04 10:40:00',1,'Shenyang Heping Branch Station',1),
(15,'2026-04-04 17:50:00',2,'Shenyang Transit Distribution Center',2),
(15,'2026-04-05 13:00:00',3,'Heping District, Shenyang',3);



-- Order 1 (Delivered, SF Express)
UPDATE oms_order_delivery
SET delivery_user = 'David Wilson', delivery_user_phone = '400-606-5500'
WHERE order_id = 1;

-- Order 2 (Delivered, YTO Express)
UPDATE oms_order_delivery
SET delivery_user = 'Brian Davis', delivery_user_phone = '400-800-8888'
WHERE order_id = 2;

-- Order 3 (Delivered, SF Express)
UPDATE oms_order_delivery
SET delivery_user = 'David Wilson', delivery_user_phone = '400-606-5500'
WHERE order_id = 3;

-- Order 4 (Delivered, ZTO Express)
UPDATE oms_order_delivery
SET delivery_user = 'Brian Davis', delivery_user_phone = '400-800-8888'
WHERE order_id = 4;

-- Order 5 (In Transit, JD Logistics)
UPDATE oms_order_delivery
SET delivery_user = 'Ethan Taylor', delivery_user_phone = '95152'
WHERE order_id = 5;

-- Order 6 (Pending Dispatch, no sender, keep NULL),
-- No execution required

-- Order 7 (In Transit, STO Express)
UPDATE oms_order_delivery
SET delivery_user = 'Grace Johnson', delivery_user_phone = '95554'
WHERE order_id = 7;

-- Order 8 (In Transit, SF Express)
UPDATE oms_order_delivery
SET delivery_user = 'Ethan Taylor', delivery_user_phone = '95152'
WHERE order_id = 8;

-- Order 9 (In Transit, YTO Express)
UPDATE oms_order_delivery
SET delivery_user = 'Grace Johnson', delivery_user_phone = '95554'
WHERE order_id = 9;

-- Order 10 (Delivered, SF Express)
UPDATE oms_order_delivery
SET delivery_user = 'Ethan Taylor', delivery_user_phone = '95152'
WHERE order_id = 10;

-- Order 11 (Pending Dispatch, no sender, keep NULL),
-- No execution required

-- Order 12 (In Transit, JD Logistics)
UPDATE oms_order_delivery
SET delivery_user = 'David Wilson', delivery_user_phone = '400-606-5500'
WHERE order_id = 12;

-- Order 13 (Delivered, ZTO Express)
UPDATE oms_order_delivery
SET delivery_user = 'Brian Davis', delivery_user_phone = '400-800-8888'
WHERE order_id = 13;

-- Order 14 (In Transit, STO Express)
UPDATE oms_order_delivery
SET delivery_user = 'Grace Johnson', delivery_user_phone = '95554'
WHERE order_id = 14;

-- Order 15 (Delivered, SF Express)
UPDATE oms_order_delivery
SET delivery_user = 'Ethan Taylor', delivery_user_phone = '95152'
WHERE order_id = 15;



-- ====================== UMS RBAC & Admin System Tables ======================
DROP TABLE IF EXISTS ums_role;
CREATE TABLE ums_role (
  id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'Role ID',
  name VARCHAR(64) NOT NULL COMMENT 'Role name',
  description VARCHAR(255) COMMENT 'Role description',
  admin_count INT DEFAULT 0 COMMENT 'Associated admin count',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
  update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update timestamp',
  status TINYINT DEFAULT 1 COMMENT 'Status: 0-Disabled 1-Enabled'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User Roles Table';

INSERT INTO ums_role (id, name, description, admin_count, status) VALUES
(1, 'Super Administrator', 'Full system-wide administrative access', 1, 1),
(2, 'Marketing Specialist', 'Manages promotional campaigns and marketing activities', 0, 1),
(3, 'Operations Specialist', 'Manages promotions, logistics dispatches, and campaign operations', 0, 1),
(4, 'Customer Support', 'Manages AI support inquiries and user consultations', 0, 1),
(5, 'Catalog Product Manager', 'Manages catalog inventory and data analytics dashboards', 0, 1);

DROP TABLE IF EXISTS ums_menu;
CREATE TABLE ums_menu (
  id bigint NOT NULL AUTO_INCREMENT,
  parent_id bigint DEFAULT 0,
  title varchar(100) NOT NULL,
  path varchar(255) DEFAULT NULL,
  icon varchar(100) DEFAULT NULL,
  sort int DEFAULT 0,
  type int DEFAULT 0,
  permission varchar(100) DEFAULT NULL,
  status int DEFAULT 1,
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Menu permissions table';

INSERT INTO ums_menu (id, parent_id, title, path, icon, sort, type, permission, status) VALUES
(1, 0, 'Dashboard', '/dashboard', 'odometer', 1, 1, 'admin:dashboard:view', 1),
(2, 0, 'Products', '/product', 'goods', 2, 1, 'admin:product:view', 1),
(3, 0, 'SKU Management', '/sku', 'files', 3, 1, 'admin:sku:view', 1),
(4, 0, 'Orders', '/order', 'list', 4, 1, 'admin:order:view', 1),
(5, 0, 'Logistics', '/delivery', 'van', 5, 1, 'admin:delivery:view', 1),
(6, 0, 'Promotions', '/promotion', 'present', 6, 1, 'admin:promotion:view', 1),
(7, 0, 'AI Support', '/ai', 'chat-dot-round', 7, 1, 'admin:ai:view', 1),
(8, 0, 'System Users', '/user', 'user', 8, 1, 'admin:user:view', 1);

DROP TABLE IF EXISTS ums_role_menu;
CREATE TABLE ums_role_menu (
  id bigint NOT NULL AUTO_INCREMENT,
  role_id bigint NOT NULL,
  menu_id bigint NOT NULL,
  PRIMARY KEY (id),
  KEY idx_role_id (role_id),
  KEY idx_menu_id (menu_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Role menu permissions';

INSERT INTO ums_role_menu (role_id, menu_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8),
(2, 6),
(3, 2), (3, 5), (3, 6),
(4, 7), (4, 8),
(5, 1), (5, 3);

DROP TABLE IF EXISTS ums_admin;
CREATE TABLE ums_admin (
  id bigint NOT NULL AUTO_INCREMENT,
  username varchar(64) NOT NULL,
  password varchar(100) NOT NULL,
  nick_name varchar(64) DEFAULT NULL,
  avatar varchar(500) DEFAULT NULL,
  phone varchar(32) DEFAULT NULL,
  status int DEFAULT 1,
  role_id bigint DEFAULT 1,
  price decimal(10,2) DEFAULT 0.00,
  promotion_quota bigint DEFAULT 1000,
  used_promotion_quota bigint DEFAULT 0,
  create_time datetime DEFAULT CURRENT_TIMESTAMP,
  update_time datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Admin user table';

INSERT INTO ums_admin (id, username, password, nick_name, avatar, phone, status, role_id, price, promotion_quota, used_promotion_quota)
VALUES (1, 'admin', '$2a$10$fWsw88GQgxOEdWTxxoL5tuBm9rW/jPDvN6rRRMMSGUNg4Jyl4OQgq', 'System Administrator', '', '13800000000', 1, 1, 10000.00, 1000, 0);

DROP TABLE IF EXISTS ums_promotion_package;
CREATE TABLE ums_promotion_package (
  id bigint NOT NULL AUTO_INCREMENT,
  name varchar(50) NOT NULL,
  amount decimal(10,2) NOT NULL,
  total_promotion_amount bigint DEFAULT 0,
  promotion_quota bigint DEFAULT 0,
  weight int DEFAULT 0,
  remark varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS pms_sku_price_history;
CREATE TABLE pms_sku_price_history (
  id bigint NOT NULL AUTO_INCREMENT,
  sku_id bigint NOT NULL,
  sku_code varchar(64) DEFAULT NULL,
  old_price decimal(10,2) DEFAULT NULL,
  new_price decimal(10,2) DEFAULT NULL,
  old_cost decimal(10,2) DEFAULT NULL,
  new_cost decimal(10,2) DEFAULT NULL,
  operator varchar(64) DEFAULT NULL,
  update_time datetime DEFAULT CURRENT_TIMESTAMP,
  remark varchar(255) DEFAULT NULL,
  change_type tinyint NOT NULL DEFAULT 1,
  PRIMARY KEY (id),
  KEY idx_sku_id (sku_id),
  KEY idx_update_time (update_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Product SKU Table
DROP TABLE IF EXISTS `pms_product_sku`;
CREATE TABLE `pms_product_sku` (
 `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'SKU primary ID',
 `product_id` bigint NOT NULL COMMENT 'Associated product ID',
 `sku_code` varchar(64) NOT NULL COMMENT 'Unique SKU code',
 `model` varchar(100) DEFAULT NULL COMMENT 'Product model',
 `spec` varchar(100) DEFAULT NULL COMMENT 'Product spec/color',
 `color` varchar(50) DEFAULT NULL COMMENT 'Product color',
 `size` varchar(50) DEFAULT NULL COMMENT 'Size specification (e.g. 16-inch, S/M/L)',
 `price` decimal(10,2) NOT NULL COMMENT 'Sale price',
 `cost` decimal(10,2) DEFAULT 0.00 COMMENT 'Product cost price',
 `weight` decimal(10,2) DEFAULT 0.00 COMMENT 'Product weight in kg',
 `stock` int NOT NULL DEFAULT 0 COMMENT 'Current stock inventory',
 `pic` varchar(500) DEFAULT NULL COMMENT 'SKU image URL',
 `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
 `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update timestamp',
 PRIMARY KEY (`id`),
 KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Product SKU Table';




INSERT INTO `pms_product_sku`
(`product_id`, `sku_code`, `model`, `spec`, `price`, `stock`, `pic`)
VALUES
-- ======================
-- Product 1: Apple iPhone 17（Total inventory 50）
-- ======================
(1, 'SKU001', 'Apple iPhone 17', 'Black', 7999.00, 20, ''),
(1, 'SKU002', 'Apple iPhone 17', 'White', 7999.00, 15, ''),
(1, 'SKU003', 'Apple iPhone 17', 'Blue', 7999.00, 8, ''),
(1, 'SKU004', 'Apple iPhone 17', 'Red', 7999.00, 5, ''),
(1, 'SKU005', 'Apple iPhone 17', 'Purple', 7999.00, 2, ''),
-- 20+15+8+5+2 = 50 ✅

-- ======================
-- Product 2: Huawei Mate 70（Total inventory 80）
-- ======================
    (2, 'SKU006', 'Huawei Mate 70', 'Black', 6999.00, 30, ''),
(2, 'SKU007', 'Huawei Mate 70', 'White', 6999.00, 25, ''),
(2, 'SKU008', 'Huawei Mate 70', 'Green', 6999.00, 12, ''),
(2, 'SKU009', 'Huawei Mate 70', 'Orange', 6999.00, 8, ''),
(2, 'SKU010', 'Huawei Mate 70', 'Gold', 6999.00, 5, ''), -- 30+25+12+8+5 = 80 ✅

-- ======================
-- Product 4: Lenovo Legion（Total inventory 30）
-- ======================
(4, 'SKU011', 'Lenovo Legion', 'Black', 7999.00, 12, ''),
(4, 'SKU012', 'Lenovo Legion', 'White', 7999.00, 8, ''),
(4, 'SKU013', 'Lenovo Legion', 'Grey', 7999.00, 5, ''),
(4, 'SKU014', 'Lenovo Legion', 'Blue', 7999.00, 3, ''),
(4, 'SKU015', 'Lenovo Legion', 'Red', 7999.00, 2, ''), -- 12+8+5+3+2 = 30 ✅

-- ======================
-- Product 5: Apple Mac（Total inventory 40）
-- ======================
(5, 'SKU016', 'Apple Mac', 'Silver', 7999.00, 15, ''),
(5, 'SKU017', 'Apple Mac', 'Space Grey', 7999.00, 12, ''),
(5, 'SKU018', 'Apple Mac', 'Gold', 7999.00, 7, ''),
(5, 'SKU019', 'Apple Mac', 'Pink', 7999.00, 4, ''),
(5, 'SKU020', 'Apple Mac', 'Blue', 7999.00, 2, ''), -- 15+12+7+4+2 = 40 ✅

-- ======================
-- Product 6: Wireless Bluetooth Earbuds（Total inventory 200）
-- ======================
(6, 'SKU021', 'Wireless Bluetooth Earbuds', 'White', 299.00, 80, ''),
(6, 'SKU022', 'Wireless Bluetooth Earbuds', 'Black', 299.00, 60, ''),
(6, 'SKU023', 'Wireless Bluetooth Earbuds', 'Blue', 299.00, 30, ''),
(6, 'SKU024', 'Wireless Bluetooth Earbuds', 'Green', 299.00, 20, ''),
(6, 'SKU025', 'Wireless Bluetooth Earbuds', 'Pink', 299.00, 10, ''), -- 80+60+30+20+10 = 200 ✅

-- ======================
-- Product 7: Mechanical Keyboard（Total inventory 150）
-- ======================
(7, 'SKU026', 'Mechanical Keyboard', 'Black', 259.00, 60, ''),
(7, 'SKU027', 'Mechanical Keyboard', 'White', 259.00, 40, ''),
(7, 'SKU028', 'Mechanical Keyboard', 'Blue', 259.00, 25, ''),
(7, 'SKU029', 'Mechanical Keyboard', 'Red', 259.00, 15, ''),
(7, 'SKU030', 'Mechanical Keyboard', 'Silver', 259.00, 10, ''), -- 60+40+25+15+10 = 150 ✅

-- ======================
-- Product 8: Tablet Computer（Total inventory 60）
-- ======================
(8, 'SKU031', 'Tablet Computer', 'Silver', 3999.00, 25, ''),
(8, 'SKU032', 'Tablet Computer', 'Grey', 3999.00, 18, ''),
(8, 'SKU033', 'Tablet Computer', 'Pink', 3999.00, 10, ''),
(8, 'SKU034', 'Tablet Computer', 'Blue', 3999.00, 5, ''),
(8, 'SKU035', 'Tablet Computer', 'Green', 3999.00, 2, ''), -- 25+18+10+5+2 = 60 ✅

-- ======================
-- Product 9: Pure Cotton Short Sleeve T-Shirt（Total inventory 500）
-- ======================
(9, 'SKU036', 'Pure Cotton Short Sleeve T-Shirt', 'White', 59.00, 200, ''),
(9, 'SKU037', 'Pure Cotton Short Sleeve T-Shirt', 'Black', 59.00, 150, ''),
(9, 'SKU038', 'Pure Cotton Short Sleeve T-Shirt', 'Grey', 59.00, 70, ''),
(9, 'SKU039', 'Pure Cotton Short Sleeve T-Shirt', 'Blue', 59.00, 50, ''),
(9, 'SKU040', 'Pure Cotton Short Sleeve T-Shirt', 'Red', 59.00, 30, ''), -- 200+150+70+50+30 = 500 ✅

-- ======================
-- Product 10: Straight Leg Denim Jeans（Total inventory 300）
-- ======================
(10, 'SKU041', 'Straight Leg Denim Jeans', 'Blue', 89.00, 120, ''),
(10, 'SKU042', 'Straight Leg Denim Jeans', 'Black', 89.00, 90, ''),
(10, 'SKU043', 'Straight Leg Denim Jeans', 'Grey', 89.00, 50, ''),
(10, 'SKU044', 'Straight Leg Denim Jeans', 'White', 89.00, 25, ''),
(10, 'SKU045', 'Straight Leg Denim Jeans', 'Dark Blue', 89.00, 15, ''), -- 120+90+50+25+15 = 300 ✅

-- ======================
-- Product 11: Athletic Sports T-Shirt（Total inventory 400）
-- ======================
(11, 'SKU046', 'Athletic Sports T-Shirt', 'White', 89.00, 160, ''),
(11, 'SKU047', 'Athletic Sports T-Shirt', 'Black', 89.00, 120, ''),
(11, 'SKU048', 'Athletic Sports T-Shirt', 'Grey', 89.00, 60, ''),
(11, 'SKU049', 'Athletic Sports T-Shirt', 'Blue', 89.00, 40, ''),
(11, 'SKU050', 'Athletic Sports T-Shirt', 'Red', 89.00, 20, ''), -- 160+120+60+40+20 = 400 ✅

-- ======================
-- Product 12: Casual Jacket（Total inventory 200）
-- ======================
(12, 'SKU051', 'Casual Jacket', 'Black', 159.00, 80, ''),
(12, 'SKU052', 'Casual Jacket', 'Khaki', 159.00, 60, ''),
(12, 'SKU053', 'Casual Jacket', 'Grey', 159.00, 30, ''),
(12, 'SKU054', 'Casual Jacket', 'Blue', 159.00, 20, ''),
(12, 'SKU055', 'Casual Jacket', 'Green', 159.00, 10, ''), -- 80+60+30+20+10 = 200 ✅

-- ======================
-- Product 13: Breathable Mesh Sneakers（Total inventory 150）
-- ======================
(13, 'SKU056', 'Breathable Mesh Sneakers', 'Black & White', 299.00, 60, ''),
(13, 'SKU057', 'Breathable Mesh Sneakers', 'All Black', 299.00, 45, ''),
(13, 'SKU058', 'Breathable Mesh Sneakers', 'White & Blue', 299.00, 25, ''),
(13, 'SKU059', 'Breathable Mesh Sneakers', 'White & Red', 299.00, 15, ''),
(13, 'SKU060', 'Breathable Mesh Sneakers', 'Grey & Black', 299.00, 5, ''), -- 60+45+25+15+5 = 150 ✅

-- ======================
-- Product 14: Commuter Backpack（Total inventory 250）
-- ======================
(14, 'SKU061', 'Commuter Backpack', 'Black', 119.00, 100, ''),
(14, 'SKU062', 'Commuter Backpack', 'Grey', 119.00, 70, ''),
(14, 'SKU063', 'Commuter Backpack', 'Blue', 119.00, 40, ''),
(14, 'SKU064', 'Commuter Backpack', 'Green', 119.00, 25, ''),
(14, 'SKU065', 'Commuter Backpack', 'Brown', 119.00, 15, ''), -- 100+70+40+25+15 = 250 ✅

-- ======================
-- Product15: Classic Baseball Cap（Total inventory 300）
-- ======================
(15, 'SKU066', 'Classic Baseball Cap', 'Black', 39.00, 120, ''),
(15, 'SKU067', 'Classic Baseball Cap', 'White', 39.00, 90, ''),
(15, 'SKU068', 'Classic Baseball Cap', 'Khaki', 39.00, 50, ''),
(15, 'SKU069', 'Classic Baseball Cap', 'Blue', 39.00, 25, ''),
(15, 'SKU070', 'Classic Baseball Cap', 'Red', 39.00, 15, ''), -- 120+90+50+25+15 = 300 ✅

-- ======================
-- Product16: Premium Coffee Beans（Total inventory 100）
-- ======================
(16, 'SKU071', 'Premium Coffee Beans', 'Medium Roast', 68.00, 40, ''),
(16, 'SKU072', 'Premium Coffee Beans', 'Dark Roast', 68.00, 30, ''),
(16, 'SKU073', 'Premium Coffee Beans', 'Light Roast', 68.00, 15, ''),
(16, 'SKU074', 'Premium Coffee Beans', 'Italian Blend', 68.00, 10, ''),
(16, 'SKU075', 'Premium Coffee Beans', 'American Blend', 68.00, 5, ''), -- 40+30+15+10+5 = 100 ✅

-- ======================
-- Product17: Pure Whole Milk（Total inventory 200）
-- ======================
(17, 'SKU076', 'Pure Whole Milk', '200ml x 12 packs', 69.90, 80, ''),
(17, 'SKU077', 'Pure Whole Milk', '250ml x 16 packs', 69.90, 60, ''),
(17, 'SKU078', 'Pure Whole Milk', '1L x 6 packs', 69.90, 30, ''),
(17, 'SKU079', 'Pure Whole Milk', '200ml x 24 packs', 69.90, 20, ''),
(17, 'SKU080', 'Pure Whole Milk', '250ml x 24 packs', 69.90, 10, ''), -- 80+60+30+20+10 = 200 ✅

-- ======================
-- Product18: Crispy Potato Chips Gift Pack（Total inventory 300）
-- ======================
(18, 'SKU081', 'Crispy Potato Chips Gift Pack', 'Original Flavor Combo', 39.90, 120, ''),
(18, 'SKU082', 'Crispy Potato Chips Gift Pack', 'Spicy Flavor Combo', 39.90, 90, ''),
(18, 'SKU083', 'Crispy Potato Chips Gift Pack', 'BBQ Flavor Combo', 39.90, 50, ''),
(18, 'SKU084', 'Crispy Potato Chips Gift Pack', 'Tomato Flavor Combo', 39.90, 25, ''),
(18, 'SKU085', 'Crispy Potato Chips Gift Pack', 'Assorted Flavors Combo', 39.90, 15, ''), -- 120+90+50+25+15 = 300 ✅

-- ======================
-- Product19: Natural Mineral Water（Total inventory 500）
-- ======================
(19, 'SKU086', 'Natural Mineral Water', '550ml x 12 bottles', 29.90, 200, ''),
(19, 'SKU087', 'Natural Mineral Water', '550ml x 24 bottles', 29.90, 150, ''),
(19, 'SKU088', 'Natural Mineral Water', '1.5L x 6 bottles', 29.90, 70, ''),
(19, 'SKU089', 'Natural Mineral Water', '1.5L x 12 bottles', 29.90, 50, ''),
(19, 'SKU090', 'Natural Mineral Water', '4.5L x 4 jugs', 29.90, 30, ''), -- 200+150+70+50+30 = 500 ✅

-- ======================
-- Product20: Assorted Nuts Gift Box（Total inventory 150）
-- ======================
(20, 'SKU091', 'Assorted Nuts Gift Box', 'Classic Edition', 99.00, 60, ''),
(20, 'SKU092', 'Assorted Nuts Gift Box', 'Premium Edition', 99.00, 45, ''),
(20, 'SKU093', 'Assorted Nuts Gift Box', 'Daily Nut Pack', 99.00, 25, ''),
(20, 'SKU094', 'Assorted Nuts Gift Box', 'Holiday Gift Edition', 99.00, 15, ''),
(20, 'SKU095', 'Assorted Nuts Gift Box', 'Kids Edition', 99.00, 5, ''), -- 60+45+25+15+5 = 150 ✅

-- ======================
-- Product21: Solid Wood Sofa（Total inventory 20）
-- ======================
(21, 'SKU096', 'Solid Wood Sofa', '3-Seater', 2999.00, 8, ''),
(21, 'SKU097', 'Solid Wood Sofa', '2-Seater', 2999.00, 6, ''),
(21, 'SKU098', 'Solid Wood Sofa', '1-Seater', 2999.00, 3, ''),
(21, 'SKU099', 'Solid Wood Sofa', 'L-Shape Sectional', 2999.00, 2, ''),
(21, 'SKU100', 'Solid Wood Sofa', 'Chaise Lounge Sectional', 2999.00, 1, ''), -- 8+6+3+2+1 = 20 ✅

-- ======================
-- Product22: Dining Table and Chairs Set（Total inventory 30）
-- ======================
(22, 'SKU101', 'Dining Table and Chairs Set', '4-Chair Set', 1599.00, 12, ''),
(22, 'SKU102', 'Dining Table and Chairs Set', '6-Chair Set', 1599.00, 8, ''),
(22, 'SKU103', 'Dining Table and Chairs Set', '8-Chair Set', 1599.00, 5, ''),
(22, 'SKU104', 'Dining Table and Chairs Set', 'Extendable Table Set', 1599.00, 3, ''),
(22, 'SKU105', 'Dining Table and Chairs Set', 'Sintered Stone Set', 1599.00, 2, ''), -- 12+8+5+3+2 = 30 ✅

-- ======================
-- Product23: Pure Cotton 4-Piece Bedding Set（Total inventory 100）
-- ======================
(23, 'SKU106', 'Pure Cotton 4-Piece Bedding Set', '1.5m Bed (Double)', 299.00, 40, ''),
(23, 'SKU107', 'Pure Cotton 4-Piece Bedding Set', '1.8m Bed (Queen)', 299.00, 30, ''),
(23, 'SKU108', 'Pure Cotton 4-Piece Bedding Set', '2.0m Bed (King)', 299.00, 15, ''),
(23, 'SKU109', 'Pure Cotton 4-Piece Bedding Set', 'Minimalist Design', 299.00, 10, ''),
(23, 'SKU110', 'Pure Cotton 4-Piece Bedding Set', 'Printed Floral Pattern', 299.00, 5, ''), -- 40+30+15+10+5 = 100 ✅

-- ======================
-- Product24: Natural Latex Mattress（Total inventory 50）
-- ======================
(24, 'SKU111', 'Natural Latex Mattress', '1.5m x 2.0m', 899.00, 20, ''),
(24, 'SKU112', 'Natural Latex Mattress', '1.8m x 2.0m', 899.00, 15, ''),
(24, 'SKU113', 'Natural Latex Mattress', '2.0m x 2.2m', 899.00, 8, ''),
(24, 'SKU114', 'Natural Latex Mattress', '5cm Thickness', 899.00, 5, ''),
(24, 'SKU115', 'Natural Latex Mattress', '10cm Thickness', 899.00, 2, ''), -- 20+15+8+5+2 = 50 ✅

-- ======================
-- Product25: Hydrating Skincare Lotion Set（Total inventory 200）
-- ======================
(25, 'SKU116', 'Hydrating Skincare Lotion Set', 'Refreshing Type', 199.00, 80, ''),
(25, 'SKU117', 'Hydrating Skincare Lotion Set', 'Moisturizing Type', 199.00, 60, ''),
(25, 'SKU118', 'Hydrating Skincare Lotion Set', 'Sensitive Skin Formula', 199.00, 30, ''),
(25, 'SKU119', 'Hydrating Skincare Lotion Set', 'Brightening Formula', 199.00, 20, ''),
(25, 'SKU120', 'Hydrating Skincare Lotion Set', 'Anti-Aging Formula', 199.00, 10, ''), -- 80+60+30+20+10 = 200 ✅

-- ======================
-- Product26: SPF50+ Sunscreen Lotion（Total inventory 300）
-- ======================
(26, 'SKU121', 'SPF50+ Sunscreen Lotion', 'SPF50+ Refreshing Gel', 89.00, 120, ''),
(26, 'SKU122', 'SPF50+ Sunscreen Lotion', 'SPF50+ Hydrating Cream', 89.00, 90, ''),
(26, 'SKU123', 'SPF50+ Sunscreen Lotion', 'Kids Edition', 89.00, 50, ''),
(26, 'SKU124', 'SPF50+ Sunscreen Lotion', 'Tone-Up Primer Formula', 89.00, 25, ''),
(26, 'SKU125', 'SPF50+ Sunscreen Lotion', 'Waterproof Formula', 89.00, 15, ''), -- 120+90+50+25+15 = 300 ✅

-- ======================
-- Product27: Cushion BB Cream（Total inventory 150）
-- ======================
(27, 'SKU126', 'Cushion BB Cream', 'Natural Beige', 159.00, 60, ''),
(27, 'SKU127', 'Cushion BB Cream', 'Ivory White', 159.00, 45, ''),
(27, 'SKU128', 'Cushion BB Cream', 'Dewy Finish', 159.00, 25, ''),
(27, 'SKU129', 'Cushion BB Cream', 'Full Coverage', 159.00, 15, ''),
(27, 'SKU130', 'Cushion BB Cream', 'Refill Pack', 159.00, 5, ''), -- 60+45+25+15+5 = 150 ✅

-- ======================
-- Product28: Lipstick Gift Set（Total inventory 100）
-- ======================
(28, 'SKU131', 'Lipstick Gift Set', 'Classic Ruby Red', 299.00, 40, ''),
(28, 'SKU132', 'Lipstick Gift Set', 'Soft Rose Bean', 299.00, 30, ''),
(28, 'SKU133', 'Lipstick Gift Set', 'Vibrant Tomato Red', 299.00, 15, ''),
(28, 'SKU134', 'Lipstick Gift Set', 'Coral Romance', 299.00, 10, ''),
(28, 'SKU135', 'Lipstick Gift Set', 'Vintage Crimson Red', 299.00, 5, ''), -- 40+30+15+10+5 = 100 ✅

-- ======================
-- Product29: Quick-Dry Performance T-Shirt（Total inventory 200）
-- ======================
(29, 'SKU136', 'Quick-Dry Performance T-Shirt', 'White', 79.00, 80, ''),
(29, 'SKU137', 'Quick-Dry Performance T-Shirt', 'Black', 79.00, 60, ''),
(29, 'SKU138', 'Quick-Dry Performance T-Shirt', 'Grey', 79.00, 30, ''),
(29, 'SKU139', 'Quick-Dry Performance T-Shirt', 'Blue', 79.00, 20, ''),
(29, 'SKU140', 'Quick-Dry Performance T-Shirt', 'Neon Lime Green', 79.00, 10, ''), -- 80+60+30+20+10 = 200 ✅

-- ======================
-- Product30: Athletic Sports Shorts（Total inventory 250）
-- ======================
(30, 'SKU141', 'Athletic Sports Shorts', 'Black', 59.00, 100, ''),
(30, 'SKU142', 'Athletic Sports Shorts', 'Grey', 59.00, 70, ''),
(30, 'SKU143', 'Athletic Sports Shorts', 'Blue', 59.00, 40, ''),
(30, 'SKU144', 'Athletic Sports Shorts', 'White', 59.00, 25, ''),
(30, 'SKU145', 'Athletic Sports Shorts', 'Aero-Dry Performance', 59.00, 15, ''); -- 100+70+40+25+15 = 250 ✅



-- Order & delivery schema optimizations consolidated into table DDL



Drop TABLE if EXISTS `sms_activity`;
CREATE TABLE `sms_activity` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key ID',
`title` varchar(100) NOT NULL COMMENT 'Activity title',
`type` tinyint NOT NULL COMMENT 'Activity type: 1-Seckill 2-Coupon 3-Follow discount 4-Full reduction',
`start_time` datetime NOT NULL COMMENT 'Activity start time',
`end_time` datetime NOT NULL COMMENT 'Activity end time',
`status` tinyint NOT NULL DEFAULT '0' COMMENT 'Activity status: 0-Not started 1-In progress 2-Ended 3-Off-shelf',
`description` varchar(500) DEFAULT NULL COMMENT 'Activity description',
`create_by` varchar(64) DEFAULT NULL COMMENT 'Created by',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
`update_by` varchar(64) DEFAULT NULL COMMENT 'Updated by',
`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update timestamp',
`is_deleted` tinyint NOT NULL DEFAULT '0' COMMENT 'Is deleted: 0-Active 1-Deleted',
`sms_status` varchar(50) DEFAULT NULL COMMENT '3-offline 4-online',
`user_level_limit` int DEFAULT NULL,
`order_type_limit` int DEFAULT NULL,
PRIMARY KEY (`id`),
KEY `idx_type` (`type`),
KEY `idx_status` (`status`),
KEY `idx_time` (`start_time`, `end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Marketing Activities Table';

INSERT INTO `sms_activity` (`id`, `title`, `type`, `start_time`, `end_time`, `status`, `description`, `create_by`, `create_time`, `is_deleted`, `sms_status`, `user_level_limit`, `order_type_limit`) VALUES
(1, 'Cyber Super Flash Sale', 1, '2026-01-01 00:00:00', '2026-12-31 23:59:59', 1, 'Limited quantity flash sale on flagship devices', 'admin', NOW(), 0, '4', 0, 0),
(2, 'Spring Tech Voucher Carnival', 2, '2026-01-01 00:00:00', '2026-12-31 23:59:59', 1, 'Special coupon vouchers for digital accessories', 'admin', NOW(), 0, '4', 0, 0),
(3, 'New Subscriber VIP Perk', 3, '2026-01-01 00:00:00', '2026-12-31 23:59:59', 1, 'Exclusive follower price discounts', 'admin', NOW(), 0, '4', 0, 0),
(4, 'Storewide Volume Rebate', 4, '2026-01-01 00:00:00', '2026-12-31 23:59:59', 1, 'Tiered full reduction across all apparel', 'admin', NOW(), 0, '4', 0, 0);

DROP TABLE IF EXISTS `sms_seckill`;
CREATE TABLE `sms_seckill` (
 `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key ID',
 `activity_id` bigint NOT NULL COMMENT 'Associated activity ID',
 `product_id` bigint NOT NULL COMMENT 'Product ID',
 `sku_id` bigint NOT NULL COMMENT 'Product SKU ID',
 `seckill_price` decimal(10,2) NOT NULL COMMENT 'Seckill promotional price',
 `original_price` decimal(10,2) NOT NULL COMMENT 'Original retail price',
 `stock` int NOT NULL COMMENT 'Activity inventory allowance',
 `sold_stock` int NOT NULL DEFAULT '0' COMMENT 'Sold inventory count',
 `limit_quantity` int NOT NULL DEFAULT '1' COMMENT 'User purchase quantity limit',
`user_level_limit` int DEFAULT NULL,
`order_type_limit` int DEFAULT NULL,
 PRIMARY KEY (`id`),
 UNIQUE KEY `uk_activity_sku` (`activity_id`, `sku_id`),
 KEY `idx_activity_id` (`activity_id`),
 KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Seckill Activities Table';

INSERT INTO `sms_seckill` (`id`, `activity_id`, `product_id`, `sku_id`, `seckill_price`, `original_price`, `stock`, `sold_stock`, `limit_quantity`, `user_level_limit`, `order_type_limit`) VALUES
(1, 1, 1, 1, 5999.00, 7999.00, 20, 5, 1, 0, 0),
(2, 1, 2, 6, 4999.00, 6999.00, 30, 12, 1, 0, 0),
(3, 1, 6, 21, 99.00, 299.00, 80, 35, 2, 0, 0),
(4, 1, 7, 26, 129.00, 259.00, 50, 22, 1, 0, 0);


DROP TABLE IF EXISTS `sms_coupon`;
CREATE TABLE `sms_coupon` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key ID',
  `activity_id` bigint NOT NULL COMMENT 'Associated activity ID',
  `coupon_type` tinyint NOT NULL COMMENT 'Coupon type: 1-Threshold reduction 2-Direct deduction 3-Discount rate',
  `discount_value` decimal(10,2) NOT NULL COMMENT 'Discount amount or percentage',
  `min_consume` decimal(10,2) NOT NULL DEFAULT '0' COMMENT 'Minimum consumption threshold',
  `use_scope` tinyint NOT NULL COMMENT 'Usage scope：1-All Store Products 2-Designated Categories 3-Designated Products',
  `total_count` int NOT NULL COMMENT 'Total quota issued',
  `received_count` int NOT NULL DEFAULT '0' COMMENT 'Total claimed count',
  `used_count` int NOT NULL DEFAULT '0' COMMENT 'Total used count',
  `limit_per_user` int NOT NULL DEFAULT '1' COMMENT 'Claim limit per user',
  `receive_start_time` datetime NOT NULL COMMENT 'Claim period start time',
  `receive_end_time` datetime NOT NULL COMMENT 'Claim period end time',
  `use_start_time` datetime NOT NULL COMMENT 'Usage valid start time',
  `use_end_time` datetime NOT NULL COMMENT 'Usage valid end time',
`user_level_limit` int DEFAULT NULL,
`order_type_limit` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_activity_id` (`activity_id`),
  KEY `idx_use_end_time` (`use_end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Coupons Table';


DROP TABLE IF EXISTS `sms_follow_discount`;
CREATE TABLE `sms_follow_discount` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key ID',
  `activity_id` bigint NOT NULL COMMENT 'Associated activity ID',
  `discount_amount` decimal(10,2) NOT NULL COMMENT 'Instant discount amount',
  `use_condition` tinyint NOT NULL COMMENT 'Usage condition: 1-No threshold 2-First order only',
  `use_scope` tinyint NOT NULL COMMENT 'Usage scope：1-All Store Products 2-Designated Products',
  `participant_count` int NOT NULL DEFAULT '0' COMMENT 'Participant user count',
  `used_count` int NOT NULL DEFAULT '0' COMMENT 'Redeemed order count',
  `total_discount_amount` decimal(12,2) NOT NULL DEFAULT '0' COMMENT 'Total promotional discount amount',
`user_level_limit` int DEFAULT NULL,
`order_type_limit` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_activity_id` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Follow Discounts Table';

-- ==============================
-- Optimized: supports tiered threshold reduction and multi-level discounts
-- ==============================
DROP TABLE IF EXISTS `sms_full_reduction`;
CREATE TABLE `sms_full_reduction` (
 `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key ID',
 `activity_id` bigint NOT NULL COMMENT 'Associated activity ID',
 `full_amount` decimal(10,2) NOT NULL COMMENT 'Threshold ReductionThresholdAmount',
 `reduction_amount` decimal(10,2) NOT NULL COMMENT 'Threshold ReductionAmount',
 `discount_rate` decimal(5,2) DEFAULT NULL COMMENT 'Discount rate (e.g. 0.85 = 15% off)',
 `rule_type` tinyint NOT NULL DEFAULT 1 COMMENT 'Rule type: 1-Threshold reduction, 2-Discount rate',
 `overlay_rule` tinyint NOT NULL DEFAULT '0' COMMENT 'Stacking rule: 0-Cannot stack, 1-Can stack with coupons',
 `use_scope` tinyint NOT NULL COMMENT 'Usage scope：1-All Store Products 2-Designated Categories 3-Designated Products',
 `order_count` int NOT NULL DEFAULT '0' COMMENT ' and Order count',
 `total_discount_amount` decimal(12,2) NOT NULL DEFAULT '0' COMMENT 'Total promotional discount amount',
`user_level_limit` int DEFAULT NULL,
`order_type_limit` int DEFAULT NULL,
 PRIMARY KEY (`id`),
 KEY `idx_activity_id` (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Full Reduction Activities Table（Multi-tierThreshold Reduction+Discount）';


DROP TABLE IF EXISTS `sms_activity_product`;
CREATE TABLE `sms_activity_product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key ID',
  `activity_id` bigint NOT NULL COMMENT 'Associated activity ID',
  `product_id` bigint NOT NULL COMMENT 'Product ID',
  `category_id` bigint DEFAULT NULL COMMENT 'ProductCategory ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_activity_product` (`activity_id`, `product_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='ActivitiesProductAssociated  Table';


-- Carrier NameDictionary Table oms_delivery_company 
DROP TABLE IF EXISTS `oms_delivery_company`;
CREATE TABLE `oms_delivery_company` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key ID',
  `company_code` varchar(32) NOT NULL COMMENT 'Express CarrierCode SF/YTO/ZTO',
  `company_name` varchar(50) NOT NULL COMMENT 'Carrier Name',
  `sort` int DEFAULT 0 COMMENT 'Sort order',
  `status` tinyint DEFAULT 1 COMMENT 'Status 0Disabled 1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update timestamp',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Carrier NameDictionary Table';

-- Carrier NameData
INSERT INTO `oms_delivery_company` (`company_code`,`company_name`,`sort`) VALUES
  ('SF','SF Express',1),
  ('YTO','YTO Express',2),
  ('ZTO','ZTO Express',3),
  ('STO','STO Express',4),
  ('YD','Yunda Express',5),
  ('JD','JD Logistics',6),
  ('EMS','China Post EMS',7),
  ('YT','J&T Express',8),
  ('JDYZ','JD Post Express',9),
  ('BS','Best Express',10);



TRUNCATE TABLE oms_order_delivery;

INSERT INTO `oms_order_delivery`
(`order_id`,`order_no`,`delivery_company_id`,`delivery_user`,`delivery_user_phone`,`delivery_no`,`delivery_status`,`delivery_time`,`sign_time`,`operator`,`remark`,`create_time`,`update_time`)
VALUES
    (1,'ORDER20260',1,'David Wilson','16631952038','SF2026033000',2,'2026-03-30 09:30:00','2026-03-31 10:00:00','admin','Order signed and accepted normally','2026-04-04 11:19:51','2026-04-17 10:00:00'),
    (2,'ORDER20260',2,'Brian Davis','400-800-8888','YTO202603300',3,'2026-03-30 13:40:00','2026-04-01 10:00:00','admin','Transit completed successfully','2026-04-04 11:19:51','2026-04-17 11:20:00'),
    (3,'ORDER20260',1,'David Wilson','16631952038','SF2026033000',3,'2026-03-30 11:00:00','2026-03-31 11:00:00','admin','Delayed at customer request','2026-04-04 11:19:51','2026-04-17 11:20:00'),
    (4,'ORDER20260',3,'Brian Davis','400-800-8888','ZTO202603300',3,'2026-03-30 17:20:00','2026-04-01 11:00:00','admin','Completed normally','2026-04-04 11:19:51','2026-04-17 11:20:00'),
    (5,'ORDER20260',6,'Ethan Taylor','95152','JD2026040400',2,'2026-04-04 11:00:00',NULL,'admin','Package currently in transit','2026-04-04 11:19:51','2026-04-17 11:20:00'),
    (6,'ORDER20260',NULL,'Frank Zhou','111000000','JD2026040400',0,NULL,NULL,'admin','Pending pickup from merchant warehouse','2026-04-04 11:19:51','2026-04-17 11:20:00'),
    (7,'ORDER20260',4,'Grace Johnson','95554','STO202604040',2,'2026-04-04 11:00:00',NULL,'admin','Package out for local delivery','2026-04-04 11:19:51','2026-04-17 11:20:00'),
    (8,'ORDER20260',1,'Ethan Taylor','95152','SF2026040400',2,'2026-04-04 11:00:00',NULL,'admin','Package currently in transit','2026-04-04 11:19:51','2026-04-17 11:20:00'),
    (9,'ORDER20260',2,'Grace Johnson','95554','YTO202604040',2,'2026-04-04 11:00:00',NULL,'admin','Package currently in transit','2026-04-04 11:19:51','2026-04-17 11:20:00'),
    (10,'ORDER20260',1,'Ethan Taylor','95152','SF2026040400',3,'2026-04-04 11:00:00','2026-04-05 10:00:00','admin','Package delivered and signed normally','2026-04-04 11:19:51','2026-04-17 11:20:00'),
    (11,'ORDER20260',7,'Sarah Sister','89981','SF2026040400',1,'2026-04-16 13:24:00',NULL,'admin','Package Picked Up','2026-04-04 11:19:51','2026-04-18 14:20:00'),
    (12,'ORDER20260',6,'David Wilson','400-606-5500','JD2026040401',2,'2026-04-04 11:00:00',NULL,'admin','Package currently in transit','2026-04-04 11:19:51','2026-04-17 11:20:00'),
    (13,'ORDER20260',3,'Brian Davis','400-800-8888','ZTO202604040',3,'2026-04-04 11:00:00','2026-04-05 11:00:00','admin','Delivered and confirmed','2026-04-04 11:19:51','2026-04-17 11:20:00'),
    (14,'ORDER20260',4,'Grace Johnson','95554','STO202604040',2,'2026-04-04 11:00:00',NULL,'admin','Package out for local delivery','2026-04-04 11:19:51','2026-04-17 11:20:00'),
    (15,'ORDER20260',1,'Ethan Taylor','95152','SF2026040400',3,'2026-04-04 11:00:00','2026-04-05 11:00:00','admin','Delivered and confirmed','2026-04-04 11:19:51','2026-04-17 11:20:00');


UPDATE oms_order_delivery SET delivery_company_id = NULL WHERE id = 6;

-- ----------------------------
--  Table structure for financial ledger table
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_finance`;
CREATE TABLE `oms_order_finance` (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Financial ledger ID',
                                     `order_id` bigint NOT NULL COMMENT 'Order ID (foreign key to oms_order)',
                                     `order_no` varchar(64) NOT NULL COMMENT 'Order number',
                                     `user_id` bigint NOT NULL COMMENT 'User ID (foreign key to ums_user)',
                                     `total_amount` decimal(10,2) NOT NULL COMMENT 'Order total amount',
                                     `pay_amount` decimal(10,2) NOT NULL COMMENT 'Actual paid amount',
                                     `refund_amount` decimal(10,2) DEFAULT '0.00' COMMENT 'Refund amount',
                                     `pay_type` tinyint NOT NULL COMMENT 'Payment method: 0-Unpaid 1-WeChat 2-Alipay',
                                     `pay_status` tinyint NOT NULL DEFAULT '0' COMMENT 'Payment status: 0-Pending 1-Paid 2-Refunding 3-Refunded',
                                     `pay_time` datetime DEFAULT NULL COMMENT 'Payment timestamp',
                                     `refund_time` datetime DEFAULT NULL COMMENT 'Refund timestamp',
                                     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
                                     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update timestamp',
                                     PRIMARY KEY (`id`),
                                     KEY `idx_order_id` (`order_id`),
                                     KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OrderLedger Table';

-- ----------------------------
-- Seed test records
-- ----------------------------
INSERT INTO `oms_order_finance` VALUES
                                    (1, 1, 'ORDER20260330001', 1, 897.00, 897.00, 0.00, 1, 1, '2026-03-30 09:15:00', NULL, '2026-03-30 09:15:00', '2026-03-30 09:15:00'),
                                    (2, 4, 'ORDER202604040001', 2, 299.00, 299.00, 0.00, 1, 1, '2026-04-04 10:38:35', NULL, '2026-04-04 10:38:35', '2026-04-04 10:38:35'),
                                    (3, 21, 'ORDER20260330004', 1, 68.00, 68.00, 0.00, 1, 1, '2026-03-30 16:50:00', NULL, '2026-03-30 16:50:00', '2026-03-30 16:50:00');


-- ----------------------------
-- Table structure for order after-sales table
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_after_sales`;
CREATE TABLE `oms_order_after_sales` (
                                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'After-sales request ID',
                                         `after_sales_no` varchar(64) NOT NULL COMMENT 'After-sales tracking number',
                                         `order_id` bigint NOT NULL COMMENT 'Order ID',
                                         `order_item_id` bigint NOT NULL COMMENT 'Order item ID',
                                         `user_id` bigint NOT NULL COMMENT 'User ID',
                                         `product_id` bigint NOT NULL COMMENT 'Product ID',
                                         `sku_id` bigint NOT NULL COMMENT 'SKU ID',
                                         `after_sales_type` tinyint NOT NULL COMMENT 'After-sales type: 1-Return 2-Exchange 3-Repair',
                                         `apply_quantity` int NOT NULL COMMENT 'Requested quantity',
                                         `apply_reason` varchar(500) NOT NULL COMMENT 'Application reason',
                                         `apply_status` tinyint NOT NULL DEFAULT 0 COMMENT 'Status: 0-Pending 1-Approved 2-Completed 3-Rejected',
                                         `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
                                         `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update timestamp',
                                         PRIMARY KEY (`id`),
                                         KEY `idx_order_id` (`order_id`),
                                         KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OrderAfter-Sales Table';

-- TestData
INSERT INTO `oms_order_after_sales` VALUES
                                        (1,'AFTER20260420001',1,1,1,311,311,1,1,'Changed my mind / No longer needed',0,NOW(),NOW()),
                                        (2,'AFTER20260420002',4,4,2,346,346,2,1,'Product defective / Quality issue',1,NOW(),NOW());


-- ----------------------------
-- Table structure for OrderReview Table
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_comment`;
CREATE TABLE `oms_order_comment` (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Comment ID',
                                     `order_id` bigint NOT NULL COMMENT 'Order ID',
                                     `order_item_id` bigint NOT NULL COMMENT 'Order item ID（Associated Product）',
                                     `user_id` bigint NOT NULL COMMENT 'ReviewUser ID',
                                     `product_id` bigint NOT NULL COMMENT 'Product ID',
                                     `sku_id` bigint NOT NULL COMMENT 'SKU ID',
                                     `score` tinyint NOT NULL DEFAULT 5 COMMENT ' min 1-5',
                                     `comment_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Comment timestamp',
                                     `comment_content` varchar(500) NOT NULL COMMENT 'ReviewContent',
                                     `reply_content` varchar(500) DEFAULT NULL COMMENT 'Reply',
                                     `reply_time` datetime DEFAULT NULL COMMENT 'Reply timestamp',
                                     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
                                     PRIMARY KEY (`id`),
                                     KEY `idx_user_id` (`user_id`),
                                     KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OrderReview Table';

-- ----------------------------
-- Seed test records
-- ----------------------------
INSERT INTO `oms_order_comment` VALUES
                                    (1, 1, 1, 1, 311, 311, 5, '2026-03-30 15:00:00', 'The sound quality of the earbuds is crystal clear and shipping was extremely fast!', NULL, NULL, '2026-03-30 15:00:00'),
                                    (2, 8, 8, 2, 346, 346, 4, '2026-04-04 12:00:00', 'The shoes are very comfortable, though they run slightly small so order one size up.', NULL, NULL, '2026-04-04 12:00:00');



DROP TABLE IF EXISTS `sys_table_column_config`;
CREATE TABLE `sys_table_column_config` (
                                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key ID',
                                           `user_id` bigint NOT NULL COMMENT 'Current authenticated user ID',
                                           `page_code` varchar(64) NOT NULL COMMENT 'Page code: logistics_order_list',
                                           `column_code` varchar(64) NOT NULL COMMENT 'Table column unique field code',
                                           `column_name` varchar(32) NOT NULL COMMENT 'Table column display header',
                                           `is_show` tinyint NOT NULL DEFAULT '1' COMMENT 'Visibility: 1-Show 0-Hide',
                                           `sort_num` int NOT NULL DEFAULT '0' COMMENT 'Table column sort order',
                                           `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
                                           `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update timestamp',
                                           PRIMARY KEY (`id`),
    -- Composite unique index: one configuration per user + page + column
                                           UNIQUE KEY `uk_user_page_column` (`user_id`,`page_code`,`column_code`),
    -- Fast lookup index
                                           KEY `idx_user_page` (`user_id`,`page_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User tableListConfig Table';


-- Default table column display config for admin user (user_id=1)
INSERT INTO sys_table_column_config (user_id, page_code, column_code, column_name, is_show, sort_num)
VALUES
    (1, 'logistics_order_list', 'order_no', 'Order Number', 1, 1),
    (1, 'logistics_order_list', 'receiver_name', 'Recipient Name', 1, 2),
    (1, 'logistics_order_list', 'receiver_phone', 'Phone Number', 0, 3),
    (1, 'logistics_order_list', 'logistics_status', 'Logistics Status', 1, 4),
    (1, 'logistics_order_list', 'express_company', 'Carrier Name', 1, 5),
    (1, 'logistics_order_list', 'express_no', 'Tracking Number', 1, 6),
    (1, 'logistics_order_list', 'sender_name', 'Sender Name', 1, 7),
    (1, 'logistics_order_list', 'send_time', 'Dispatch Time', 1, 8),
    (1, 'logistics_order_list', 'logistics_update_time', 'Latest Tracking Time', 1, 9);
-- SMS activity extensions consolidated into table DDL



DROP TABLE IF EXISTS `product_params`;
CREATE TABLE `product_params` (
                                  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
                                  `products_id` int NOT NULL COMMENT 'Product ID',
                                  `brand` varchar(100) DEFAULT '' COMMENT 'Brand',
                                  `model` varchar(100) DEFAULT '' COMMENT 'Model',
                                  `material` varchar(100) DEFAULT '' COMMENT 'Material',
                                  `origin` varchar(100) DEFAULT '' COMMENT 'Origin',
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='ProductSpecificationParameters Table';



INSERT INTO `product_params` (`id`, `products_id`, `brand`, `model`, `material`, `origin`)
VALUES
-- 1 Apple iPhone 17Phone
(1, 1, 'Apple', 'iPhone 17', 'Ceramic Glass + Aerospace Aluminum', 'United States'),
-- 2 Huawei Mate 70Phone
(2, 2, 'Huawei', 'Mate 70', 'Vegan Leather + Metal Frame', 'China'),
-- 4 Lenovo LegionComputer
(3, 4, 'Lenovo', 'Legion Gaming Series', 'Anodized Aluminum + Polycarbonate', 'China'),
-- 5 Apple Mac
(4, 5, 'Apple', 'Mac Pro Series', 'Unibody Recycled Aluminum', 'United States'),
-- 6 Wireless Bluetooth Earbuds
(5, 6, 'Universal Generic', 'Standard Edition', 'Matte ABS + Liquid Silicone', 'China'),
-- 7 Mechanical Keyboard
(6, 7, 'Universal Generic', 'Mechanical Keyboard', 'Double-Shot ABS + Brushed Aluminum', 'China'),
-- 8 Tablet Computer
(7, 8, 'Universal Generic', 'Standard Edition', 'Aluminum Unibody + Gorilla Glass', 'China'),
-- 9 Pure Cotton Short Sleeve T-Shirt
(8, 9, 'Universal Generic', 'Essential Classic', '100% Combed Cotton', 'China'),
-- 10 Straight Leg Denim Jeans
(9, 10, 'Universal Generic', 'Straight-Leg Regular Fit', 'Raw Denim Cotton', 'China'),
-- 11 Athletic Sports T-Shirt
(10, 11, 'Athletic Pro', 'Aero-Dry Performance', 'Moisture-Wicking Polyester', 'China'),
-- 12 Casual Jacket
(11, 12, 'Universal Generic', 'Relaxed Urban Fit', 'Cotton Blend Twill', 'China'),
-- 13 Breathable Mesh Sneakers
(12, 13, 'Athletic Pro', 'AeroMesh Breathable', 'Engineered Knit Mesh + Rubber Sole', 'China'),
-- 14 Commuter Backpack
(13, 14, 'Universal Generic', 'Daily Commuter', '900D Waterproof Oxford Fabric', 'China'),
-- 15 Classic Baseball Cap
(14, 15, 'Universal Generic', 'Essential Classic', '100% Organic Cotton', 'China'),
-- 16 Premium Coffee Beans
(15, 16, 'Universal Generic', 'Medium Roast', '100% Arabica Single Origin', 'Colombia'),
-- 17 Pure Whole Milk
(16, 17, 'Universal Generic', 'Whole Pasteurized Milk', '100% Fresh Raw Cow Milk', 'China'),
-- 18 Crispy Potato Chips Gift Pack
(17, 18, 'Universal Generic', 'Assorted Flavors Combo', 'Selected Potato Starch', 'China'),
-- 19 Natural Mineral Water
(18, 19, 'Universal Generic', 'Natural Spring Source', 'Natural Deep Mountain Spring Water', 'China'),
-- 20 Assorted Nuts Gift Box
(19, 20, 'Universal Generic', 'Gourmet Selection', 'Premium Mixed Roasted Nuts', 'China'),
-- 21 Solid Wood Sofa
(20, 21, 'Universal Generic', '3-Seater', 'FSC Solid Beech Wood + Linen Fabric', 'China'),
-- 22 Dining Table and Chairs Set
(21, 22, 'Universal Generic', '4-Seater Compact', 'Solid Oak Wood + Powder-Coated Steel', 'China'),
-- 23 Pure Cotton 4-Piece Bedding Set
(22, 23, 'Universal Generic', 'Standard Edition', '100% Combed Cotton', 'China'),
-- 24 Natural Latex Mattress
(23, 24, 'Universal Generic', 'Standard Edition', 'Natural Latex Core', 'Thailand'),
-- 25 Hydrating Skincare Lotion Set
(24, 25, 'Luxury Beauty Lab', 'Hydrating Edition', 'Nourishing Skin Essence', 'China'),
-- 26 SPF50+ Sunscreen Lotion
(25, 26, 'Luxury Beauty Lab', 'SPF50+', 'Hybrid Mineral & Chemical Filters', 'Japan'),
-- 27 Cushion BB Cream
(26, 27, 'Luxury Beauty Lab', 'Natural Beige', 'Cushion Powder Compact', 'South Korea'),
-- 28 Lipstick Gift Set
(27, 28, 'Luxury Beauty Lab', 'Multicolor Edition', 'Lipstick', 'France'),
-- 29 Quick-Dry Performance T-Shirt
(28, 29, 'Athletic Pro', 'Aero-Dry Performance', 'Moisture-Wicking Polyester', 'China'),
-- 30 Athletic Sports Shorts
(29, 30, 'Athletic Pro', 'Aero-Dry Performance', 'Moisture-Wicking Polyester', 'China');

DROP TABLE IF EXISTS `pms_product_feature`;
CREATE TABLE `pms_product_feature` (
                                       `id` int NOT NULL AUTO_INCREMENT COMMENT 'Primary key ID',
                                       `product_id` int NOT NULL COMMENT 'Product ID associated with pms_product table',
                                       `feature_title` varchar(50) NOT NULL COMMENT 'Title（e.g. ：Material）',
                                       `feature_desc` varchar(100) DEFAULT '' COMMENT 'Description（e.g. : Sustained Resilience）',
                                       `icon` varchar(100) DEFAULT '' COMMENT 'Icons（VantIcons or ImagesAddress）',
                                       `sort` int DEFAULT 0 COMMENT 'Sort order，',
                                       `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
                                       PRIMARY KEY (`id`),
                                       KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Product Table';
-- ====================== Product ID 1 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (1, '100% Genuine Official Merchandise', 'Authenticity verified with buyer protection', 'check', 1),
                                                                                                      (1, 'Priority express warehouse dispatch', 'Dispatches rapidly within 48 hours', 'clock', 2),
                                                                                                      (1, 'High-Grade Materials', 'Durable textured grip feels great in hand', 'star-o', 3),
                                                                                                      (1, 'Official Warranty', 'Comprehensive dedicated customer care', 'shield-o', 4);

-- ====================== Product ID 2 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (2, 'Eco Materials', 'Natural non-toxic environmentally conscious', 'leaf', 1),
                                                                                                      (2, 'Fine Craftsmanship', 'Precision engineered down to the millimetre', 'fire-o', 2),
                                                                                                      (2, 'Ergonomic Design', 'Engineered for intuitive daily workflow', 'gem-o', 3),
                                                                                                      (2, 'Built to Last', 'Structural durability that resists wear', 'check', 4);

-- ====================== Product ID 4 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (4, 'Plush Softness', 'Gentle on sensitive skin with cloud touch', 'friend-o', 1),
                                                                                                      (4, 'Ultra Breathable', 'Year-round temperature regulating comfort', 'cloud-o', 2),
                                                                                                      (4, 'Colorfast Dye', 'Resists fading wash after wash', 'shield-o', 3),
                                                                                                      (4, 'Versatile Silhouette', 'Effortlessly transitions through all settings', 'star-o', 4);

-- ====================== Product ID 5 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (5, 'Reinforced Shell', 'Drop-tested impact and shock resistance', 'balance', 1),
                                                                                                      (5, 'Precision Fit', 'Snug seamless alignment for maximum stability', 'location-o', 2),
                                                                                                      (5, 'Minimalist Look', 'Timeless clean aesthetic that never ages', 'eye-o', 3),
                                                                                                      (5, 'Quality Inspected', 'Rigorous quality control and certs', 'check', 4);

-- ====================== Product ID 6 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (6, 'Surge Protection', 'Multi-tier circuit and overload safeguards', 'safe', 1),
                                                                                                      (6, 'Fast Charging', 'High wattage rapid charging efficiency', 'charging', 2),
                                                                                                      (6, 'Stable Delivery', 'Smooth uninterrupted power and signal', 'wap-home', 3),
                                                                                                      (6, 'Pocket Compact', 'Ultra lightweight and easy to pack anywhere', 'edit', 4);

-- ====================== Product ID 7 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (7, 'Food-Grade Safe', 'BPA-free zero hazardous chemical leach', 'medal', 1),
                                                                                                      (7, 'Easy to Clean', 'Wipe-clean non-porous stain resistance', 'clean', 2),
                                                                                                      (7, 'Spacious Volume', 'Generous capacity for all daily essentials', 'folder-o', 3),
                                                                                                      (7, 'Durable Utility', 'The undisputed high-utility benchmark', 'fire-o', 4);

-- ====================== Product ID 8 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (8, 'Retina Display', 'Immersive expansive visual fidelity', 'photo-o', 1),
                                                                                                      (8, 'High Performance', 'Effortless high-throughput multitasking', 'rocket', 2),
                                                                                                      (8, 'Slim Profile', 'Effortless lightweight portability', 'smile-o', 3),
                                                                                                      (8, 'Long Endurance', 'Zero battery anxiety while on the go', 'battery', 4);

-- ====================== Product ID 9 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (9, 'Organic Cotton', 'Breathable and gentle without irritation', 'flower-o', 1),
                                                                                                      (9, 'Relaxed Fit', 'Unrestricted freedom of motion', 'star', 2),
                                                                                                      (9, 'Shape-retaining and deformation-resistant', 'Retains original shape and drape', 'refresh', 3),
                                                                                                      (9, 'Vibrant Palette', 'Diverse color options for every wardrobe', 'photo', 4);

-- ====================== Product ID 10 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (10, '4-Way Stretch', 'Comfort waistband with zero pinch', 'like-o', 1),
                                                                                                      (10, 'Tailored Silhouette', 'Flattering lines with tailored silhouette', 'fire', 2),
                                                                                                      (10, 'Heavy Duty', 'Tear-resistant durable daily wear', 'flag-o', 3),
                                                                                                      (10, 'Easy Pairing', 'Ideal for commute, casual, and street style', 'magic', 4);

-- ====================== Product ID 11 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (11, 'Aero Quick-Dry', 'Wicks moisture away within seconds', 'sport-o', 1),
                                                                                                      (11, 'Mesh Ventilation', 'Micro-perforated airflow eliminates clamminess', 'wind', 2),
                                                                                                      (11, 'Active Flex', 'Unencumbered agility during training', 'like', 3),
                                                                                                      (11, 'Wash Resistant', 'Shape retention after intense cycles', 'success', 4);

-- ====================== Product ID 12 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (12, 'Relaxed Casual Cut', 'Elevates everyday casual styling', 'flower', 1),
                                                                                                      (12, 'Ultra-soft breathable comfort textiles', 'Plush non-scratchy feel gentle on skin', 'gem', 2),
                                                                                                      (12, 'Anti-Pilling Knit', 'Keeps clean tailored drape without wrinkling', 'gift-o', 3),
                                                                                                      (12, 'Vibrant Palette', 'Complements diverse decor aesthetics', 'photo-o', 4);

-- ====================== Product ID 13 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (13, 'Kinetic Rebound Cushioning Tech', 'Glides with lightweight cushioned steps', 'cloud', 1),
                                                                                                      (13, 'All-terrain high-grip rubber outsole', 'Wet-weather slip-resistant safety traction', 'location', 2),
                                                                                                      (13, 'Airflow Engineered Knit Mesh', 'Eliminates foot odor and stuffiness', 'star', 3),
                                                                                                      (13, 'Contemporary Streetwear Aesthetics', 'Versatile effortless wardrobe pairing', 'fire', 4);

-- ====================== Product ID 14 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (14, 'Generous capacity for all gear', 'Engineered for travel and daily commute', 'folder', 1),
                                                                                                      (14, 'Waterproof and abrasion-resistant coating', 'Weatherproof protection against sudden rain', 'shield', 2),
                                                                                                      (14, 'Ergonomically contoured padded shoulder straps', 'Cushioned straps prevent shoulder strain', 'friend', 3),
                                                                                                      (14, 'Multi-compartment storage organization', 'Compact fold for easy neat storage', 'edit', 4);

-- ====================== Product ID 15 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (15, 'Ultra-soft breathable comfort textiles', 'Breathable skin-friendly moisture regulation', 'medal-o', 1),
                                                                                                      (15, 'Conforms snugly to head shape', 'Painless pressure-free fit for extended wear', 'user', 2),
                                                                                                      (15, 'Mesh Ventilation', 'Banishes trapped heat and humidity', 'leaf', 3),
                                                                                                      (15, 'Trendsetting Style', 'Versatile effortless wardrobe pairing', 'magic', 4);

-- ====================== Product ID 16 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (16, 'Hand-picked specialty grade coffee beans', 'Robust full-bodied aromatic profile', 'food-o', 1),
                                                                                                      (16, 'Medium Roast', 'Balanced taste profile with zero harsh bitterness', 'fire', 2),
                                                                                                      (16, 'Fresh Batch Roast', 'Seals in natural aromatic bouquet', 'star-o', 3),
                                                                                                      (16, 'Versatile Extraction', 'Seamlessly transitions across all occasions', 'wap-home', 4);

-- ====================== Product ID 17 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (17, 'Premium Dairy Heritage', 'Pure dairy source with zero additives', 'certificate', 1),
                                                                                                      (17, 'Nutrient Dense', 'Replenishes essential daily trace minerals', 'gem-o', 2),
                                                                                                      (17, 'Silky Smooth Finish', 'Velvety mouthfeel with rich lingering depth', 'smile', 3),
                                                                                                      (17, 'Sterile cleanroom manufacturing standard', 'Certified non-toxic quality assurance', 'check', 4);

-- ====================== Product ID 18 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (18, 'Party Variety', 'Caters to diverse taste preferences', 'like-o', 1),
                                                                                                      (18, 'Crisp Golden Crunch', 'Irresistible mouthwatering savory crunch', 'fire-o', 2),
                                                                                                      (18, 'Single-Serve Pack', 'Individually wrapped perfect for sharing', 'gift', 3),
                                                                                                      (18, 'Guaranteed fresh manufacturing batch', 'Cruelty-free dermatological certification', 'shield-o', 4);

-- ====================== Product ID 19 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (19, 'Natural Deep Mountain Spring Water', 'Protected pristine mountain aquifer', 'water-o', 1),
                                                                                                      (19, 'Natural Alkaline Balance pH 7.6', 'Mild naturally sweet and mellow notes', 'leaf-o', 2),
                                                                                                      (19, 'Zero Artificial Preservatives', 'Pure pristine drinking water certified safe', 'medal', 3),
                                                                                                      (19, 'Spacious Volume', 'Perfect hydration for the entire family', 'folder-o', 4);

-- ====================== Product ID 20 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (20, 'Gourmet selection of roasted nuts', 'Carefully culled plump whole kernels', 'food', 1),
                                                                                                      (20, 'Slow Low-Temp Roasting', 'Retains vital micronutrients and vitamins', 'fire', 2),
                                                                                                      (20, 'Pure clean formula without extra fillers', 'Zero additive pure formulation for wellness', 'check', 3),
                                                                                                      (20, 'Hygienic single-portion stay-fresh sachets', 'Lightweight and easily portable', 'gift-o', 4);

-- ====================== Product ID 21 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (21, 'Handcrafted Solid Hardwood', 'Eco-friendly structural durability', 'medal', 1),
                                                                                                      (21, 'Zero-VOC eco-friendly protective lacquer', 'Zero odor certified non-toxic finish', 'leaf', 2),
                                                                                                      (21, 'Polished edges with rounded smooth finish', 'Smooth chamfered corners prevent accidental bumps', 'magic', 3),
                                                                                                      (21, 'Heavy-duty load-bearing capacity', 'Rock-solid vibration-free foundation', 'shield', 4);

-- ====================== Product ID 22 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (22, 'Understated Minimalist Design', 'Harmonizes with any interior decor style', 'photo', 1),
                                                                                                      (22, 'Sustainably Sourced Eco Materials', 'Zero formaldehyde certified eco-safe', 'flower-o', 2),
                                                                                                      (22, 'Massive Storage', 'Eliminates desk and pocket clutter', 'folder', 3),
                                                                                                      (22, 'Effortless tool-free assembly', 'Saves prep time with zero hassle', 'rocket', 4);

-- ====================== Product ID 23 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (23, 'Organic Cotton', 'Silky breathable contact prevents clamminess', 'flower', 1),
                                                                                                      (23, 'High-Thread-Count Sateen Weave', 'Velvety smooth cloud-soft comfort', 'gem', 2),
                                                                                                      (23, 'Anti-Pilling Knit', 'Resists wrinkling and stays flat with long-term use', 'refresh', 3),
                                                                                                      (23, 'Reactive Dyeing', 'Colorfast fabric stays rich after countless washes', 'photo-o', 4);

-- ====================== Product ID 24 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (24, 'Natural Latex Core', 'Hypoallergenic breathable anti-mite barrier', 'medal-o', 1),
                                                                                                      (24, 'Adapts naturally to body contours', 'Ergonomic contour relieves lumbar strain', 'like', 2),
                                                                                                      (24, 'Acoustic Dampening Whisper-Quiet Build', 'Independent motion isolation pocket coils', 'moon-o', 3),
                                                                                                      (24, 'Sustained Resilience', 'Retains posture contour after years of use', 'shield-o', 4);

-- ====================== Product ID 25 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (25, 'Hypoallergenic', 'Dermatologist-tested zero-irritation formula', 'smile-o', 1),
                                                                                                      (25, 'Deep Hydration', 'Multi-molecular moisture lock barrier', 'water', 2),
                                                                                                      (25, 'Lightweight Texture', 'Non-greasy fast-absorbing fluid finish', 'flower', 3),
                                                                                                      (25, 'Zero Alcohol Added', 'Paraben-free and safe for daily regimen', 'check', 4);

-- ====================== Product ID 26 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (26, 'Broad-Spectrum SPF50+', 'Shields against UVA/UVB photo-aging', 'sun-o', 1),
                                                                                                      (26, 'Lightweight Texture', 'Invisible finish with zero white cast', 'water-o', 2),
                                                                                                      (26, 'Water & Sweat Proof', 'Stays active during sports and humidity', 'shield', 3),
                                                                                                      (26, 'Pore-Clearing Breathable', 'Non-comedogenic wear that won''t clog pores', 'smile', 4);

-- ====================== Product ID 27 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (27, 'Weightless Blending', 'Melts into skin for a second-skin veil', 'magic', 1),
                                                                                                      (27, 'Radiant Camouflage', 'Evens redness and blemishes with glass glow', 'gem-o', 2),
                                                                                                      (27, 'Water & Sweat Proof', '16-Hour Longwear', 'fire-o', 3),
                                                                                                      (27, 'Botanical Infused', 'Enriched with anti-inflammatory essence', 'flower-o', 4);

-- ====================== Product ID 28 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (28, 'Festive Velvet Box', 'Prestige packaging designed for gifting', 'gift', 1),
                                                                                                      (28, 'Couture Shades', 'Iconic curated spectrum for every occasion', 'like-o', 2),
                                                                                                      (28, 'Silky Satin Glide', 'Rich saturated pigment in a single swipe', 'food-o', 3),
                                                                                                      (28, 'Authentic Certified', 'Cruelty-free dermatological certification', 'certificate', 4);

-- ====================== Product ID 29 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (29, 'Hydro-Cool Tech', 'Wicks moisture away within seconds', 'sport-o', 1),
                                                                                                      (29, 'Mesh Ventilation', 'Micro-perforated airflow eliminates clamminess', 'wind', 2),
                                                                                                      (29, 'Dynamic Flex', 'Unencumbered agility during training', 'like', 3),
                                                                                                      (29, 'Shape-retaining and deformation-resistant', 'Keeps clean tailored drape without wrinkling', 'refresh', 4);

-- ====================== Product ID 30 ======================
INSERT INTO `pms_product_feature` (`product_id`, `feature_title`, `feature_desc`, `icon`, `sort`) VALUES
                                                                                                      (30, '4-Way Stretch', 'Designed for maximum athletic comfort', 'like-o', 1),
                                                                                                      (30, 'Relaxed Fit', 'Unrestricted freedom of active motion', 'star', 2),
                                                                                                      (30, 'Ultra Breathable', 'Micro-perforated airflow eliminates clamminess', 'cloud-o', 3),
                                                                                                      (30, 'Anti-Pilling Knit', 'Keeps clean tailored drape without wrinkling', 'refresh', 4);
DROP TABLE IF EXISTS `ai_chat_memory`;
CREATE TABLE `ai_chat_memory` (
                                  `id` bigint NOT NULL AUTO_INCREMENT,
                                  `memory_id` varchar(64) NOT NULL COMMENT 'Session unique identifier (userId/sessionId)',
                                  `message_content` text COMMENT 'Message content',
                                  `message_type` varchar(20) COMMENT 'USER/AI',
                                  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
                                  PRIMARY KEY (`id`),
                                  KEY `idx_memory_id` (`memory_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `ums_cart`;
CREATE TABLE `ums_cart` (
 `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Shopping cart item ID',
 `user_id` bigint NOT NULL COMMENT 'User ID',
 `sku_id` bigint NOT NULL COMMENT 'ProductSKU ID',
 `quantity` int NOT NULL DEFAULT 1 COMMENT 'Purchase quantity',
 `selected` tinyint NOT NULL DEFAULT 1 COMMENT 'Selection status: 1-Selected 0-Unselected',
 `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
 `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
 PRIMARY KEY (`id`),
 UNIQUE KEY `uk_user_sku` (`user_id`,`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT=' Table';


DROP TABLE IF EXISTS `product_comment_image`;
CREATE TABLE `product_comment_image` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Primary key ID',
`comment_id` bigint NOT NULL COMMENT 'Associated comment ID',
`img_url` varchar(255) NOT NULL COMMENT 'Image URL',
`sort` int DEFAULT 0 COMMENT 'Image sort order (descending)',
`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Creation timestamp',
PRIMARY KEY (`id`),
KEY `idx_comment_id` (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='ReviewImages Table（ itemsReview coupons）';

DROP TABLE IF EXISTS `comment_tag_relation`;
DROP TABLE IF EXISTS `comment_tag`;
CREATE TABLE `comment_tag` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT 'Tag ID',
`tag_name` varchar(50) NOT NULL COMMENT 'Tag name',
`sort` int DEFAULT 0 COMMENT 'Sort order',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Review tags dictionary';

-- Seed initial comment tags
INSERT INTO `comment_tag` (`tag_name`) VALUES
 ('Excellent Quality'),
 ('Great Value for Money'),
 ('Super Fast Delivery'),
 ('Pristine Packaging'),
 ('Accurate to Description'),
 ('Highly Recommended');

CREATE TABLE `comment_tag_relation` (
                                        id bigint PRIMARY KEY AUTO_INCREMENT,
                                        comment_id bigint NOT NULL,  -- Comment ID
                                        tag_id bigint NOT NULL       -- Tag ID
);