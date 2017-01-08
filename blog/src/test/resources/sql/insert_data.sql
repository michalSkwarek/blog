-- ----------------------------------------------
-- add few users
INSERT INTO user (id, username, password, enabled, role) VALUES (1, 'user1', 'pass1', TRUE, 'ROLE_ADMIN');
INSERT INTO user (id, username, password, enabled, role) VALUES (2, 'user2', 'pass2', TRUE, 'ROLE_ADMIN');

-- add few posts
INSERT INTO post (id, author_id, title, text, created_date, published_date) VALUES (1, 1, 'title1', 'text1', TIMESTAMP '2000-01-11 11:22:33', TIMESTAMP '2000-01-12 22:33:44');
INSERT INTO post (id, author_id, title, text, created_date, published_date) VALUES (2, 2, 'title2', 'text2', TIMESTAMP '2000-01-11 11:22:33', TIMESTAMP '2000-01-12 22:33:44');
INSERT INTO post (id, author_id, title, text, created_date, published_date) VALUES (3, 1, 'title3', 'text3', TIMESTAMP '2000-01-11 11:22:33', NULL);

-- add few comments
INSERT INTO comment (id, author, text, created_date, approved_comment, post_id) VALUES (1, 'author1', 'commentText1', TIMESTAMP '2000-01-11 11:22:33', TRUE, 1);
INSERT INTO comment (id, author, text, created_date, approved_comment, post_id) VALUES (2, 'author2', 'commentText2', TIMESTAMP '2000-01-11 11:22:33', FALSE, 1);