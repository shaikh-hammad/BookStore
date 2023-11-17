INSERT INTO book(title, author, price, description)
VALUES ('Java', 'Author', 50.0, 'CS'),
       ('Eragon', 'Christopher Paolini', 20.0, 'Fantasy'),
       ('The Secret of the Old Clock', 'Carolyn Keene', 15.0, 'Crime'),
       ('Heads, You Lose!', 'R.L Stine', 25.0, 'Horror');

INSERT INTO sec_user (email, encryptedPassword, enabled)
VALUES ('fahad.jan@sheridancollege.ca',
        '$2a$10$1ltibqiyyBJMJQ4hqM7f0OusP6np/IHshkYc4TjedwHnwwNChQZCy', 1);

INSERT INTO sec_user (email, encryptedPassword, enabled)
VALUES ('frank@sheridancollege.ca',
        '$2a$10$PrI5Gk9L.tSZiW9FXhTS8O8Mz9E97k2FZbFvGFFaSsiTUIl.TCrFu', 1);

INSERT INTO sec_role (roleName)
VALUES ('ROLE_USER');
INSERT INTO sec_role (roleName)
VALUES ('ROLE_GUEST');
INSERT INTO sec_role (roleName)
VALUES ('ROLE_ADMIN');
INSERT INTO user_role (userId, roleId)
VALUES (1, 3);
INSERT INTO user_role (userId, roleId)
VALUES (1, 1);
INSERT INTO user_role (userId, roleId)
VALUES (2, 1);