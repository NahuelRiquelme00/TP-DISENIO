CREATE TABLE pais(
	id_pais serial PRIMARY KEY,
	nombre_pais varchar(30)
);

INSERT INTO pais VALUES (DEFAULT,'Argentina'), (DEFAULT,'Brasil'), (DEFAULT,'Chile'), (DEFAULT,'Uruguay'), (DEFAULT,'Bolivia');


CREATE TABLE provincia(
	id_provincia serial PRIMARY KEY,
	nombre_provincia varchar(30),
	id_pais integer REFERENCES pais(id_pais)
);

INSERT INTO provincia VALUES (DEFAULT,'Santa Fe',1), (DEFAULT,'Buenos Aires',1), (DEFAULT,'Entre Rios',1), (DEFAULT,'Chubut',1), (DEFAULT,'Cordoba',1), (DEFAULT,'Tucumán',1);
INSERT INTO provincia VALUES (DEFAULT,'Rio de Janeiro',2), (DEFAULT,'São Paulo',2), (DEFAULT,'Bahía',2), (DEFAULT,'Santa Catarina',2), (DEFAULT,'Minas Gerais',2);
INSERT INTO provincia VALUES (DEFAULT,'Santiago',3), (DEFAULT,'Los Andes',3), (DEFAULT,'Valparaíso',3), (DEFAULT,'Concepción',3), (DEFAULT,'Valdivía',3);
INSERT INTO provincia VALUES (DEFAULT,'Montevideo',4), (DEFAULT,'Flores',4), (DEFAULT,'Durazno',4), (DEFAULT,'Cerro Largo',4), (DEFAULT,'Soriano',4);
INSERT INTO provincia VALUES (DEFAULT,'Beni',5), (DEFAULT,'Chuquisaca',5), (DEFAULT,'La Paz',5), (DEFAULT,'Potosí',5);

CREATE TABLE localidad(
	id_localidad serial PRIMARY KEY,
	nombre_localidad varchar(30),
	id_provincia integer REFERENCES provincia(id_provincia)
);

INSERT INTO localidad VALUES (DEFAULT,'La Capital',1), (DEFAULT,'Rosario',1), (DEFAULT,'Belgrano',1), (DEFAULT,'San Justo',1), (DEFAULT,'San Javier',1), (DEFAULT,'AMBA',2);
INSERT INTO localidad VALUES (DEFAULT,'Rio de Janeiro',7), (DEFAULT,'São Paulo',8), (DEFAULT,'Salvador de Bahía',9), (DEFAULT,'Florianópolis',10), (DEFAULT,'Belo Horizonte',11), (DEFAULT,'Fortaleza',9), (DEFAULT,'Recife',9),  (DEFAULT,'Natal',9);
INSERT INTO localidad VALUES (DEFAULT,'Santiago',12), (DEFAULT,'Cerrillos',12), (DEFAULT,'Recoleta',12), (DEFAULT,'San Esteban',13), (DEFAULT,'Quintero',14), (DEFAULT,'Casablanca',14), (DEFAULT,'Florida',15),  (DEFAULT,'Los Lagos',16),  (DEFAULT,'Lanco',16);
INSERT INTO localidad VALUES (DEFAULT,'Montevideo',17), (DEFAULT,'Trinidad',18), (DEFAULT,'Durazno',19), (DEFAULT,'Carmen',19), (DEFAULT,'Melo',20), (DEFAULT,'Río Branco',20), (DEFAULT,'Mercedes',21), (DEFAULT,'Dolores',21);
INSERT INTO localidad VALUES (DEFAULT,'	Trinidad ',22), (DEFAULT,'Sucre',23), (DEFAULT,'La Paz',24), (DEFAULT,'Potosí',25);

CREATE TABLE direccion(
	id_direccion serial PRIMARY KEY,
	calle varchar(30),
	numero integer,
	piso varchar(30),
	departamento varchar(30),
	codigo_postal integer,
	id_localidad integer REFERENCES localidad(id_localidad)
);

CREATE TABLE tipo_posicion_frente_IVA(
	id_tipo_posicion_frente_IVA serial PRIMARY KEY,
	nombre varchar(30),
	tipo_factura varchar(30) CHECK (tipo_factura IN ('A','B','E'))
);

CREATE TABLE persona_fisica(
	id_persona_fisica serial PRIMARY KEY,
	tipo_documento varchar(30) CHECK (tipo_documento IN ('DNI','LE','LC','PASAPORTE','OTRO')),
	numero_documento integer,
	apellido varchar(30),
	nombres varchar(30),
	fecha_nacimiento date,
	email varchar(30),
	ocupacion varchar(30),
	nacionalidad varchar(30),
	telefono varchar(30),
	id_direccion integer REFERENCES direccion(id_direccion),
	id_tipo_posicion_frente_IVA integer REFERENCES tipo_posicion_frente_IVA(id_tipo_posicion_frente_IVA)
);

INSERT INTO tipo_posicion_frente_IVA
VALUES (DEFAULT, 'RESPONSABLE INSCRIPTO', 'A'),(DEFAULT, 'CONSUMIDOR FINAL', 'B'), (DEFAULT, 'MONOTRIBUTISTA', 'A'), (DEFAULT, 'SUJETO EXENTO', 'B'), (DEFAULT, 'EXTRANJERO', 'E');

	   
INSERT INTO persona_fisica
VALUES (DEFAULT,
		'DNI',
		40587514,
		'RIQUELME',
		'NAHUEL',
		'1999-12-09',
		'NAHUELRIQUIELME@GMAIL.COM',
		'ESTUDIANTE',
		'ARGENTINA',
		'3425684598',
	   	null,
	   	1);
		
INSERT INTO persona_fisica
VALUES (DEFAULT,
		'DNI',
		34227524,
		'GOMEZ',
		'ALEJANDRO',
		'1988-01-15',
		'ALEJANDROGOMEZ@GMAIL.COM',
		'ABOGADO',
		'ARGENTINA',
		'3425684547',
	   	null,
	   	1);
		
INSERT INTO persona_fisica
VALUES (DEFAULT,
		'DNI',
		34227524,
		'DE PAUL',
		'RODRIGO',
		'1994-05-24',
		'RODEPAUL@GMAIL.COM',
		'PROFESOR',
		'ARGENTINA',
		'3425684315',
	   	null,
	   	1);

INSERT INTO persona_fisica
VALUES (DEFAULT,
		'DNI',
		34227524,
		'MORENO',
		'MARIANO',
		'1987-06-18',
		'MARIANOMORENO@GMAIL.COM',
		'ARQUITECTO',
		'BOLIVIANO',
		'3421472589',
	   	null,
	   	1);

INSERT INTO persona_fisica
VALUES (DEFAULT,
		'DNI',
		34227524,
		'BECKER',
		'ALISSON',
		'1992-10-02',
		'ALIBECKER@GMAIL.COM',
		'INGENIERO',
		'BRASILERO',
		'3429466178',
	   	null,
	   	1);


--DROP TABLE pais;
--DROP TABLE provincia;
--DROP TABLE localidad;
--DROP TABLE direccion;
--DROP TABLE tipo_posicion_frente_IVA;
--DROP TABLE persona_fisica;




