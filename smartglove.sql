create table users(
id int not null auto_increment,
nome varchar(20),
peso int,
email varchar(40) UNIQUE,
senha varchar(30),
esporte varchar(200),
primary key(id)
);

create table treino(
id_treino int not null auto_increment,
tempo varchar(10),
data varchar(20),
titulo varchar(20),
forca varchar(150),
velocity varchar(150),
fk_id_user int,
primary key(id_treino),
FOREIGN key(fk_id_user)references users(id)
);
