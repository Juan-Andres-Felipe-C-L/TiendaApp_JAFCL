CREATE DATABASE tienda_db;
USE tienda_db;
 
CREATE TABLE roles(
  id int primary key not null auto_increment,
  name varchar(50) not null
);
 
CREATE TABLE users(
  id int primary key not null auto_increment,
  username varchar(50) not null unique,
  email varchar(255) not null unique,
  password varchar(255) not null,
  active boolean not null default true,
  rol_id int not null,
  foreign key (rol_id) references roles (id)
);
 
CREATE TABLE categories(
  id int primary key not null auto_increment,
  name varchar(100) not null unique
);
 
CREATE TABLE products(
  id int primary key not null auto_increment,
  name varchar(150) not null,
  description varchar(500),
  price decimal(12,2) not null,
  stock int not null,
  category_id int not null,
  seller_id int not null,
  foreign key (category_id) references categories (id),
  foreign key (seller_id) references users (id)
);
 
CREATE TABLE orders(
  id int primary key not null auto_increment,
  client_id int not null,
  product_id int not null,
  quantity int not null,
  total decimal(12,2) not null,
  status varchar(20) not null default 'PENDIENTE',
  created_at datetime not null default current_timestamp,
  foreign key (client_id) references users (id),
  foreign key (product_id) references products (id)
);

CREATE TABLE order_products (
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);

-- Se pone sentencia para poder actualizar la información de las tablas, apesar de usar relaciones de Foreign Keys: 
set sql_safe_updates = 0;
 
INSERT INTO roles (name) VALUES ('ADMIN'), ('CLIENTE'), ('VENDEDOR');

-- Se inserta el ADMIN directamente en la base de datos, con contraseña encriptada por Bcrypt:
Insert into users (username, email, password, rol_id) values('Michael', 'maikol@sena.edu.co', '$2a$10$VU4Av7oFScu6L.UOtGOX1uKAGkq3dUlo74RoxACBZmnf9SdExXeky', 1);