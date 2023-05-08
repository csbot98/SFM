-- Szükséges SQL kódok::

CREATE TABLE felszereles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nev VARCHAR(255) NOT NULL,
    darab INT NOT NULL
);

INSERT INTO felszereles (nev, darab)
VALUES ('felnott_melleny', 30),
       ('felnott_gumi', 30),
       ('gyerek_melleny', 30),
       ('gyerek_gumi', 30),
       ('csonak', 10);

CREATE TABLE foglalasok (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nev VARCHAR(255) NOT NULL,
	datum DATE NOT NULL,
    felnott_melleny INT NOT NULL,
    felnott_gumi INT NOT NULL,
    gyerek_melleny INT,
    gyerek_gumi INT,
    csonak BOOLEAN NOT NULL,
	aktiv BOOLEAN NOT NULL
);

CREATE TABLE jovobeni_foglalasok (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nev VARCHAR(255) NOT NULL,
	datum DATE NOT NULL,
    felnott_melleny INT NOT NULL,
    felnott_gumi INT NOT NULL,
    gyerek_melleny INT,
    gyerek_gumi INT,
    csonak BOOLEAN NOT NULL,
	aktiv BOOLEAN NOT NULL
);

-- A "foglalasok" táblába, mindenféleképpen kell egy sor
INSERT INTO foglalasok (nev, datum, felnott_melleny, felnott_gumi, gyerek_melleny, gyerek_gumi, csonak, aktiv) VALUES ('Teszt Elek', NOW(), 0, 0, 0, 0, 0, 0);

INSERT INTO jovobeni_foglalasok (nev, datum, felnott_melleny, felnott_gumi, gyerek_melleny, gyerek_gumi, csonak, aktiv) VALUES ('Teszt Elekje', 20230510, 2, 3, 1, 4, 1, 1);

-- A JAVA kódban megadott SQL lekérdezéseknek csak az egyik fő kódját rakom be ide, a többit ezen alapján már ki lehet találni.

-- müködő összes sql lekérdezés (jovobeni)
-- jovobeni_foglalasok.datum=:jovobeni_datum //ezt a részt majd a felhasználó adja meg

WITH felszereles_osszesites AS (
		SELECT 
		SUM(CASE WHEN nev = 'felnott_melleny' THEN darab ELSE 0 END) AS felszereles_felnott_melleny,
		SUM(CASE WHEN nev = 'felnott_gumi' THEN darab ELSE 0 END) AS felszereles_felnott_gumi,
		SUM(CASE WHEN nev = 'gyerek_melleny' THEN darab ELSE 0 END) AS felszereles_gyerek_melleny,
		SUM(CASE WHEN nev = 'gyerek_gumi' THEN darab ELSE 0 END) AS felszereles_gyerek_gumi,
		SUM(CASE WHEN nev = 'csonak' THEN darab ELSE 0 END) AS felszereles_csonak
		FROM felszereles),
foglalasok_osszesites AS (
		SELECT
		SUM(CASE WHEN foglalasok.aktiv = TRUE AND foglalasok.datum=CURRENT_DATE AND felnott_melleny > 0 THEN felnott_melleny ELSE 0 END) AS foglalasok_felnott_melleny,
		SUM(CASE WHEN foglalasok.aktiv = TRUE AND foglalasok.datum=CURRENT_DATE AND felnott_gumi > 0 THEN felnott_gumi ELSE 0 END) AS foglalasok_felnott_gumi,
		SUM(CASE WHEN foglalasok.aktiv = TRUE AND foglalasok.datum=CURRENT_DATE AND gyerek_melleny > 0 THEN gyerek_melleny ELSE 0 END) AS foglalasok_gyerek_melleny,
		SUM(CASE WHEN foglalasok.aktiv = TRUE AND foglalasok.datum=CURRENT_DATE AND gyerek_gumi > 0 THEN gyerek_gumi ELSE 0 END) AS foglalasok_gyerek_gumi, 
		SUM(CASE WHEN foglalasok.aktiv = TRUE AND foglalasok.datum=CURRENT_DATE AND csonak > 0 THEN csonak ELSE 0 END) AS foglalasok_csonak
		FROM foglalasok),
jovobeni_foglalasok_osszesites AS (
		SELECT
		SUM(CASE WHEN jovobeni_foglalasok.aktiv = TRUE AND jovobeni_foglalasok.datum=20230519 AND felnott_melleny > 0 THEN felnott_melleny ELSE 0 END) AS jovobeni_foglalasok_felnott_melleny,
		SUM(CASE WHEN jovobeni_foglalasok.aktiv = TRUE AND jovobeni_foglalasok.datum=20230519 AND felnott_gumi > 0 THEN felnott_gumi ELSE 0 END) AS jovobeni_foglalasok_felnott_gumi,
		SUM(CASE WHEN jovobeni_foglalasok.aktiv = TRUE AND jovobeni_foglalasok.datum=20230519 AND gyerek_melleny > 0 THEN gyerek_melleny ELSE 0 END) AS jovobeni_foglalasok_gyerek_melleny,
		SUM(CASE WHEN jovobeni_foglalasok.aktiv = TRUE AND jovobeni_foglalasok.datum=20230519 AND gyerek_gumi > 0 THEN gyerek_gumi ELSE 0 END) AS jovobeni_foglalasok_gyerek_gumi,
		SUM(CASE WHEN jovobeni_foglalasok.aktiv = TRUE AND jovobeni_foglalasok.datum=20230519 AND csonak > 0 THEN csonak ELSE 0 END) AS jovobeni_foglalasok_csonak
		FROM jovobeni_foglalasok
)
SELECT
	-- felnott_melleny:
	--	felszereles_osszesites.felszereles_felnott_melleny,
	--	foglalasok_osszesites.foglalasok_felnott_melleny,
	--	jovobeni_foglalasok_osszesites.jovobeni_foglalasok_felnott_melleny,
		felszereles_osszesites.felszereles_felnott_melleny - foglalasok_osszesites.foglalasok_felnott_melleny - jovobeni_foglalasok_osszesites.jovobeni_foglalasok_felnott_melleny AS elerheto_felnott_melleny,
		
	-- felnott_gumi:
	-- felszereles_osszesites.felszereles_felnott_gumi,
    -- foglalasok_osszesites.foglalasok_felnott_gumi,
    -- jovobeni_foglalasok_osszesites.jovobeni_foglalasok_felnott_gumi,
	 felszereles_osszesites.felszereles_felnott_gumi - foglalasok_osszesites.foglalasok_felnott_gumi - jovobeni_foglalasok_osszesites.jovobeni_foglalasok_felnott_gumi AS elerheto_felnott_gumi,
	
	-- gyerek_melleny:
	-- felszereles_osszesites.felszereles_gyerek_melleny,
    -- foglalasok_osszesites.foglalasok_gyerek_melleny,
    -- jovobeni_foglalasok_osszesites.jovobeni_foglalasok_gyerek_melleny,
	 felszereles_osszesites.felszereles_gyerek_melleny - foglalasok_osszesites.foglalasok_gyerek_melleny - jovobeni_foglalasok_osszesites.jovobeni_foglalasok_gyerek_melleny AS elerheto_gyerek_melleny,
	
	-- gyerek_gumi:
	-- felszereles_osszesites.felszereles_gyerek_gumi,
    -- foglalasok_osszesites.foglalasok_gyerek_gumi,
    -- jovobeni_foglalasok_osszesites.jovobeni_foglalasok_gyerek_gumi,
	 felszereles_osszesites.felszereles_gyerek_gumi - foglalasok_osszesites.foglalasok_gyerek_gumi - jovobeni_foglalasok_osszesites.jovobeni_foglalasok_gyerek_gumi AS elerheto_gyerek_gumi,
	
	-- csonak:
	--	felszereles_osszesites.felszereles_csonak,
	--	foglalasok_osszesites.foglalasok_csonak,
	--	jovobeni_foglalasok_osszesites.jovobeni_foglalasok_csonak,
		felszereles_osszesites.felszereles_csonak  - jovobeni_foglalasok_osszesites.jovobeni_foglalasok_csonak  - foglalasok_osszesites.foglalasok_csonak AS elerheto_csonak
FROM felszereles_osszesites, foglalasok_osszesites, jovobeni_foglalasok_osszesites;


