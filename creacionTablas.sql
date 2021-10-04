--CREATE TYPE tipoDocumento AS ENUM ('DNI','LE','LC','PASAPORTE','OTRO');
--CREATE TYPE tipoFactura AS ENUM ('A','B');

CREATE TABLE pais(
	id_pais serial PRIMARY KEY,
	nombre_pais varchar(30)
);

INSERT INTO pais VALUES (DEFAULT,'Argentina'), (DEFAULT,'Brazil'), (DEFAULT,'Chile'), (DEFAULT,'Uruguay'), (DEFAULT,'Bolivia');


CREATE TABLE provincia(
	id_provincia serial PRIMARY KEY,
	nombre_provincia varchar(30),
	id_pais integer REFERENCES pais(id_pais)
);

INSERT INTO provincia VALUES (DEFAULT,'Santa Fe',1), (DEFAULT,'Buenos Aires',1), (DEFAULT,'Entre Rios',1), (DEFAULT,'Chubut',1), (DEFAULT,'Cordoba',1);

CREATE TABLE localidad(
	id_localidad serial PRIMARY KEY,
	nombre_localidad varchar(30),
	id_provincia integer REFERENCES provincia(id_provincia)
);

INSERT INTO localidad VALUES (DEFAULT,'La Capital',1), (DEFAULT,'Rosario',1), (DEFAULT,'Belgrano',1), (DEFAULT,'San Justo',1), (DEFAULT,'San Javier',1);

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
	tipo_factura varchar(30) CHECK (tipo_factura IN ('A','B'))
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

DROP TABLE persona_fisica;
DROP TABLE tipo_posicion_frente_IVA;
DROP TABLE direccion;

INSERT INTO persona_fisica
VALUES (DEFAULT,
		'DNI',
		40587514,
		'Riquelme',
		'Nahuel',
		'09-12-1999',
		'nahuelriquelme@gmail.com',
		'Estudiante',
		'Argentina',
		'3425684598',
	   	1,
	   	1);
		
INSERT INTO persona_fisica
VALUES (DEFAULT,
		'DNI',
		40585894,
		'Apellido1',
		'Nombre1',
		'09-12-1998',
		'persona1@gmail.com',
		'Abogado',
		'Argentina',
		'3425684547',
	   	1,
	   	1);
		
INSERT INTO tipo_posicion_frente_IVA
VALUES (DEFAULT, 'RESPONSABLE INSCRIPTO', 'A');

INSERT INTO direccion
VALUES (DEFAULT,
	   'Mitre',
	   5891,
	   null,
	   'Santa Fe',
	   3000,
	   null);


