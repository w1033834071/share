drop table if exists question;
create table question (
	id int not null auto_increment,
    title varchar(255) not null,
    content text not null,
    userId int not null,
    createdDate datetime not null,
    commentCount int not null,
    primary key(id),
    index date_index (createdDate asc)
);


drop table if exists user;
create table user(
	id int(11) unsigned not null auto_increment,
    name varchar(64) not null default '',
    password varchar(128) not null default '',
    salt varchar(64) not null default '',
    head_url varchar(255) not null default '',
    primary key(id),
    unique key name(name)
)engine = InnoDB default charset = utf8



drop table if exists login_ticket;
create table login_ticket(
    id int not null auto_increment,
    userId int not null,
    ticket varchar(50) not null,
    expired datetime not null,
    status int null default 0,
    primary key(id),
    unique index ticket_UNIQUE (ticket ASC)
)engine=InnoDB default charset=utf8