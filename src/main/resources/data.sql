-- data.sql PostgreSQL — ShopFlow sans DTO
-- Mot de passe = "password" (BCrypt)

INSERT INTO users (id,prenom,nom,email,mot_de_passe,role,actif,date_creation)
VALUES
(1,'Admin','GLID','admin@shopflow.com','$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi','ADMIN',true,NOW()),
(2,'Seller','Demo','seller@shopflow.com','$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi','SELLER',true,NOW()),
(3,'Client','Demo','client@shopflow.com','$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi','CUSTOMER',true,NOW())
ON CONFLICT (id) DO NOTHING;
SELECT setval('users_id_seq',(SELECT MAX(id) FROM users));

INSERT INTO seller_profiles (id,user_id,nom_boutique,description,note)
VALUES (1,2,'TechShop Tunisie','Boutique de produits technologiques premium',4.5)
ON CONFLICT (id) DO NOTHING;
SELECT setval('seller_profiles_id_seq',(SELECT MAX(id) FROM seller_profiles));

INSERT INTO categories (id,nom,description,parent_id) VALUES
(1,'Électronique','Appareils et gadgets électroniques',NULL),
(2,'Vêtements','Mode homme, femme et enfant',NULL),
(3,'Maison','Articles de maison et décoration',NULL),
(4,'Informatique','Ordinateurs et périphériques',1),
(5,'Smartphones','Téléphones mobiles et accessoires',1),
(6,'Homme','Vêtements pour homme',2),
(7,'Femme','Vêtements pour femme',2),
(8,'Cuisine','Ustensiles et électroménager',3)
ON CONFLICT (id) DO NOTHING;
SELECT setval('categories_id_seq',(SELECT MAX(id) FROM categories));

INSERT INTO products (id,seller_id,nom,description,prix,prix_promo,stock,actif,images,date_creation) VALUES
(1,2,'Laptop HP ProBook 450','Ordinateur portable 15 pouces, Intel Core i7, 16GB RAM.',1299.99,999.99,15,true,'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=500',NOW()),
(2,2,'iPhone 14 Pro 256GB','Smartphone Apple, puce A16 Bionic, caméra 48MP.',1499.99,NULL,8,true,'https://images.unsplash.com/photo-1663499482523-1c0c1bae4ce1?w=500',NOW()),
(3,2,'Samsung Galaxy S23','Smartphone Android 128GB, Snapdragon 8 Gen 2.',899.99,749.99,20,true,'https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=500',NOW()),
(4,2,'Clavier Mécanique RGB','Clavier gaming mécanique, switches Cherry MX.',149.99,NULL,25,true,'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=500',NOW()),
(5,2,'T-Shirt Premium Homme','T-shirt 100% coton peigné, col rond, tailles S à XL.',29.99,NULL,50,true,'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=500',NOW()),
(6,2,'Robe Florale Femme','Robe légère en viscose, motif floral, tailles 36 à 46.',49.99,39.99,30,true,'https://images.unsplash.com/photo-1572804013427-4d7ca7268217?w=500',NOW()),
(7,2,'Casque Sony WH-1000XM5','Casque Bluetooth, réduction de bruit active, autonomie 30h.',349.99,279.99,12,true,'https://images.unsplash.com/photo-1546435770-a3e426bf472b?w=500',NOW()),
(8,2,'Robot Cuiseur Multifonction','Robot cuiseur 6L, 10 programmes automatiques.',199.99,NULL,18,true,'https://images.unsplash.com/photo-1585515320310-259814833e62?w=500',NOW())
ON CONFLICT (id) DO NOTHING;
SELECT setval('products_id_seq',(SELECT MAX(id) FROM products));

INSERT INTO product_categories (product_id,category_id) VALUES
(1,1),(1,4),(2,1),(2,5),(3,1),(3,5),(4,1),(4,4),
(5,2),(5,6),(6,2),(6,7),(7,1),(8,3),(8,8)
ON CONFLICT DO NOTHING;

INSERT INTO addresses (id,user_id,rue,ville,code_postal,pays,principal) VALUES
(1,3,'12 Rue de la Paix','Tunis','1000','Tunisie',true),
(2,3,'5 Avenue Habib Bourguiba','Sousse','4000','Tunisie',false)
ON CONFLICT (id) DO NOTHING;
SELECT setval('addresses_id_seq',(SELECT MAX(id) FROM addresses));

INSERT INTO coupons (id,code,type,valeur,date_expiration,usages_max,usages_actuels,actif) VALUES
(1,'PROMO10','PERCENT',10.0,'2025-12-31',100,0,true),
(2,'BIENVENUE20','FIXED',20.0,'2025-12-31',50,0,true),
(3,'FLASH50','PERCENT',50.0,'2025-06-30',10,0,true),
(4,'ETE2024','PERCENT',15.0,'2025-09-30',200,0,true)
ON CONFLICT (id) DO NOTHING;
SELECT setval('coupons_id_seq',(SELECT MAX(id) FROM coupons));