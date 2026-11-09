-- 36 real products; checked 2026-10-28. Photos are stored in frontend/public/images/products.
-- Prices are SAMPLE ZAR values; stock quantities are SAMPLE inventory.
-- Re-running updates images on matching products without resetting their price or stock.
USE radiant_skin_db;
START TRANSACTION;

-- Source: https://www.caretobeauty.com/za/cerave-facial-moisturising-lotion-fragrance-free-spf50-52ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'SKIN','CeraVe','CeraVe AM Facial Moisturizing Lotion Fragrance-Free SPF50 52ml','CeraVe AM Facial Moisturizing Lotion Fragrance-Free SPF50 52ml. Follow the directions on the product packaging.',279.99,30,52,'/images/products/catalogue-cerave-am-facial-moisturizing-lotion-fragrance-free-spf50-52ml.jpg',NULL,NULL,'Follow the directions on the product packaging.'
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='SKIN' AND brand='CeraVe' AND name='CeraVe AM Facial Moisturizing Lotion Fragrance-Free SPF50 52ml' AND volume_ml=52);
UPDATE product SET image_url='/images/products/catalogue-cerave-am-facial-moisturizing-lotion-fragrance-free-spf50-52ml.jpg' WHERE product_type='SKIN' AND brand='CeraVe' AND name='CeraVe AM Facial Moisturizing Lotion Fragrance-Free SPF50 52ml' AND volume_ml=52;

-- Source: https://www.caretobeauty.com/za/cerave-hydrating-foaming-oil-cleanser-236ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'SKIN','CeraVe','CeraVe Hydrating Foaming Oil Cleanser 236ml','CeraVe Hydrating Foaming Oil Cleanser 236ml. Follow the directions on the product packaging.',279.99,30,236,'/images/products/catalogue-cerave-hydrating-foaming-oil-cleanser-236ml.jpg',NULL,NULL,'Follow the directions on the product packaging.'
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='SKIN' AND brand='CeraVe' AND name='CeraVe Hydrating Foaming Oil Cleanser 236ml' AND volume_ml=236);
UPDATE product SET image_url='/images/products/catalogue-cerave-hydrating-foaming-oil-cleanser-236ml.jpg' WHERE product_type='SKIN' AND brand='CeraVe' AND name='CeraVe Hydrating Foaming Oil Cleanser 236ml' AND volume_ml=236;

-- Source: https://www.caretobeauty.com/za/cerave-skin-renewing-retinol-serum-30ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'SKIN','CeraVe','CeraVe Skin Renewing Retinol Serum 30ml','CeraVe Skin Renewing Retinol Serum 30ml. Follow the directions on the product packaging.',279.99,30,30,'/images/products/catalogue-cerave-skin-renewing-retinol-serum-30ml.jpg',NULL,NULL,'Follow the directions on the product packaging.'
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='SKIN' AND brand='CeraVe' AND name='CeraVe Skin Renewing Retinol Serum 30ml' AND volume_ml=30);
UPDATE product SET image_url='/images/products/catalogue-cerave-skin-renewing-retinol-serum-30ml.jpg' WHERE product_type='SKIN' AND brand='CeraVe' AND name='CeraVe Skin Renewing Retinol Serum 30ml' AND volume_ml=30;

-- Source: https://www.caretobeauty.com/za/cerave-invisible-hydrating-fluid-sunscreen-spf50-50ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'SKIN','CeraVe','CeraVe Invisible Hydrating Fluid Sunscreen SPF50 50ml','CeraVe Invisible Hydrating Fluid Sunscreen SPF50 50ml. Follow the directions on the product packaging.',279.99,30,50,'/images/products/catalogue-cerave-invisible-hydrating-fluid-sunscreen-spf50-50ml.jpg',NULL,NULL,'Follow the directions on the product packaging.'
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='SKIN' AND brand='CeraVe' AND name='CeraVe Invisible Hydrating Fluid Sunscreen SPF50 50ml' AND volume_ml=50);
UPDATE product SET image_url='/images/products/catalogue-cerave-invisible-hydrating-fluid-sunscreen-spf50-50ml.jpg' WHERE product_type='SKIN' AND brand='CeraVe' AND name='CeraVe Invisible Hydrating Fluid Sunscreen SPF50 50ml' AND volume_ml=50;

-- Source: https://www.caretobeauty.com/za/cerave-skin-renewing-eye-cream-14ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'SKIN','CeraVe','CeraVe Skin Renewing Eye Cream 14ml','CeraVe Skin Renewing Eye Cream 14ml. Follow the directions on the product packaging.',279.99,30,14,'/images/products/catalogue-cerave-skin-renewing-eye-cream-14ml.jpg',NULL,NULL,'Follow the directions on the product packaging.'
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='SKIN' AND brand='CeraVe' AND name='CeraVe Skin Renewing Eye Cream 14ml' AND volume_ml=14);
UPDATE product SET image_url='/images/products/catalogue-cerave-skin-renewing-eye-cream-14ml.jpg' WHERE product_type='SKIN' AND brand='CeraVe' AND name='CeraVe Skin Renewing Eye Cream 14ml' AND volume_ml=14;

-- Source: https://www.caretobeauty.com/za/cerave-invisible-dry-touch-fluid-sunscreen-spf50-50ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'SKIN','CeraVe','CeraVe Invisible Dry Touch Fluid Sunscreen SPF50+ 50ml','CeraVe Invisible Dry Touch Fluid Sunscreen SPF50+ 50ml. Follow the directions on the product packaging.',279.99,30,50,'/images/products/catalogue-cerave-invisible-dry-touch-fluid-sunscreen-spf50-50ml.jpg',NULL,NULL,'Follow the directions on the product packaging.'
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='SKIN' AND brand='CeraVe' AND name='CeraVe Invisible Dry Touch Fluid Sunscreen SPF50+ 50ml' AND volume_ml=50);
UPDATE product SET image_url='/images/products/catalogue-cerave-invisible-dry-touch-fluid-sunscreen-spf50-50ml.jpg' WHERE product_type='SKIN' AND brand='CeraVe' AND name='CeraVe Invisible Dry Touch Fluid Sunscreen SPF50+ 50ml' AND volume_ml=50;

-- Source: https://www.caretobeauty.com/za/cerave-skin-renewing-vitamin-c-serum-30ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'SKIN','CeraVe','CeraVe Skin Renewing Vitamin C Serum 30ml','CeraVe Skin Renewing Vitamin C Serum 30ml. Follow the directions on the product packaging.',279.99,30,30,'/images/products/catalogue-cerave-skin-renewing-vitamin-c-serum-30ml.jpg',NULL,NULL,'Follow the directions on the product packaging.'
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='SKIN' AND brand='CeraVe' AND name='CeraVe Skin Renewing Vitamin C Serum 30ml' AND volume_ml=30);
UPDATE product SET image_url='/images/products/catalogue-cerave-skin-renewing-vitamin-c-serum-30ml.jpg' WHERE product_type='SKIN' AND brand='CeraVe' AND name='CeraVe Skin Renewing Vitamin C Serum 30ml' AND volume_ml=30;

-- Source: https://www.caretobeauty.com/za/cerave-balancing-air-foam-cleanser-148ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'SKIN','CeraVe','CeraVe Balancing Air Foam Cleanser 148ml','CeraVe Balancing Air Foam Cleanser 148ml. Follow the directions on the product packaging.',279.99,30,148,'/images/products/catalogue-cerave-balancing-air-foam-cleanser-148ml.jpg',NULL,NULL,'Follow the directions on the product packaging.'
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='SKIN' AND brand='CeraVe' AND name='CeraVe Balancing Air Foam Cleanser 148ml' AND volume_ml=148);
UPDATE product SET image_url='/images/products/catalogue-cerave-balancing-air-foam-cleanser-148ml.jpg' WHERE product_type='SKIN' AND brand='CeraVe' AND name='CeraVe Balancing Air Foam Cleanser 148ml' AND volume_ml=148;

-- Source: https://www.caretobeauty.com/za/cerave-oil-control-moisturizing-gel-cream-52ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'SKIN','CeraVe','Cerave Oil Control Moisturizing Gel-Cream 52ml','Cerave Oil Control Moisturizing Gel-Cream 52ml. Follow the directions on the product packaging.',279.99,30,52,'/images/products/catalogue-cerave-oil-control-moisturizing-gel-cream-52ml.jpg',NULL,NULL,'Follow the directions on the product packaging.'
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='SKIN' AND brand='CeraVe' AND name='Cerave Oil Control Moisturizing Gel-Cream 52ml' AND volume_ml=52);
UPDATE product SET image_url='/images/products/catalogue-cerave-oil-control-moisturizing-gel-cream-52ml.jpg' WHERE product_type='SKIN' AND brand='CeraVe' AND name='Cerave Oil Control Moisturizing Gel-Cream 52ml' AND volume_ml=52;

-- Source: https://www.caretobeauty.com/za/cerave-hydrating-hyaluronic-acid-serum-30ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'SKIN','CeraVe','CeraVe Hydrating Hyaluronic Acid Serum 30ml','CeraVe Hydrating Hyaluronic Acid Serum 30ml. Follow the directions on the product packaging.',279.99,30,30,'/images/products/catalogue-cerave-hydrating-hyaluronic-acid-serum-30ml.jpg',NULL,NULL,'Follow the directions on the product packaging.'
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='SKIN' AND brand='CeraVe' AND name='CeraVe Hydrating Hyaluronic Acid Serum 30ml' AND volume_ml=30);
UPDATE product SET image_url='/images/products/catalogue-cerave-hydrating-hyaluronic-acid-serum-30ml.jpg' WHERE product_type='SKIN' AND brand='CeraVe' AND name='CeraVe Hydrating Hyaluronic Acid Serum 30ml' AND volume_ml=30;

-- Source: https://www.caretobeauty.com/za/cerave-facial-moisturising-lotion-spf30-52ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'SKIN','CeraVe','CeraVe AM Facial Moisturizing Lotion SPF30 52ml','CeraVe AM Facial Moisturizing Lotion SPF30 52ml. Follow the directions on the product packaging.',279.99,30,52,'/images/products/catalogue-cerave-am-facial-moisturizing-lotion-spf30-52ml.jpg',NULL,NULL,'Follow the directions on the product packaging.'
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='SKIN' AND brand='CeraVe' AND name='CeraVe AM Facial Moisturizing Lotion SPF30 52ml' AND volume_ml=52);
UPDATE product SET image_url='/images/products/catalogue-cerave-am-facial-moisturizing-lotion-spf30-52ml.jpg' WHERE product_type='SKIN' AND brand='CeraVe' AND name='CeraVe AM Facial Moisturizing Lotion SPF30 52ml' AND volume_ml=52;

-- Source: https://www.caretobeauty.com/za/cerave-resurfacing-retinol-serum-30ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'SKIN','CeraVe','CeraVe Resurfacing Retinol Serum 30ml','CeraVe Resurfacing Retinol Serum 30ml. Follow the directions on the product packaging.',279.99,30,30,'/images/products/catalogue-cerave-resurfacing-retinol-serum-30ml.jpg',NULL,NULL,'Follow the directions on the product packaging.'
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='SKIN' AND brand='CeraVe' AND name='CeraVe Resurfacing Retinol Serum 30ml' AND volume_ml=30);
UPDATE product SET image_url='/images/products/catalogue-cerave-resurfacing-retinol-serum-30ml.jpg' WHERE product_type='SKIN' AND brand='CeraVe' AND name='CeraVe Resurfacing Retinol Serum 30ml' AND volume_ml=30;

-- Source: https://www.caretobeauty.com/za/tresemme-keratin-smooth-heat-protect-spray-200ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'HAIR','TRESemme','TRESemmé Keratin Smooth Heat Protect Spray 200ml','TRESemmé Keratin Smooth Heat Protect Spray 200ml. Follow the directions on the product packaging.',149.99,30,200,'/images/products/catalogue-tresemm-keratin-smooth-heat-protect-spray-200ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Keratin Smooth Heat Protect Spray 200ml' AND volume_ml=200);
UPDATE product SET image_url='/images/products/catalogue-tresemm-keratin-smooth-heat-protect-spray-200ml.jpg' WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Keratin Smooth Heat Protect Spray 200ml' AND volume_ml=200;

-- Source: https://www.caretobeauty.com/za/tresemme-keratin-smooth-deep-smoothing-mask-frizz-prone-hair-440ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'HAIR','TRESemme','TRESemmé Keratin Smooth Deep Smoothing Mask Frizz-Prone Hair 440ml','TRESemmé Keratin Smooth Deep Smoothing Mask Frizz-Prone Hair 440ml. Follow the directions on the product packaging.',149.99,30,440,'/images/products/catalogue-tresemm-keratin-smooth-deep-smoothing-mask-frizz-prone-hair-440ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Keratin Smooth Deep Smoothing Mask Frizz-Prone Hair 440ml' AND volume_ml=440);
UPDATE product SET image_url='/images/products/catalogue-tresemm-keratin-smooth-deep-smoothing-mask-frizz-prone-hair-440ml.jpg' WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Keratin Smooth Deep Smoothing Mask Frizz-Prone Hair 440ml' AND volume_ml=440;

-- Source: https://www.caretobeauty.com/za/tresemme-heat-defence-care-protect-spray-270ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'HAIR','TRESemme','TRESemmé Heat Defence Care & Protect Spray 270ml','TRESemmé Heat Defence Care & Protect Spray 270ml. Follow the directions on the product packaging.',149.99,30,270,'/images/products/catalogue-tresemm-heat-defence-care-protect-spray-270ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Heat Defence Care & Protect Spray 270ml' AND volume_ml=270);
UPDATE product SET image_url='/images/products/catalogue-tresemm-heat-defence-care-protect-spray-270ml.jpg' WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Heat Defence Care & Protect Spray 270ml' AND volume_ml=270;

-- Source: https://www.caretobeauty.com/za/tresemme-lamellar-shine-leave-in-cream-serum-200ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'HAIR','TRESemme','TRESemmé Lamellar Shine Leave-In Cream Serum 200ml','TRESemmé Lamellar Shine Leave-In Cream Serum 200ml. Follow the directions on the product packaging.',149.99,30,200,'/images/products/catalogue-tresemm-lamellar-shine-leave-in-cream-serum-200ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Lamellar Shine Leave-In Cream Serum 200ml' AND volume_ml=200);
UPDATE product SET image_url='/images/products/catalogue-tresemm-lamellar-shine-leave-in-cream-serum-200ml.jpg' WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Lamellar Shine Leave-In Cream Serum 200ml' AND volume_ml=200;

-- Source: https://www.caretobeauty.com/za/tresemme-flawless-curls-leave-in-cream-serum-200ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'HAIR','TRESemme','TRESemmé Flawless Curls Leave-In Cream Serum 200ml','TRESemmé Flawless Curls Leave-In Cream Serum 200ml. Follow the directions on the product packaging.',149.99,30,200,'/images/products/catalogue-tresemm-flawless-curls-leave-in-cream-serum-200ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Flawless Curls Leave-In Cream Serum 200ml' AND volume_ml=200);
UPDATE product SET image_url='/images/products/catalogue-tresemm-flawless-curls-leave-in-cream-serum-200ml.jpg' WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Flawless Curls Leave-In Cream Serum 200ml' AND volume_ml=200;

-- Source: https://www.caretobeauty.com/za/tresemme-biotin-repair-leave-in-cream-serum-200ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'HAIR','TRESemme','TRESemmé Biotin Repair Leave-In Cream Serum 200ml','TRESemmé Biotin Repair Leave-In Cream Serum 200ml. Follow the directions on the product packaging.',149.99,30,200,'/images/products/catalogue-tresemm-biotin-repair-leave-in-cream-serum-200ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Biotin Repair Leave-In Cream Serum 200ml' AND volume_ml=200);
UPDATE product SET image_url='/images/products/catalogue-tresemm-biotin-repair-leave-in-cream-serum-200ml.jpg' WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Biotin Repair Leave-In Cream Serum 200ml' AND volume_ml=200;

-- Source: https://www.caretobeauty.com/za/tresemme-keratin-smooth-finishing-oil-75ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'HAIR','TRESemme','TRESemmé Keratin Smooth Finishing Oil 75ml','TRESemmé Keratin Smooth Finishing Oil 75ml. Follow the directions on the product packaging.',149.99,30,75,'/images/products/catalogue-tresemm-keratin-smooth-finishing-oil-75ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Keratin Smooth Finishing Oil 75ml' AND volume_ml=75);
UPDATE product SET image_url='/images/products/catalogue-tresemm-keratin-smooth-finishing-oil-75ml.jpg' WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Keratin Smooth Finishing Oil 75ml' AND volume_ml=75;

-- Source: https://www.caretobeauty.com/za/tresemme-lamellar-shine-ultra-gloss-spray-200ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'HAIR','TRESemme','TRESemmé Lamellar Shine Ultra Gloss Spray 200ml','TRESemmé Lamellar Shine Ultra Gloss Spray 200ml. Follow the directions on the product packaging.',149.99,30,200,'/images/products/catalogue-tresemm-lamellar-shine-ultra-gloss-spray-200ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Lamellar Shine Ultra Gloss Spray 200ml' AND volume_ml=200);
UPDATE product SET image_url='/images/products/catalogue-tresemm-lamellar-shine-ultra-gloss-spray-200ml.jpg' WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Lamellar Shine Ultra Gloss Spray 200ml' AND volume_ml=200;

-- Source: https://www.caretobeauty.com/za/tresemme-biotin-repair-instant-recovery-mask-damaged-hair-440ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'HAIR','TRESemme','TRESemmé Biotin Repair Instant Recovery Mask Damaged Hair 440ml','TRESemmé Biotin Repair Instant Recovery Mask Damaged Hair 440ml. Follow the directions on the product packaging.',149.99,30,440,'/images/products/catalogue-tresemm-biotin-repair-instant-recovery-mask-damaged-hair-440ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Biotin Repair Instant Recovery Mask Damaged Hair 440ml' AND volume_ml=440);
UPDATE product SET image_url='/images/products/catalogue-tresemm-biotin-repair-instant-recovery-mask-damaged-hair-440ml.jpg' WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Biotin Repair Instant Recovery Mask Damaged Hair 440ml' AND volume_ml=440;

-- Source: https://www.caretobeauty.com/za/tresemme-rich-moisture-moisturizing-mask-dry-hair-440ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'HAIR','TRESemme','TRESemmé Rich Moisture Moisturizing Mask Dry Hair 440ml','TRESemmé Rich Moisture Moisturizing Mask Dry Hair 440ml. Follow the directions on the product packaging.',149.99,30,440,'/images/products/catalogue-tresemm-rich-moisture-moisturizing-mask-dry-hair-440ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Rich Moisture Moisturizing Mask Dry Hair 440ml' AND volume_ml=440);
UPDATE product SET image_url='/images/products/catalogue-tresemm-rich-moisture-moisturizing-mask-dry-hair-440ml.jpg' WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Rich Moisture Moisturizing Mask Dry Hair 440ml' AND volume_ml=440;

-- Source: https://www.caretobeauty.com/za/tresemme-revitalise-color-conditioner-685ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'HAIR','TRESemme','TRESemmé Revitalise Color Conditioner 685ml','TRESemmé Revitalise Color Conditioner 685ml. Follow the directions on the product packaging.',149.99,30,685,'/images/products/catalogue-tresemm-revitalise-color-conditioner-685ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Revitalise Color Conditioner 685ml' AND volume_ml=685);
UPDATE product SET image_url='/images/products/catalogue-tresemm-revitalise-color-conditioner-685ml.jpg' WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Revitalise Color Conditioner 685ml' AND volume_ml=685;

-- Source: https://www.caretobeauty.com/za/tresemme-replenish-cleanse-shampoo-900ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'HAIR','TRESemme','TRESemmé Replenish & Cleanse Shampoo 900ml','TRESemmé Replenish & Cleanse Shampoo 900ml. Follow the directions on the product packaging.',149.99,30,900,'/images/products/catalogue-tresemm-replenish-cleanse-shampoo-900ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Replenish & Cleanse Shampoo 900ml' AND volume_ml=900);
UPDATE product SET image_url='/images/products/catalogue-tresemm-replenish-cleanse-shampoo-900ml.jpg' WHERE product_type='HAIR' AND brand='TRESemme' AND name='TRESemmé Replenish & Cleanse Shampoo 900ml' AND volume_ml=900;

-- Source: https://www.caretobeauty.com/za/nivea-q10-firming-bronze-body-lotion-250ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'BODY','NIVEA','Nivea Q10 Firming + Bronze Body Lotion 250ml','Nivea Q10 Firming + Bronze Body Lotion 250ml. Follow the directions on the product packaging.',119.99,30,250,'/images/products/catalogue-nivea-q10-firming-bronze-body-lotion-250ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='BODY' AND brand='NIVEA' AND name='Nivea Q10 Firming + Bronze Body Lotion 250ml' AND volume_ml=250);
UPDATE product SET image_url='/images/products/catalogue-nivea-q10-firming-bronze-body-lotion-250ml.jpg' WHERE product_type='BODY' AND brand='NIVEA' AND name='Nivea Q10 Firming + Bronze Body Lotion 250ml' AND volume_ml=250;

-- Source: https://www.caretobeauty.com/za/nivea-q10-firming-energizing-body-serum-lotion-200ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'BODY','NIVEA','Nivea Q10 Firming + Energizing Body Serum-Lotion 200ml','Nivea Q10 Firming + Energizing Body Serum-Lotion 200ml. Follow the directions on the product packaging.',119.99,30,200,'/images/products/catalogue-nivea-q10-firming-energizing-body-serum-lotion-200ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='BODY' AND brand='NIVEA' AND name='Nivea Q10 Firming + Energizing Body Serum-Lotion 200ml' AND volume_ml=200);
UPDATE product SET image_url='/images/products/catalogue-nivea-q10-firming-energizing-body-serum-lotion-200ml.jpg' WHERE product_type='BODY' AND brand='NIVEA' AND name='Nivea Q10 Firming + Energizing Body Serum-Lotion 200ml' AND volume_ml=200;

-- Source: https://www.caretobeauty.com/za/nivea-protective-care-hand-cream-75ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'BODY','NIVEA','Nivea Protective Care Hand Cream 75ml','Nivea Protective Care Hand Cream 75ml. Follow the directions on the product packaging.',119.99,30,75,'/images/products/catalogue-nivea-protective-care-hand-cream-75ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='BODY' AND brand='NIVEA' AND name='Nivea Protective Care Hand Cream 75ml' AND volume_ml=75);
UPDATE product SET image_url='/images/products/catalogue-nivea-protective-care-hand-cream-75ml.jpg' WHERE product_type='BODY' AND brand='NIVEA' AND name='Nivea Protective Care Hand Cream 75ml' AND volume_ml=75;

-- Source: https://www.caretobeauty.com/za/nivea-dark-chocolate-cookie-shower-gel-250ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'BODY','NIVEA','Nivea Dark Chocolate Cookie Shower Gel 250ml','Nivea Dark Chocolate Cookie Shower Gel 250ml. Follow the directions on the product packaging.',119.99,30,250,'/images/products/catalogue-nivea-dark-chocolate-cookie-shower-gel-250ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='BODY' AND brand='NIVEA' AND name='Nivea Dark Chocolate Cookie Shower Gel 250ml' AND volume_ml=250);
UPDATE product SET image_url='/images/products/catalogue-nivea-dark-chocolate-cookie-shower-gel-250ml.jpg' WHERE product_type='BODY' AND brand='NIVEA' AND name='Nivea Dark Chocolate Cookie Shower Gel 250ml' AND volume_ml=250;

-- Source: https://www.caretobeauty.com/za/nivea-caramel-fudge-shower-gel-250ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'BODY','NIVEA','Nivea Caramel Fudge Shower Gel 250ml','Nivea Caramel Fudge Shower Gel 250ml. Follow the directions on the product packaging.',119.99,30,250,'/images/products/catalogue-nivea-caramel-fudge-shower-gel-250ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='BODY' AND brand='NIVEA' AND name='Nivea Caramel Fudge Shower Gel 250ml' AND volume_ml=250);
UPDATE product SET image_url='/images/products/catalogue-nivea-caramel-fudge-shower-gel-250ml.jpg' WHERE product_type='BODY' AND brand='NIVEA' AND name='Nivea Caramel Fudge Shower Gel 250ml' AND volume_ml=250;

-- Source: https://www.caretobeauty.com/za/nivea-body-serum-lotion-200ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'BODY','NIVEA','Nivea Body Serum Lotion 200ml','Nivea Body Serum Lotion 200ml. Follow the directions on the product packaging.',119.99,30,200,'/images/products/catalogue-nivea-body-serum-lotion-200ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='BODY' AND brand='NIVEA' AND name='Nivea Body Serum Lotion 200ml' AND volume_ml=200);
UPDATE product SET image_url='/images/products/catalogue-nivea-body-serum-lotion-200ml.jpg' WHERE product_type='BODY' AND brand='NIVEA' AND name='Nivea Body Serum Lotion 200ml' AND volume_ml=200;

-- Source: https://www.caretobeauty.com/za/nivea-nourishing-body-cream-400ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'BODY','NIVEA','Nivea Nourishing Body Cream 400ml','Nivea Nourishing Body Cream 400ml. Follow the directions on the product packaging.',119.99,30,400,'/images/products/catalogue-nivea-nourishing-body-cream-400ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='BODY' AND brand='NIVEA' AND name='Nivea Nourishing Body Cream 400ml' AND volume_ml=400);
UPDATE product SET image_url='/images/products/catalogue-nivea-nourishing-body-cream-400ml.jpg' WHERE product_type='BODY' AND brand='NIVEA' AND name='Nivea Nourishing Body Cream 400ml' AND volume_ml=400;

-- Source: https://www.caretobeauty.com/za/nivea-men-sensitive-soothing-shower-gel-500ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'BODY','NIVEA','Nivea Men Sensitive Soothing Shower Gel 500ml','Nivea Men Sensitive Soothing Shower Gel 500ml. Follow the directions on the product packaging.',119.99,30,500,'/images/products/catalogue-nivea-men-sensitive-soothing-shower-gel-500ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='BODY' AND brand='NIVEA' AND name='Nivea Men Sensitive Soothing Shower Gel 500ml' AND volume_ml=500);
UPDATE product SET image_url='/images/products/catalogue-nivea-men-sensitive-soothing-shower-gel-500ml.jpg' WHERE product_type='BODY' AND brand='NIVEA' AND name='Nivea Men Sensitive Soothing Shower Gel 500ml' AND volume_ml=500;

-- Source: https://www.caretobeauty.com/za/nivea-men-cool-kick-shower-gel-500ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'BODY','NIVEA','Nivea Men Cool Kick Shower Gel 500ml','Nivea Men Cool Kick Shower Gel 500ml. Follow the directions on the product packaging.',119.99,30,500,'/images/products/catalogue-nivea-men-cool-kick-shower-gel-500ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='BODY' AND brand='NIVEA' AND name='Nivea Men Cool Kick Shower Gel 500ml' AND volume_ml=500);
UPDATE product SET image_url='/images/products/catalogue-nivea-men-cool-kick-shower-gel-500ml.jpg' WHERE product_type='BODY' AND brand='NIVEA' AND name='Nivea Men Cool Kick Shower Gel 500ml' AND volume_ml=500;

-- Source: https://www.caretobeauty.com/za/nivea-repair-care-urea-advanced-repair-body-lotion-400ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'BODY','NIVEA','Nivea Repair & Care Urea Advanced Repair Body Lotion 400ml','Nivea Repair & Care Urea Advanced Repair Body Lotion 400ml. Follow the directions on the product packaging.',119.99,30,400,'/images/products/catalogue-nivea-repair-care-urea-advanced-repair-body-lotion-400ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='BODY' AND brand='NIVEA' AND name='Nivea Repair & Care Urea Advanced Repair Body Lotion 400ml' AND volume_ml=400);
UPDATE product SET image_url='/images/products/catalogue-nivea-repair-care-urea-advanced-repair-body-lotion-400ml.jpg' WHERE product_type='BODY' AND brand='NIVEA' AND name='Nivea Repair & Care Urea Advanced Repair Body Lotion 400ml' AND volume_ml=400;

-- Source: https://www.caretobeauty.com/za/cerave-intensive-moisturizing-lotion-473ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'BODY','CeraVe','CeraVe Intensive Moisturizing Lotion 473ml','CeraVe Intensive Moisturizing Lotion 473ml. Follow the directions on the product packaging.',119.99,30,473,'/images/products/catalogue-cerave-intensive-moisturizing-lotion-473ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='BODY' AND brand='CeraVe' AND name='CeraVe Intensive Moisturizing Lotion 473ml' AND volume_ml=473);
UPDATE product SET image_url='/images/products/catalogue-cerave-intensive-moisturizing-lotion-473ml.jpg' WHERE product_type='BODY' AND brand='CeraVe' AND name='CeraVe Intensive Moisturizing Lotion 473ml' AND volume_ml=473;

-- Source: https://www.caretobeauty.com/za/cerave-reparative-hand-cream-50ml/
INSERT INTO product (product_type,brand,name,description,price,stock_quantity,volume_ml,image_url,skin_concern,hair_concern,usage_instructions)
SELECT 'BODY','CeraVe','CeraVe Reparative Hand Cream Extremely Dry and Rough Hands 50ml','CeraVe Reparative Hand Cream Extremely Dry and Rough Hands 50ml. Follow the directions on the product packaging.',119.99,30,50,'/images/products/catalogue-cerave-reparative-hand-cream-extremely-dry-and-rough-hands-50ml.jpg',NULL,NULL,NULL
WHERE NOT EXISTS (SELECT 1 FROM product WHERE product_type='BODY' AND brand='CeraVe' AND name='CeraVe Reparative Hand Cream Extremely Dry and Rough Hands 50ml' AND volume_ml=50);
UPDATE product SET image_url='/images/products/catalogue-cerave-reparative-hand-cream-extremely-dry-and-rough-hands-50ml.jpg' WHERE product_type='BODY' AND brand='CeraVe' AND name='CeraVe Reparative Hand Cream Extremely Dry and Rough Hands 50ml' AND volume_ml=50;

COMMIT;
SELECT product_type,COUNT(*) AS products FROM product GROUP BY product_type;
