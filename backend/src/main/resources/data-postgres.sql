insert into role (name) values ('ROLE_SYSTEM_ADMIN');
insert into role (name) values ('ROLE_PHARMACY_ADMIN');
insert into role (name) values ('ROLE_SUPPLIER');
insert into role (name) values ('ROLE_PHARMACIST');
insert into role (name) values ('ROLE_DERMATOLOGIST');
insert into role (name) values ('ROLE_PATIENT');

-- NOVI TESTNI PODACI
insert into address (city, street_name, number, postal_code, latitude, longitude, active)
values ('Novi Sad', 'Bulevar Oslobodjenja', '100', '21102', 19.840239410440507, 45.24526342119054, true);


insert into point (dermatologist_examination, pharmacist_counseling, active)
values (10, 20, true);

-- insert into patient_category (regular_patient_limit, silver_patient_limit, gold_patient_limit, active)
-- values (30, 50, 80, true);

insert into pharmacy (name, address_id, start_work_time, end_work_time, rating, point_id, counseling_price, active) values ('Benu', 1, 480, 1200, 0.0, 1, 100, true);
insert into pharmacy (name, address_id, start_work_time, end_work_time, rating, point_id, counseling_price, active) values ('Lilly', 1, 480, 1200, 0.0, 1, 200, true);
insert into pharmacy (name, address_id, start_work_time, end_work_time, rating, point_id, counseling_price, active) values ('Galen Pharm', 1, 480, 1200, 0.0, 1, 500, true);
insert into pharmacy (name, address_id, start_work_time, end_work_time, rating, point_id, counseling_price, active) values ('Dr. Max', 1, 480, 1200, 0.0, 1, 500, true);

insert into medicine (name, type, medicine_form, structure, manufacture, medicine_issuing_type, daily_intake, contraindications, rating, points, active)
values ('Bromazepam', 'sedativ', 'PILL', 'sastav', 'Galenika', 'WITH_PRESCRIPTION', 1.0, 'kontraindikaicije', 0.0, 1.0, true);
insert into medicine (name, type, medicine_form, structure, manufacture, medicine_issuing_type, daily_intake, contraindications, rating, points, active)
values ('Brufen', 'analgetik', 'PILL', 'sastav', 'Galenika', 'WITHOUT_PRESCRIPTION', 1.0, 'kontraindikaicije', 0.0, 1.0, true );
insert into medicine (name, type, medicine_form, structure, manufacture, medicine_issuing_type, daily_intake, contraindications, rating, points, active)
values ('Analgin', 'sedativ', 'PILL', 'sastav', 'Microlenika', 'WITH_PRESCRIPTION', 1.0, 'kontraindikaicije', 0.0, 2.0, true );
insert into medicine (name, type, medicine_form, structure, manufacture, medicine_issuing_type, daily_intake, contraindications, rating, points, active)
values ('Medical Marihuana', 'sedativ', 'PILL', 'sastav', 'Amtserdamac', 'WITH_PRESCRIPTION', 1.0, 'kontraindikaicije', 0.0, 2.0, true);
insert into medicine (name, type, medicine_form, structure, manufacture, medicine_issuing_type, daily_intake, contraindications, rating, points, active)
values ('Andol', 'analgetik', 'PILL', 'sastav', 'Galenika', 'WITHOUT_PRESCRIPTION', 1.0, 'kontraindikaicije', 0.0, 1.0, true );
insert into medicine (name, type, medicine_form, structure, manufacture, medicine_issuing_type, daily_intake, contraindications, rating, points, active)
values ('Xanax', 'sedativ', 'PILL', 'sastav', 'Galenika', 'WITH_PRESCRIPTION', 1.0, 'kontraindikaicije', 0.0, 2.0, true);
insert into medicine (name, type, medicine_form, structure, manufacture, medicine_issuing_type, daily_intake, contraindications, rating, points, active)
values ('Amphetamine', 'analgetik', 'PILL', 'sastav', 'Galenika', 'WITHOUT_PRESCRIPTION', 1.0, 'kontraindikaicije', 0.0, 1.0, true);
insert into medicine (name, type, medicine_form, structure, manufacture, medicine_issuing_type, daily_intake, contraindications, rating, points, active)
values ('LSD', 'sedativ', 'PILL', 'sastav', 'Galenika', 'WITHOUT_PRESCRIPTION', 1.0, 'kontraindikaicije', 0.0, 2.0, true);
insert into medicine (name, type, medicine_form, structure, manufacture, medicine_issuing_type, daily_intake, contraindications, rating, points, active)
values ('Cocaine', 'sedativ', 'POWDER', 'sastav', 'Galenika', 'WITHOUT_PRESCRIPTION', 1.0, 'kontraindikaicije', 0.0, 1.0, true);
insert into medicine (name, type, medicine_form, structure, manufacture, medicine_issuing_type, daily_intake, contraindications, rating, points, active)
values ('Metaloid', 'sedativ', 'PILL', 'sastav', 'Galenika', 'WITHOUT_PRESCRIPTION', 1.0, 'kontraindikaicije', 0.0, 2.0, true);

insert into medicine_replacement_medicines (medicine_id, replacement_medicines_id) values (2, 6);

insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (300, 500, 1, 1, 0.0, true);
insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (200, 500, 2, 1, 0.0, true);
insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (500, 1000, 1, 2, 0.0, true);
insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (400, 500, 2, 2, 0.0, true);
insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (400, 500, 6, 2, 0.0, true);
insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (600, 1500, 1, 3, 0.0, true);
insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (400, 5, 6, 1, 0.0, true);
insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (300, 500, 3, 1, 0.0, true);
insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (400, 500, 4, 1, 0.0, true);
insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (500, 500, 5, 1, 0.0, true);
insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (600, 500, 6, 3, 0.0, true);
insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (700, 500, 7, 3, 0.0, true);
insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (800, 500, 8, 3, 0.0, true);
insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (900, 500, 9, 1, 0.0, true);
insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (1000, 500, 10, 1, 0.0, true);
insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (200, 500, 3, 4, 0.0, true);
insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (400, 500, 4, 4, 0.0, true);
insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (300, 500, 5, 4, 0.0, true);
insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (400, 500, 6, 4, 0.0, true);
insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (200, 500, 7, 2, 0.0, true);
insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (500, 500, 8, 2, 0.0, true);
insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (600, 500, 9, 2, 0.0, true);
insert into pharmacy_medicine (price, available_amount, medicine_id, pharmacy_id, rating, active) values (800, 500, 10, 2, 0.0, true);


insert into patient_category (name, minimum_points, discount, color, active) values ('SILVER', 50, 2.0, 'silver', true);
insert into patient_category (name, minimum_points, discount, color, active) values ('GOLD', 80, 5.0, 'gold', true);

insert into patient (id, first_name, last_name, email, password, phone_number, role_id, address_id, active, points, patient_category_id)
values (31, 'Marija', 'Maric', 'isa.confirmtoken+pacijent1@gmail.com', '$2y$10$/UqZTZ8quDOhE3dAL3mBg.pV09JAI7pQ8IQ.cCozGA.mWXDaq6zRa', '0655890200', 6, 1, true, 60.0, 1); /* marija123 */

insert into patient (id, first_name, last_name, email, password, phone_number, role_id, address_id, active, points, patient_category_id)
values (32, 'Pera', 'Peric', 'isa.confirmtoken+pacijent2@gmail.com', '$2y$10$gjKBBEOtR7Xsaay1U1af8.AW0a9eDLDDSrHUAiAbf55jYWalDoyEi', '0655890200', 6, 1, true, 60.0, 1);    /* pera123 */
-- alergican na Bromazepam i Brufen

insert into penalty (date, description, patient_id, active) values (1621085639000, 'Medicine reservation canceled 3 times', 31, true);
insert into penalty (date, description, patient_id, active) values (1621155679000, 'Counseling appointment canceled 3 times', 31, true);

insert into patient (id, first_name, last_name, email, password, phone_number, role_id, address_id, active, points, patient_category_id)
values (33, 'Darko', 'Peric', 'isa.confirmtoken+pacijent3@gmail.com', '$2y$10$kY.s3AEXJXGAiaYtdsn/CubM.b6sqP0rGxDKcBR2G9STVdMXCmHSm', '0655890200', 6, 1, true, 60.0, 1);  /* darko123 */
insert into patient (id, first_name, last_name, email, password, phone_number, role_id, address_id, active, points, patient_category_id)
values (34, 'Pacijent', 'Pet', 'isa.confirmtoken+pacijent4@gmail.com', '$2y$10$gjKBBEOtR7Xsaay1U1af8.AW0a9eDLDDSrHUAiAbf55jYWalDoyEi', '0655890200', 6, 1, true, 60.0, 1);
insert into patient (id, first_name, last_name, email, password, phone_number, role_id, address_id, active, points, patient_category_id)
values (35, 'Pacijent', 'Sest', 'isa.confirmtoken+pacijent5@gmail.com', '$2y$10$gjKBBEOtR7Xsaay1U1af8.AW0a9eDLDDSrHUAiAbf55jYWalDoyEi', '0655890200', 6, 1, true, 60.0, 1);
insert into patient (id, first_name, last_name, email, password, phone_number, role_id, address_id, active, points, patient_category_id)
values (36, 'Pacijent', 'Sedam', 'isa.confirmtoken+pacijent6@gmail.com', '$2y$10$gjKBBEOtR7Xsaay1U1af8.AW0a9eDLDDSrHUAiAbf55jYWalDoyEi', '0655890200', 6, 1, true, 60.0, 1);
insert into patient (id, first_name, last_name, email, password, phone_number, role_id, address_id, active, points, patient_category_id)
values (37, 'Pacijent', 'Osam', 'isa.confirmtoken+pacijent7@gmail.com', '$2y$10$gjKBBEOtR7Xsaay1U1af8.AW0a9eDLDDSrHUAiAbf55jYWalDoyEi', '0655890200', 6, 1, true, 60.0, 1);
insert into patient (id, first_name, last_name, email, password, phone_number, role_id, address_id, active, points, patient_category_id)
values (38, 'Marko', 'Markovic', 'isa.confirmtoken+pacijent8@gmail.com', '$2y$10$4zq915hpKemqhSHzR/QrjuZwrsQokcV/Z7iwf9VM3xp4J5y9Vc50O', '0655890200', 6, 1, true, 60.0, 1);

insert into pharmacy_medicine_price (price, start_date, pharmacy_medicine_id, active) values(300, 1609527600000, 1, true);
insert into pharmacy_medicine_price (price, start_date, pharmacy_medicine_id, active) values(200, 1609527600000, 2, true);
insert into pharmacy_medicine_price (price, start_date, pharmacy_medicine_id, active) values(500, 1609527600000, 3, true);
insert into pharmacy_medicine_price (price, start_date, pharmacy_medicine_id, active) values(400, 1609527600000, 4, true);
insert into pharmacy_medicine_price (price, start_date, pharmacy_medicine_id, active) values(600, 1609527600000, 5, true);
insert into pharmacy_medicine_price (price, start_date, pharmacy_medicine_id, active) values(300, 1609527600000, 6, true);

insert into patient_allergic_medicine (patient_id, medicine_id) values (32, 2);
insert into patient_allergic_medicine (patient_id, medicine_id) values (31, 2);

insert into e_prescription (patient_id, date_of_issue, status, active) values (32, 1618225109675, 'NEW', true);
insert into e_prescription (patient_id, date_of_issue, status, active) values (32, 1618225109675, 'NEW', true);
insert into e_prescription (patient_id, date_of_issue, status, active) values (32, 1618225109675, 'NEW', true);
insert into e_prescription (patient_id, date_of_issue, status, active) values (32, 1618225109675, 'NEW', true);
insert into e_prescription (patient_id, date_of_issue, status, active) values (32, 1618225109675, 'NEW', true);
insert into e_prescription (patient_id, date_of_issue, status, active) values (31, 1618225109675, 'NEW', true);
insert into e_prescription (patient_id, date_of_issue, status, active) values (31, 1618225109675, 'NEW', true);
insert into e_prescription (patient_id, date_of_issue, status, active) values (31, 1618225109675, 'NEW', true);
insert into e_prescription (patient_id, date_of_issue, status, active) values (31, 1618225109675, 'NEW', true);
insert into e_prescription (patient_id, date_of_issue, status, active) values (31, 1618225109675, 'NEW', true);
insert into e_prescription (patient_id, date_of_issue, status, active) values (32, 1618477200000, 'REJECTED', true);
insert into e_prescription (patient_id, date_of_issue, status, active) values (32, 1618477200000, 'PROCESSED', true);

insert into eprescription_medicine (medicine_id, therapy_duration, amount, eprescription_id, active) values (1, 20, 5, 1, true);
insert into eprescription_medicine (medicine_id, therapy_duration, amount, eprescription_id, active) values (2, 10, 1, 1, true);

insert into eprescription_medicine (medicine_id, therapy_duration, amount, eprescription_id, active) values (3, 10, 2, 2, true);
insert into eprescription_medicine (medicine_id, therapy_duration, amount, eprescription_id, active) values (4, 7, 1, 2, true);

insert into eprescription_medicine (medicine_id, therapy_duration, amount, eprescription_id, active) values (1, 8, 3, 3, true);
insert into eprescription_medicine (medicine_id, therapy_duration, amount, eprescription_id, active) values (5, 10, 4, 3, true);
insert into eprescription_medicine (medicine_id, therapy_duration, amount, eprescription_id, active) values (6, 4, 2, 3, true);
insert into eprescription_medicine (medicine_id, therapy_duration, amount, eprescription_id, active) values (7, 4, 1, 3, true);

insert into eprescription_medicine (medicine_id, therapy_duration, amount, eprescription_id, active) values (8, 2, 1, 4, true);

insert into eprescription_medicine (medicine_id, therapy_duration, amount, eprescription_id, active) values (9, 10, 2, 5, true);

insert into eprescription_medicine (medicine_id, therapy_duration, amount, eprescription_id, active) values (1, 10, 1, 6, true);
insert into eprescription_medicine (medicine_id, therapy_duration, amount, eprescription_id, active) values (2, 10, 2, 6, true);

insert into eprescription_medicine (medicine_id, therapy_duration, amount, eprescription_id, active) values (1, 8, 2, 7, true);
insert into eprescription_medicine (medicine_id, therapy_duration, amount, eprescription_id, active) values (4, 8, 3, 7, true);

insert into eprescription_medicine (medicine_id, therapy_duration, amount, eprescription_id, active) values (5, 8, 5, 8, true);

insert into eprescription_medicine (medicine_id, therapy_duration, amount, eprescription_id, active) values (7, 7, 4, 9, true);
insert into eprescription_medicine (medicine_id, therapy_duration, amount, eprescription_id, active) values (8, 7, 1, 9, true);

insert into eprescription_medicine (medicine_id, therapy_duration, amount, eprescription_id, active) values (1, 5, 3, 10, true);


insert into pharmacy_admin(id, active, email, first_name, last_name, password, phone_number, role_id, address_id, pharmacy_id)
values (100, true, 'isa.confirmtoken+adminApoteke1@gmail.com', 'Mika', 'Mikic', '$2y$10$ps6I7o2fG72aj9PdCUSEiO4jl3P6cOVt3h8yVnnUvcRsdPIq2oH2.', '0641246256', 2, 1, 1);  /* mikamikic */

insert into pharmacy_admin(id, active, email, first_name, last_name, password, phone_number, role_id, address_id, pharmacy_id)
values (101, true, 'isa.confirmtoken+adminApoteke2@gmail.com', 'Marko', 'Mikic', '$2y$10$ps6I7o2fG72aj9PdCUSEiO4jl3P6cOVt3h8yVnnUvcRsdPIq2oH2.', '0641246256', 2, 1, 2);  /* mikamikic */

insert into pharmacy_admin(id, active, email, first_name, last_name, password, phone_number, role_id, address_id, pharmacy_id)
values (102, true, 'isa.confirmtoken+adminApoteke3@gmail.com', 'Luka', 'Mikic', '$2y$10$ps6I7o2fG72aj9PdCUSEiO4jl3P6cOVt3h8yVnnUvcRsdPIq2oH2.', '0641246256', 2, 1, 3);  /* mikamikic */

insert into pharmacy_admin(id, active, email, first_name, last_name, password, phone_number, role_id, address_id, pharmacy_id)
values (103, true, 'isa.confirmtoken+adminApoteke4@gmail.com', 'Mirko', 'Mikic', '$2y$10$ps6I7o2fG72aj9PdCUSEiO4jl3P6cOVt3h8yVnnUvcRsdPIq2oH2.', '0641246256', 2, 1, 4);  /* mikamikic */


insert into dermatologist (id, first_name, last_name, email, password, phone_number, role_id, address_id, active, has_active_appointment)
values (10, 'Petar', 'Petrovic', 'isa.confirmtoken+dermatolog1@gmail.com', '$2y$10$iknlps0XBJmQ8lEoVvRs5.hBmVf8/Iof3F9GJm95T4owCrWeNix12', '0000000001', 5, 1, true, false); /*  petar123 */
insert into dermatologist (id, first_name, last_name, email, password, phone_number, role_id, address_id, active, has_active_appointment)
values (11, 'Marko', 'Markovic', 'isa.confirmtoken+dermatolog2@gmail.com', '$2y$10$fDF/j96mZnDUR1QIuHf5wOKw7iyOO6UXNLXRPl1..OA6BpsPOnhW2', '0000000002', 5, 1, true, false); /* marko123 */
insert into dermatologist (id, first_name, last_name, email, password, phone_number, role_id, address_id, active, has_active_appointment)
values (12, 'Ivan', 'Ivanovic', 'isa.confirmtoken+dermatolog3@gmail.com', '$2y$10$I53p57BGuoGgJS/U7cVW6.DKvxg3u/WnUk6KeWhZCjy12M.8ewWX6', '0000000003', 5, 1, true, false);  /* ivan123 */
insert into dermatologist (id, first_name, last_name, email, password, phone_number, role_id, address_id, active, has_active_appointment)
values (13, 'Ivan', 'Ivanovic', 'isa.confirmtoken+dermatolog4@gmail.com', '$2y$10$JsMTn0xKwbYYVJgHXtV8UeWB2NGxhfmcK0Wp2S8BJvoqtWnBpWhuG', '0651229524', 5, 1, true, false);   /* petar123 */
insert into dermatologist (id, first_name, last_name, email, password, phone_number, role_id, address_id, active, has_active_appointment)
values (14, 'Aleksandar', 'Aleksic', 'isa.confirmtoken+dermatolog5@gmail.com', '$2y$10$dlU0v.mQDP7nMr26CulxCe/HdLANvhIMZwgVACXwvJIlsSoBl8V/6', '0661229524', 5, 1, true, false);   /* marko123 */
insert into dermatologist (id, first_name, last_name, email, password, phone_number, role_id, address_id, active, has_active_appointment)
values (15, 'Baja', 'Bajic', 'isa.confirmtoken+dermatolog6@gmail.com', '$2y$10$I53p57BGuoGgJS/U7cVW6.DKvxg3u/WnUk6KeWhZCjy12M.8ewWX6', '0671229524', 5, 1, true, false);   /* ivan123 */


insert into dermatologist (id, first_name, last_name, email, password, phone_number, role_id, address_id, active, has_active_appointment)
values (16, 'Ivan', 'Ivanovic', 'isa.confirmtoken+dermatolog10@gmail.com', null, 0671229524, 5, 1, true, false);   /* first login */

/* insert into dermacy (dermatologist_id, pharmacy_id) values (10, 1);*/
insert into dermacy (dermatologist_id, pharmacy_id, active) values (11, 1, true);
insert into dermacy (dermatologist_id, pharmacy_id, active) values (12, 1, true);
insert into dermacy (dermatologist_id, pharmacy_id, active) values (10, 2, true);
insert into dermacy (dermatologist_id, pharmacy_id, active) values (10, 3, true);
insert into dermacy (dermatologist_id, pharmacy_id, active) values (10, 1, true);

insert into subscription(patient_id, pharmacy_id, active) values (31, 1, true);

-- insert into pharmacist (id, active, email, first_name, last_name, password, phone_number, role_id, address_id, pharmacy_id) /* password: generic*/
-- values (16, true, 'pharmacist1@gmail.com', 'Prvi', 'Farmaceut', '$2y$10$BsFBDiA8QGBpEiGUlAfcx.FnxGgt/uI1LkJ8dig5TRZBp4CA93oOW', '0631223124', 4, 2, 1);
insert into pharmacist (id, active, email, first_name, last_name, password, phone_number, role_id, address_id, pharmacy_id, has_active_appointment)
values (17, true, 'isa.confirmtoken+farmaceut1@gmail.com', 'Drugi', 'Farmaceut', '$2y$10$BsFBDiA8QGBpEiGUlAfcx.FnxGgt/uI1LkJ8dig5TRZBp4CA93oOW', '0631223124', 4, 1, 1, false);

insert into pharmacist (id, first_name, last_name, email, password, phone_number, role_id, address_id, active, pharmacy_id, has_active_appointment)
values (18, 'Janko', 'Jankovic', 'isa.confirmtoken+farmaceut2@gmail.com', '$2y$10$BsFBDiA8QGBpEiGUlAfcx.FnxGgt/uI1LkJ8dig5TRZBp4CA93oOW', '0000000004', 4, 1, true, 1, false);

insert into pharmacist (id, first_name, last_name, email, password, phone_number, role_id, address_id, active, pharmacy_id, has_active_appointment)
values (19, 'Jovan', 'Jovanovic', 'isa.confirmtoken+farmaceut3@gmail.com', '$2y$10$BsFBDiA8QGBpEiGUlAfcx.FnxGgt/uI1LkJ8dig5TRZBp4CA93oOW', '0000000005', 4, 1, true, 2, false);

insert into pharmacist (id, active, email, first_name, last_name, password, phone_number, role_id, address_id, has_active_appointment)
values (20, true, 'isa.confirmtoken+farmaceut4@gmail.com', 'Dragan', 'Dragic', '$2y$10$BsFBDiA8QGBpEiGUlAfcx.FnxGgt/uI1LkJ8dig5TRZBp4CA93oOW', '0611229524', 4, 1, false);

insert into pharmacist (id, active, email, first_name, last_name, password, phone_number, role_id, address_id, has_active_appointment)
values (21, true, 'isa.confirmtoken+farmaceut5@gmail.com', 'Goran', 'Goric', '$2y$10$BsFBDiA8QGBpEiGUlAfcx.FnxGgt/uI1LkJ8dig5TRZBp4CA93oOW', '0621223124', 4, 1, false);

insert into pharmacist (id, first_name, last_name, email, password, phone_number, role_id, address_id, active, has_active_appointment)
values (22, 'Milos', 'Milosevic', 'isa.confirmtoken+farmaceut6@gmail.com', '$2y$10$BsFBDiA8QGBpEiGUlAfcx.FnxGgt/uI1LkJ8dig5TRZBp4CA93oOW', '0631229524', 4, 1, true, false);

insert into pharmacist (id, first_name, last_name, email, password, phone_number, role_id, address_id, active, has_active_appointment)
values (23, 'Gavrilo', 'Princip', 'isa.confirmtoken+farmaceut7@gmail.com', '$2y$10$BsFBDiA8QGBpEiGUlAfcx.FnxGgt/uI1LkJ8dig5TRZBp4CA93oOW', '0641229524', 4, 1, true, false);

insert into pharmacist (id, first_name, last_name, email, password, phone_number, role_id, address_id, active, has_active_appointment)
values (24, 'Marko', 'Cucic', 'isa.confirmtoken+farmaceut8@gmail.com', '$2y$10$BsFBDiA8QGBpEiGUlAfcx.FnxGgt/uI1LkJ8dig5TRZBp4CA93oOW', '0641229524', 4, 1, true, false);


insert into appointment (appointment_status, appointment_type, price, medical_worker_id, start_date_and_time, end_date_and_time, pharmacy_id, active)   /* 21.06 */
values ('FREE', 'EXAMINATION', 670, 11, 1624267504000, 1624274704000, 1, true);
insert into appointment (appointment_status, appointment_type, price, medical_worker_id, start_date_and_time, end_date_and_time, pharmacy_id, active)   /*  25.06 od 11 do 11:30 */
values ('FREE', 'EXAMINATION', 1600, 11, 1624618804000, 1624620604000, 1, true);
insert into appointment (appointment_status, appointment_type, price, medical_worker_id, start_date_and_time, end_date_and_time, pharmacy_id, active)
values ('FREE', 'EXAMINATION', 1100, 10, 1624618804000, 1624620604000, 2, true);

insert into appointment (appointment_status, appointment_type, price, medical_worker_id, patient_id, start_date_and_time, end_date_and_time, pharmacy_id, active)
values ('RESERVED', 'EXAMINATION', 1700, 10, 32, 1623828600000, 1623830400000, 1, true);
insert into appointment (appointment_status, appointment_type, price, medical_worker_id, patient_id, start_date_and_time, end_date_and_time, pharmacy_id, active)
values ('DONE', 'EXAMINATION', 1000, 11, 32, 1621085639000, 1621085649000, 1, true);
insert into appointment (appointment_status, appointment_type, price, medical_worker_id, patient_id, start_date_and_time, end_date_and_time, pharmacy_id, active)
values ('DONE', 'EXAMINATION', 1700, 10, 32, 1618475400000, 1618477200000, 2, true);
insert into appointment (appointment_status, appointment_type, price, medical_worker_id, patient_id, start_date_and_time, end_date_and_time, pharmacy_id, active)
values ('DONE', 'COUNSELING', 1500, 16, 32, 1618475400000, 1618477200000, 1, true);
insert into appointment (appointment_status, appointment_type, price, medical_worker_id, patient_id, start_date_and_time, end_date_and_time, pharmacy_id, active)
values ('RESERVED', 'EXAMINATION', 1700, 10, 32, 1624624200000, 1624626900000, 1, true);    /*  25.06. od 14:30 do 15:15 */
insert into appointment (appointment_status, appointment_type, price, medical_worker_id, patient_id, start_date_and_time, end_date_and_time, pharmacy_id, active)
values ('RESERVED', 'COUNSELING', 1500, 16, 32, 1624871700000, 1624876200000, 1, true); /* 28.6. 11:15 - 11:50 */

insert into appointment (appointment_status, appointment_type, price, medical_worker_id, patient_id, start_date_and_time, end_date_and_time, pharmacy_id, active)
values ('DONE', 'EXAMINATION', 1500, 10, 33, 1618390800000, 1618392600000, 2, true);
insert into appointment (appointment_status, appointment_type, price, medical_worker_id, patient_id, start_date_and_time, end_date_and_time, pharmacy_id, active)
values ('DONE', 'EXAMINATION', 1400, 10, 38, 1618475400000, 1618477200000, 2, true);

insert into medicine_reservation (date_and_time, patient_id, status, active)
values (1624183200000, 31, 'RESERVED', true);
insert into medicine_reservation_item (amount, issued, medicine_reservation_id, pharmacy_medicine_id, active)
values (3, false, 1, 1, true);
insert into medicine_reservation_item (amount, issued, medicine_reservation_id, pharmacy_medicine_id, active)
values (2, false, 1, 2, true);

insert into medicine_reservation (date_and_time, patient_id, status, active)
values (1624183200000, 33, 'RESERVED', true);
insert into medicine_reservation_item (amount, issued, medicine_reservation_id, pharmacy_medicine_id, active)
values (5, false, 2, 1, true);
insert into medicine_reservation_item (amount, issued, medicine_reservation_id, pharmacy_medicine_id, active)
values (1, false, 2, 3, true);


insert into work_schedule(start_time, end_time, medical_worker_id, pharmacy_id, active) /* 8:00h - 12:00h */
values (480, 720, 10, 2, true);
insert into work_schedule(start_time, end_time, medical_worker_id, pharmacy_id, active)
values (780, 960, 10, 3, true);
insert into work_schedule(start_time, end_time, medical_worker_id, pharmacy_id, active)
values (480, 720, 11, 1, true);
insert into work_schedule(start_time, end_time, medical_worker_id, pharmacy_id, active)
values (720, 960, 12, 1, true);
insert into work_schedule(start_time, end_time, medical_worker_id, pharmacy_id, active)
values (480, 960, 17, 1, true);
insert into work_schedule(start_time, end_time, medical_worker_id, pharmacy_id, active)
values (480, 960, 18, 1, true);
insert into work_schedule(start_time, end_time, medical_worker_id, pharmacy_id, active)
values (480, 960, 19, 2, true);
insert into work_schedule(start_time, end_time, medical_worker_id, pharmacy_id, active)
values (480, 960, 20, 2, true);
insert into work_schedule(start_time, end_time, medical_worker_id, pharmacy_id, active)
values (480, 960, 21, 1, true);
insert into work_schedule(start_time, end_time, medical_worker_id, pharmacy_id, active)
values (480, 960, 22, 1, true);
insert into work_schedule(start_time, end_time, medical_worker_id, pharmacy_id, active)
values (480, 960, 23, 1, true);
insert into work_schedule(start_time, end_time, medical_worker_id, pharmacy_id, active)
values (480, 960, 24, 1, true);
insert into work_schedule(start_time, end_time, medical_worker_id, pharmacy_id, active) /* 8:00h - 12:00h */
values (960, 1440, 10, 1, true);

insert into supplier(id, active, email, first_name, last_name, password, phone_number, role_id, address_id)
values (51, true, 'isa.confirmtoken+dobavljac1@gmail.com', 'Mina', 'Nikolic', '$2y$10$fNkGx7fom3xUlKoCb3XnlOUjyyMF99pDiZkHEEwhneRqjpReJqC8q', '0654567654', 3, 1);    /* pw12345678*/

insert into orders (created_date, deadline, pharmacy_id, pharmacy_admin_id, order_status, active)
    values (1621022654000, 1621368254000, 1, 100, 'WAIT_DECISION', true);
insert into order_items (amount, medicine_id, order_id, active) values (200, 1, 1, true);

insert into order_items (amount, medicine_id, order_id, active) values (200, 2, 1, true);

insert into order_offer (active, delivery_deadline, order_offer_status, total_price, order_id, supplier_id)
values (true, 1621368254000, 'UNKNOWN', 300.0, 1, 51);
insert into order_offer (active, delivery_deadline, order_offer_status, total_price, order_id, supplier_id)
values (true, 1621368254000, 'UNKNOWN', 2000.0, 1, 51);
insert into order_offer (active, delivery_deadline, order_offer_status, total_price, order_id, supplier_id)
values (true, 1621368254000, 'UNKNOWN', 1500.0, 1, 51);

insert into system_admin (id, first_name, last_name, email, phone_number, role_id, address_id, active)
values (40, 'Admin', 'Admin', 'isa.confirmtoken+sistemAdmin1@gmail.com', 0000000005, 1, 1, true);
insert into system_admin (id, active, email, first_name, last_name, password, phone_number, address_id, role_id)
values (41, true, 'isa.confirmtoken+sistemAdmin2@gmail.com', 'Nikola', 'Petrovic', '$2y$10$fNkGx7fom3xUlKoCb3XnlOUjyyMF99pDiZkHEEwhneRqjpReJqC8q', '0654567654', 1, 1);  /* pw12345678*/

insert into complaint (complaint_message, complaint_response, patient_id, system_admin_id, active, entity)
values ('Farmaceut Janko Jankovic je bio izuzetno neljubazan.', 'Skrenucemo mu paznju. Izvinjavamo se na pretrpljenom psihickom bolu.', 31, 41, true, '18');
insert into complaint (complaint_message, complaint_response, patient_id, system_admin_id, active, entity)
values ('Dugo se ceka na pregled.', 'Odredjeni lekari su izuzetno trazeni.', 35, 41, true, '12');
insert into complaint (complaint_message, complaint_response, patient_id, system_admin_id, active, entity)
values ('Baja Bajic, dermatolog, nije stavio rukavice prilikom pregleda.', 'Izvinjavamo se na propustu.', 37, 41, true, '15');
insert into complaint (complaint_message, patient_id, active, entity)
values ('Dermatolog Aleksandar Aleksic psuje.', 32, true, '14');
insert into complaint (complaint_message, patient_id, active, entity)
values ('Ordinacija je jako neuredna.', 31, true, '16');


insert into time_off_request(approved, content, start_date, end_date, medical_worker_id, pharmacy_id, active)
            values (null, 'Sadrzaj', 1624521600000, 1625385600000, 16, 1, true);
insert into time_off_request(approved, content, start_date, end_date, medical_worker_id, pharmacy_id, active)
            values (null, 'Sadrzaj', 1624521600000, 1625385600000, 17, 1, true);

insert into medicine_inquiry(date_created, resolved, medicine_id, pharmacy_id, active)
            values (1621843113000, false, 1, 1, true);
insert into medicine_inquiry(date_created, resolved, medicine_id, pharmacy_id, active)
            values (1621843113000, false, 2, 1, true);
