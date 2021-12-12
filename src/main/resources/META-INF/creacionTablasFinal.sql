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

INSERT INTO provincia VALUES 
	--Argentina
	(DEFAULT,'Santa Fe',1), (DEFAULT,'Buenos Aires',1), (DEFAULT,'Entre Rios',1), (DEFAULT,'Chubut',1), (DEFAULT,'Cordoba',1), (DEFAULT,'Tucumán',1),
	--Brasil
	(DEFAULT,'Rio de Janeiro',2), (DEFAULT,'São Paulo',2), (DEFAULT,'Bahía',2), (DEFAULT,'Santa Catarina',2), (DEFAULT,'Minas Gerais',2),
	--Chile
	(DEFAULT,'Santiago',3), (DEFAULT,'Los Andes',3), (DEFAULT,'Valparaíso',3), (DEFAULT,'Concepción',3), (DEFAULT,'Valdivía',3),
	--Uruguay
	(DEFAULT,'Montevideo',4), (DEFAULT,'Flores',4), (DEFAULT,'Durazno',4), (DEFAULT,'Cerro Largo',4), (DEFAULT,'Soriano',4),
	--Bolivia
	(DEFAULT,'Beni',5), (DEFAULT,'Chuquisaca',5), (DEFAULT,'La Paz',5), (DEFAULT,'Potosí',5);

CREATE TABLE localidad(
	id_localidad serial PRIMARY KEY,
	nombre_localidad varchar(30),
	id_provincia integer REFERENCES provincia(id_provincia)
);

INSERT INTO localidad VALUES 
	--Argentina
	(DEFAULT,'La Capital',1), (DEFAULT,'Rosario',1), (DEFAULT,'Belgrano',1), (DEFAULT,'San Justo',1), (DEFAULT,'San Javier',1), (DEFAULT,'AMBA',2), 
	--Brasil
	(DEFAULT,'Rio de Janeiro',7), (DEFAULT,'São Paulo',8), (DEFAULT,'Salvador de Bahía',9), (DEFAULT,'Florianópolis',10), (DEFAULT,'Belo Horizonte',11), 
	(DEFAULT,'Fortaleza',9), (DEFAULT,'Recife',9),  (DEFAULT,'Natal',9), 
	--Chile
	(DEFAULT,'Santiago',12), (DEFAULT,'Cerrillos',12), (DEFAULT,'Recoleta',12), (DEFAULT,'San Esteban',13), (DEFAULT,'Quintero',14), (DEFAULT,'Casablanca',14), 
	(DEFAULT,'Florida',15),  (DEFAULT,'Los Lagos',16),  (DEFAULT,'Lanco',16),
	--Uruguay
	(DEFAULT,'Montevideo',17), (DEFAULT,'Trinidad',18), (DEFAULT,'Durazno',19), (DEFAULT,'Carmen',19), (DEFAULT,'Melo',20), (DEFAULT,'Río Branco',20), (DEFAULT,'Mercedes',21), (DEFAULT,'Dolores',21),
	--Bolivia
	(DEFAULT,'Trinidad ',22), (DEFAULT,'Sucre',23), (DEFAULT,'La Paz',24), (DEFAULT,'Potosí',25);

CREATE TABLE direccion(
	id_direccion serial PRIMARY KEY,
	calle varchar(30),
	numero integer,
	piso varchar(30),
	departamento varchar(30),
	codigo_postal varchar(20),
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

CREATE TABLE reserva(
	id_reserva serial PRIMARY KEY,
	nombre varchar(30),
	apellido varchar(30),
	telefono varchar(30)
);

CREATE TABLE pago(
	id_pago serial PRIMARY KEY,
	monto_total numeric,
	fecha_y_hora timestamp,
	vuelto numeric
);

CREATE TABLE persona_juridica(
	cuit bigint PRIMARY KEY,
	razon_social varchar(30),
	email varchar(30),
	telefono varchar(30),
	id_direccion integer REFERENCES direccion(id_direccion),
	id_tipo_posicion_frente_IVA integer REFERENCES tipo_posicion_frente_IVA(id_tipo_posicion_frente_IVA)
);

CREATE TABLE nota_de_credito(
	numero_nota serial PRIMARY KEY,
	importe_neto numeric,
	iva double precision,
	importe_total numeric,
	cuit bigint REFERENCES persona_juridica(cuit), 
	id_persona_fisica integer REFERENCES persona_fisica(id_persona_fisica)
);

CREATE TABLE factura(
	numero serial PRIMARY KEY,
	importe_neto numeric,
	tipo varchar(30),
	importe_total numeric,
	fecha_emision date,
	numero_nota integer REFERENCES nota_de_credito(numero_nota),
	cuit bigint REFERENCES persona_juridica(cuit),
	id_persona_fisica integer REFERENCES persona_fisica(id_persona_fisica),
	id_pago integer REFERENCES pago(id_pago)
);

CREATE TABLE tipo_habitacion(
	nombre varchar(30) PRIMARY KEY, 
	precio_actual numeric
);

CREATE TABLE habitacion(
	numero serial PRIMARY KEY,
	estado varchar(30), /* Faltaria un check con los estados posibles */
	capacidad integer,
	tipo_habitacion varchar(30) REFERENCES tipo_habitacion(nombre)
);

CREATE TABLE asociada_a(
	habitacion integer REFERENCES habitacion(numero),
	reserva integer REFERENCES reserva(id_reserva),
	fecha_inicio date,
	fecha_fin date,
	CONSTRAINT pk_asociada_a PRIMARY KEY (habitacion,reserva)
);

CREATE TABLE periodo_reserva(
	id_periodo_reserva serial PRIMARY KEY,
	fecha_inicio date,
	fecha_fin date,
	numero_habitacion integer REFERENCES habitacion(numero),
	id_reserva integer REFERENCES reserva(id_reserva)
);

CREATE TABLE estadia(
	id_estadia serial PRIMARY KEY,
	fecha_inicio date,
	fecha_fin date,
	costo numeric,
	descuento double precision,
	costo_final numeric,
	numero_habitacion integer REFERENCES habitacion(numero),
	id_persona_fisica integer REFERENCES persona_fisica(id_persona_fisica),
	numero_factura integer REFERENCES factura(numero)
);

CREATE TABLE servicio_prestado(
	id_servicio serial PRIMARY KEY,
	nombre varchar(30),
	precio numeric,
	cantidad integer,
	fecha date,
	tipo varchar(30),/* Faltaria un check con los tipos posibles */
	id_estadia integer REFERENCES estadia(id_estadia)
);

CREATE TABLE pasajero(
	id_estadia integer REFERENCES estadia(id_estadia),
	id_persona_fisica integer REFERENCES persona_fisica(id_persona_fisica),
	CONSTRAINT pk_pasajero PRIMARY KEY (id_estadia,id_persona_fisica)
);

CREATE TABLE banco(
	id_banco serial PRIMARY KEY,
	nombre_banco varchar(30)
);

CREATE TABLE plaza(
	id_banco integer REFERENCES banco(id_banco),
	id_localidad integer REFERENCES localidad(id_localidad),
	CONSTRAINT pk_plaza PRIMARY KEY (id_banco,id_localidad)
);

CREATE TABLE servicio_facturado(
	id_servicio_facturado serial PRIMARY KEY,
	nombre varchar(30),
	precio_unitario numeric,
	cantidad integer,
	precio_total numeric,
	id_servicio integer REFERENCES servicio_prestado(id_servicio),
	numero_factura integer REFERENCES factura(numero)
);

CREATE TABLE efectivo(
	id_medio_de_pago serial PRIMARY KEY,
	importe numeric,
	cotizacion double precision,
	importe_en_pesos numeric,
	id_pago integer REFERENCES pago(id_pago)
);


CREATE TABLE tarjeta_debito(
	id_medio_de_pago serial PRIMARY KEY,
	numero_tarjeta integer,
	fecha_vencimiento date,
	nombre varchar(30),
	codigo_seguridad integer,
	importe numeric,
	cotizacion double precision,
	importe_en_pesos numeric,
	id_pago integer REFERENCES pago(id_pago)
);

CREATE TABLE tarjeta_credito(
	id_medio_de_pago serial PRIMARY KEY,
	numero_tarjeta integer,
	fecha_vencimiento date,
	nombre varchar(30),
	codigo_seguridad integer,
	importe numeric,
	cotizacion double precision,
	importe_en_pesos numeric,
	id_pago integer REFERENCES pago(id_pago)
);

CREATE TABLE cheque(
	id_medio_de_pago serial PRIMARY KEY,
	numero_cheque integer,
	fecha_cobro date,
	importe numeric,
	cotizacion double precision,
	importe_en_pesos numeric,
	id_pago integer REFERENCES pago(id_pago),
	id_banco integer REFERENCES banco(id_banco)
);

CREATE TABLE usuario(
	usuario varchar(30) PRIMARY KEY,
	contraseña varchar(30)
);

/*
INSERT INTO direccion VALUES (DEFAULT, 'Mitre', 3667, null, null, 3016, 1);

INSERT INTO persona_fisica
VALUES (DEFAULT,'DNI',12345678,'BECKER','ALISSON','02-10-1992','ALIBECKER@GMAIL.COM','INGENIERO','BRASILERO','3429466178',1,1),
	(DEFAULT,'DNI',42329627,'MORENO','MARIANO','18-06-1987','MARIANOMORENO@GMAIL.COM','ARQUITECTO','BOLIVIANO','3421472589',1,1),
	(DEFAULT,'DNI',23861076,'DE PAUL','RODRIGO','24-05-1994','RODEPAUL@GMAIL.COM','PROFESOR','ARGENTINA','3425684315',1,1),
	(DEFAULT,'DNI',34227524,'GOMEZ','ALEJANDRO','15-01-1988','ALEJANDROGOMEZ@GMAIL.COM','ABOGADO','ARGENTINA','3425684547',1,1),
	(DEFAULT,'DNI',40587514,'RIQUELME','NAHUEL','09-12-1999','NAHUELRIQUIELME@GMAIL.COM','ESTUDIANTE','ARGENTINA','3425684598',1,1);

INSERT INTO tipo_habitacion VALUES
	('INDIVIDUAL ESTÁNDAR',null),
	('DOBLE ESTÁNDAR',null),
	('DOBLE SUPERIOR',null),
	('SUPERIOR FAMILY PLAN',null),
	('SUITE DOBLE',null);

INSERT INTO habitacion VALUES 
	(4,'DISPONIBLE',1,'INDIVIDUAL ESTÁNDAR'),
	(5,'RESERVADA',2,'DOBLE ESTÁNDAR'),
	(6,'OCUPADA',2,'DOBLE SUPERIOR'),
	(7,'FUERA_DE_SERVICIO',5,'SUPERIOR FAMILY PLAN'),
	(1,'DISPONIBLE',1,'INDIVIDUAL ESTÁNDAR'),
	(20,'DISPONIBLE',1,'INDIVIDUAL ESTÁNDAR'),
	(18,'OCUPADA',2,'DOBLE SUPERIOR'),
	(17,'FUERA_DE_SERVICIO',5,'SUPERIOR FAMILY PLAN'),
	(15,'DISPONIBLE',5,'SUPERIOR FAMILY PLAN'),
	(12,'DISPONIBLE',5,'SUPERIOR FAMILY PLAN'),
	(2,'OCUPADA',2,'DOBLE ESTÁNDAR'),
	(3,'DISPONIBLE',2,'DOBLE ESTÁNDAR'),
	(10,'DISPONIBLE',2,'DOBLE SUPERIOR'),
	(8,'DISPONIBLE',2,'DOBLE SUPERIOR');

INSERT INTO estadia VALUES (1,'2021-12-15','2021-12-20', 1500.00 ,null,null,1,1,null),(2,'2021-12-15','2021-12-20',null,null,null,2,2,null),(3,'2021-12-15','2021-12-20',null,null,null,3,3,null),(4,'2021-12-15','2021-12-20',null,null,null,4,4,null);

INSERT INTO reserva VALUES 
	(1,'NAHUEL','RIQUELME',456),
	(2,'ALEJANDRO','GOMEZ',123),
	(3,'RODRIGO','DE PAUL',487),
	(4,'MARIANO','MORENO',897);

INSERT INTO periodo_reserva VALUES 
	(1,'2021-12-21','2021-12-25',4,1),
	(2,'2021-12-21','2021-12-25',5,2),
	(3,'2021-12-21','2021-12-25',6,3),
	(4,'2021-12-21','2021-12-25',7,4);

INSERT INTO pasajero VALUES (1, 2), (1, 3);

insert into servicio_prestado values (DEFAULT, 'CERVEZA MILLER', 105, 10, '2021-12-16', 'BAR', 1);

INSERT INTO persona_juridica VALUES (12345678911, 'MIAMI AIRPORT', null, null, 1, 1);
*/
/*
DROP TABLE periodo_reserva, asociada_a, efectivo, estadia, factura, habitacion, nota_de_credito, pago, pasajero, 
		   persona_juridica, plaza, reserva, servicio_facturado, servicio_prestado, tarjeta_credito,
		   tarjeta_debito, tipo_habitacion, usuario, cheque, banco, cheque, 
		   pais, provincia, localidad, direccion, tipo_posicion_frente_iva, persona_fisica;

*/