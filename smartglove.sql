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
tempo time,
dado_acelerometro int,
primary key(id_treino)
);
