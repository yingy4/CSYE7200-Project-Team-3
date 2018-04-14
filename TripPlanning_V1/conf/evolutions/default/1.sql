
# --- !Ups
create table user (
  id                        bigint not null AUTO_INCREMENT,
  name                      varchar(255) not null,
  password                  varchar(225) not null,
  email                     varchar(225) not null,
  primary key (id)
);
insert into user (name, password,email) values ( 'admin','admin','zhengliu1217@gmail.com');

create table place (
  user                          varchar(255) not null,
  place_id                      varchar(1000) not null,
  category                      varchar(255) not null,
  name                          varchar(225) not null,
  addres                        varchar(1000) not null,
  favoriate                     varchar(200) not null,
  locaitn                       varchar(225) not null,,
  primary key (user,place_id)
);

# --- !Downs
drop table if exists user;
drop table if exists place;




