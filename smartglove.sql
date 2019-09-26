create table users(
id_user int not null auto_increment,
nome varchar(50),
data_nasc varchar(10),
sexo char(1),
email varchar(80),
senha varchar(50),
nome_esporte varchar(200),
primary key(id_user)
);

create table treino(
id_treino int not null auto_increment,
tempo time,
dado_acelerometro int,
primary key(id_treino)
);