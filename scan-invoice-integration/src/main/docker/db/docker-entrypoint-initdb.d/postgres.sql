create schema if not exists scaninvoice;

create table if not exists scaninvoice.transaction (
    url text,
    ynab_transaction text,
    nfc_contents xml,
    processed boolean,
    update_date date,
    create_date date,
    primary key (URL));