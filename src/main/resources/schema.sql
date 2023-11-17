-- CREATE TABLE users(
--                      username VARCHAR(255),
--                      password VARCHAR(255),
--                      email VARCHAR(255)
-- );
CREATE TABLE book(
                            isbn LONG PRIMARY KEY AUTO_INCREMENT,
                            title VARCHAR(255),
                            author VARCHAR(255),
                            price DECIMAL(10, 2),
                            description VARCHAR(255)
);
CREATE TABLE book_order(
                     id LONG PRIMARY KEY AUTO_INCREMENT,
                     isbn LONG,
                     quantity INT,
                     owner VARCHAR(255),
                     total DECIMAL
);
CREATE TABLE sec_user (
                          userId BIGINT NOT NULL PRIMARY KEY
                              AUTO_INCREMENT,
                          email VARCHAR(75) NOT NULL UNIQUE,
                          encryptedPassword VARCHAR(128) NOT NULL,
                          enabled BIT NOT NULL
);
CREATE TABLE sec_role(
                         roleId BIGINT NOT NULL PRIMARY KEY
                             AUTO_INCREMENT,
                         roleName VARCHAR(30) NOT NULL UNIQUE
);
CREATE TABLE user_role
(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    userId BIGINT NOT NULL,
    roleId BIGINT NOT NULL
);
ALTER TABLE user_role
    ADD CONSTRAINT user_role_uk UNIQUE (userId, roleId);
ALTER TABLE user_role
    ADD CONSTRAINT user_role_fk1 FOREIGN KEY (userId)
        REFERENCES sec_user (userId);
ALTER TABLE user_role
    ADD CONSTRAINT user_role_fk2 FOREIGN KEY (roleId)
        REFERENCES sec_role (roleId);
