CREATE DATABSE social-media-quarkus;

CREATE TABLE users(
	id bigserial not null primary key,
	name varchar(100) not null,
	age integer not null
);

CREATE TABLE posts(
	id bigserial not null primary key,
	text_post varchar(150) not null,
	dateTimePost timestamp not null,
	user_id bigint references users(id)
);

CREATE TABLE followers(
    id bigserial not null primary key,
    user_id bigint not null references users(id),
    follower_id bigint not null references users(id)
);
