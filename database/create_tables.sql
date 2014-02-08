/* This script creates the tables for the Parcels on the Move DB - Year Project 2014
Aurélien Plisnier 
---------------------------------------------------------
*/

DROP TABLE IF EXISTS Parcel_Status;
DROP TABLE IF EXISTS Parcel;
DROP TABLE IF EXISTS Order_Status;
DROP TABLE IF EXISTS Orders;
DROP TABLE IF EXISTS Client;
DROP TABLE IF EXISTS User;

CREATE TABLE User (
	user_id int(8) NOT NULL PRIMARY KEY AUTO_INCREMENT,
	first_name varchar(20) NOT NULL,
	last_name varchar(20) NOT NULL,
	role int(1) NOT NULL,
	password varchar(20) NOT NULL,
	is_active int(1) NOT NULL default '0',
	time_stp timestamp default current_timestamp
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Client (
	client_id int(8) NOT NULL,
	country varchar(20) NOT NULL,
	contact_name varchar(40) NOT NULL,
	company_name varchar(20) NOT NULL,
	address_street varchar(20) NOT NULL,
	address_number int(8) NOT NULL,
	postal_code int(8) NOT NULL,
	city varchar(20) NOT NULL,
	region varchar(20),
	state_department varchar(20),
	tel int(20) NOT NULL,
	fax int(20),
	email varchar(30),
	TVA_number int(20) NOT NULL,
	time_stp timestamp default current_timestamp,
	from_date DATETIME default current_timestamp,
	to_date DATETIME default '9999-12-31 23:59:59',
	PRIMARY KEY (client_id,from_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Orders (
	order_id int(8) NOT NULL,
	sender_id int(8) NOT NULL,
	receiver_id int(8) NOT NULL,
	billing_currency varchar(20) NOT NULL,
	billing_value float NOT NULL,
	collection_date timestamp NOT NULL,
	time_stp timestamp default current_timestamp,
	from_date DATETIME default current_timestamp,
	to_date DATETIME default '9999-12-31 23:59:59',
	PRIMARY KEY (order_id,from_date),
	FOREIGN KEY (sender_id) REFERENCES Client(client_id),
	FOREIGN KEY (receiver_id) REFERENCES Client(client_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Order_Status (
	order_id int(8) NOT NULL,
	status varchar(20) NOT NULL,
	from_date DATETIME  default current_timestamp,
	to_date DATETIME  default '9999-12-31 23:59:59',
	PRIMARY KEY (order_id,from_date),
	FOREIGN KEY (order_id) REFERENCES Orders(order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Parcel (
	parcel_id int(8) NOT NULL,
	order_id int(8) NOT NULL,
	scan_code int(20) NOT NULL,
	scan_code_type varchar(10) NOT NULL,
	mass_kg float NOT NULL,
	lenght_cm float NOT NULL,
	height_cm float NOT NULL,
	width_cm float NOT NULL,
	content varchar(20),
	description varchar(200),
	declared_value_currency varchar(20),
	declared_value float,
	time_stp timestamp default current_timestamp,
	from_date DATETIME  default current_timestamp,
	to_date DATETIME  default '9999-12-31 23:59:59',
	PRIMARY KEY (parcel_id,from_date),
	FOREIGN KEY (order_id) REFERENCES Orders(order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Parcel_Status (
	parcel_id int(8) NOT NULL,
	status varchar(20) NOT NULL,
	from_date DATETIME  default current_timestamp,
	to_date DATETIME  default '9999-12-31 23:59:59',
	PRIMARY KEY (parcel_id,from_date),
	FOREIGN KEY (parcel_id) REFERENCES Parcel(parcel_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
