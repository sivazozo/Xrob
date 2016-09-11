DROP TABLE IF EXISTS contacts;
CREATE TABLE contacts(
	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	name VARCHAR(30)
);

DROP TABLE IF EXISTS phone_numbers;
CREATE TABLE phone_numbers(
	id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	contact_id INTEGER NOT NULL,
	phone_number VARCHAR(20) NOT NULL,
	phone_type VARCHAR(10) NOT NULL,
	is_synced INTEGER CHECK(is_synced IN (0,1)) NOT NULL DEFAULT 0,
	FOREIGN KEY (contact_id) REFERENCES contacts (id) ON UPDATE CASCADE ON DELETE CASCADE
);