-- ----------------------------------------------
-- add few categories
INSERT INTO post (id, author, title, text, created_date, published_date) VALUES (1, 'Michal', 'Title1', 'Text1', STR_TO_DATE('21-03-2008 05:31:55', '%d-%m-%Y %H:%i:%s'), STR_TO_DATE('21-03-2008 05:31:55', '%d-%m-%Y %H:%i:%s'));
INSERT INTO post (id, author, title, text, created_date, published_date) VALUES (2, 'Michal', 'Title2', 'Text2', STR_TO_DATE('22-03-2007 05:31:55', '%d-%m-%Y %H:%i:%s'), STR_TO_DATE('22-03-2007 06:31:55', '%d-%m-%Y %H:%i:%s'));
INSERT INTO post (id, author, title, text, created_date, published_date) VALUES (3, 'Radek', 'Title3', 'Text3', STR_TO_DATE('23-03-2009 05:31:55', '%d-%m-%Y %H:%i:%s'), STR_TO_DATE('24-03-2009 06:31:55', '%d-%m-%Y %H:%i:%s'));
INSERT INTO post (id, author, title, text, created_date) VALUES (4, 'Radek', 'Title4', 'Text4', STR_TO_DATE('21-03-2007 05:31:55', '%d-%m-%Y %H:%i:%s'));
INSERT INTO post (id, author, title, text, created_date) VALUES (5, 'Weronika', 'Title5', 'Text5', STR_TO_DATE('21-03-2007 05:31:55', '%d-%m-%Y %H:%i:%s'));