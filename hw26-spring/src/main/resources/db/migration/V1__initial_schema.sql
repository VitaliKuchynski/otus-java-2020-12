create table client (
        id bigserial not null primary key,
        name varchar(50),
        address_id varchar(50) ,
        phone_id varchar(50)
);

create table phone (
    id bigserial not null primary key,
    number varchar(50),
    client_id bigint not null references client (id)
);

create table address (
    id bigserial not null primary key,
    street varchar(50),
    client_id bigint not null references client (id)
);


create index idx_client_phone_id on phone(client_id);
create index idx_client_address_id on address(client_id);

