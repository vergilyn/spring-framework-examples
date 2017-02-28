
CREATE TABLE "CITY" (
"ID" NUMBER NOT NULL ,
"NAME" VARCHAR2(255 BYTE) NOT NULL ,
"STATE" VARCHAR2(255 BYTE) NOT NULL ,
"COUNTRY" VARCHAR2(255 BYTE) NOT NULL
)
LOGGING NOCOMPRESS NOCACHE;
-- Records of PARENT
INSERT INTO "CITY" VALUES ('1', 'city_name_1','city_state_1','city_country_1');
INSERT INTO "CITY" VALUES ('2', 'city_name_2','city_state_2','city_country_2');

-- Checks structure for table PARENT
ALTER TABLE "CITY" ADD CHECK ("ID" IS NOT NULL);


CREATE TABLE "HOTEL" (
"CITY" NUMBER NOT NULL ,
"NAME" VARCHAR2(255 BYTE) NOT NULL ,
"ADDRESS" VARCHAR2(255 BYTE) NOT NULL ,
"ZIP" VARCHAR2(255 BYTE) NOT NULL
)
LOGGING NOCOMPRESS NOCACHE;

-- ----------------------------
-- Records of CHILD
-- ----------------------------
INSERT INTO "HOTEL" VALUES ('1', 'hotel_name_1', 'hotel_address_1', 'hotel_zip_1');
INSERT INTO "HOTEL" VALUES ('2', 'hotel_name_2', 'hotel_address_2', 'hotel_zip_2');

-- Checks structure for table CHILD
ALTER TABLE "HOTEL" ADD CHECK ("CITY" IS NOT NULL);
