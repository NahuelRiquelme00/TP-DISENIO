CREATE TABLE reserva(
	id_reserva serial PRIMARY KEY,
	nombre varchar(30),
	apellido varchar(30),
	telefono varchar(30)
);

CREATE TABLE pago(
	id_pago serial PRIMARY KEY,
	monto_total bytea,
	fecha_y_hora timestamp,
	vuelto bytea
);

CREATE TABLE persona_juridica(
	cuit integer PRIMARY KEY,
	razon_social varchar(30),
	email varchar(30),
	telefono varchar(30),
	id_direccion integer REFERENCES direccion(id_direccion),
	id_tipo_posicion_frente_IVA integer REFERENCES tipo_posicion_frente_IVA(id_tipo_posicion_frente_IVA)
);

CREATE TABLE nota_de_credito(
	numero_nota serial PRIMARY KEY,
	importe_neto bytea,
	iva double precision,
	importe_total bytea,
	cuit integer REFERENCES persona_juridica(cuit), 
	id_persona_fisica integer REFERENCES persona_fisica(id_persona_fisica)
);

CREATE TABLE factura(
	numero serial PRIMARY KEY,
	importe_neto bytea,
	tipo varchar(30),
	importe_total bytea,
	fecha_emision date,
	numero_nota integer REFERENCES nota_de_credito(numero_nota),
	cuit integer REFERENCES persona_juridica(cuit),
	id_persona_fisica integer REFERENCES persona_fisica(id_persona_fisica),
	id_pago integer REFERENCES pago(id_pago)
);

CREATE TABLE tipo_habitacion(
	nombre varchar(30) PRIMARY KEY, 
	precio_actual bytea
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
	costo bytea,
	descuento double precision,
	costo_final bytea,
	numero_habitacion integer REFERENCES habitacion(numero),
	id_persona_fisica integer REFERENCES persona_fisica(id_persona_fisica),
	numero_factura integer REFERENCES factura(numero)
);

CREATE TABLE servicio_prestado(
	id_servicio serial PRIMARY KEY,
	nombre varchar(30),
	precio bytea,
	cantidad integer,
	fecha date,
	tipo varchar(30),
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
	nombre varchar(30) PRIMARY KEY,
	precio_unitario bytea,
	cantidad integer,
	precio_total bytea,
	id_servicio integer REFERENCES servicio_prestado(id_servicio),
	numero_factura integer REFERENCES factura(numero)
);

CREATE TABLE efectivo(
	id_medio_de_pago serial PRIMARY KEY,
	importe bytea,
	cotizacion double precision,
	importe_en_pesos bytea,
	id_pago integer REFERENCES pago(id_pago)
);


CREATE TABLE tarjeta_debito(
	id_medio_de_pago serial PRIMARY KEY,
	numero_tarjeta integer,
	fecha_vencimiento date,
	nombre varchar(30),
	codigo_seguridad integer,
	importe bytea,
	cotizacion double precision,
	importe_en_pesos bytea,
	id_pago integer REFERENCES pago(id_pago)
);

CREATE TABLE tarjeta_credito(
	id_medio_de_pago serial PRIMARY KEY,
	numero_tarjeta integer,
	fecha_vencimiento date,
	nombre varchar(30),
	codigo_seguridad integer,
	importe bytea,
	cotizacion double precision,
	importe_en_pesos bytea,
	id_pago integer REFERENCES pago(id_pago)
);

CREATE TABLE cheque(
	id_medio_de_pago serial PRIMARY KEY,
	numero_cheque integer,
	fecha_cobro date,
	importe bytea,
	cotizacion double precision,
	importe_en_pesos bytea,
	id_pago integer REFERENCES pago(id_pago),
	id_banco integer REFERENCES banco(id_banco)
);

CREATE TABLE usuario(
	usuario varchar(30) PRIMARY KEY,
	contraseña varchar(30)
);

/*
DROP TABLE periodo_reserva, asociada_a, efectivo, estadia, factura, habitacion, nota_de_credito, pago, pasajero, 
		   persona_juridica, plaza, reserva, servicio_facturado, servicio_prestado, tarjeta_credito,
		   tarjeta_debito, tipo_habitacion, usuario, cheque, banco, cheque;
*/

INSERT INTO habitacion VALUES
(DEFAULT,'DISPONIBLE',1,'INDIVIDUAL ESTÁNDAR'),
(DEFAULT,'DISPONIBLE',1,'INDIVIDUAL ESTÁNDAR'),
(DEFAULT,'DISPONIBLE',2,'DOBLE SUPERIOR');

INSERT INTO tipo_habitacion VALUES
('INDIVIDUAL ESTANDAR', null),
('DOBLE SUPERIOR', null);

UPDATE estadia SET costo_noche_divisa = 'ARS' WHERE id_estadia = 1;

UPDATE estadia SET costo_noche_monto = 4200 WHERE id_estadia = 1;

INSERT INTO habitacion VALUES
(DEFAULT,'DISPONIBLE',2,'SUITE DOUBLE'),
(DEFAULT,'DISPONIBLE',2,'DOUBLE SUPERIOR');

DELETE FROM tipo_habitacion WHERE nombre = 'SUITE DOBLE';

DELETE FROM habitacion WHERE numero = 10;

UPDATE tipo_habitacion SET precio_actual_divisa = 'USD' WHERE nombre = 'INDIVIDUAL ESTANDAR';

ALTER TABLE tipo_habitacion ALTER COLUMN precio_actual TYPE dinero;

CREATE TYPE dinero AS (MY_CURRENCY VARCHAR(255), MY_AMOUNT bytea);

--Otros datos a cargar
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
(7,'FUERA_DE_SERVICIO',5,'SUPERIOR FAMILY PLAN');

INSERT INTO estadia VALUES 
(1,'2021-12-15','2021-12-20',null,null,null,4,3,null),
(2,'2021-12-15','2021-12-20',null,null,null,5,3,null),
(3,'2021-12-15','2021-12-20',null,null,null,6,3,null),
(4,'2021-12-15','2021-12-20',null,null,null,7,3,null);

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


