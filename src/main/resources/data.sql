insert into book(id, isbn, name, author, book_category, price, pages, description, stock, img_url)
VALUES (1, '978-3-16-148410-0', 'Gone Girl', 'Gillian Flynn', 'MYSTERY', 49.99, 312,
        'Gone Girl by Gillian Flynn centers its story about Nick and Amy Dunne''s strained marriage relationship. Nick used to work as a journalist, but loses his job. With his broke financial status, Nick decides to relocate from New York City to his smaller home town, North Carthage.',
        100, '/img/gone-girl.jpg'),
       (2, '481-8-34-116410-0', 'Dilemma', 'B. A. Paris', 'MEMOIR', 27.41, 352,
        'The Dilemma is a novel bearing the features of a thriller with an in-depth psychology of the characters. Livia is a mature woman, for years with her husband, Adam, with whom, despite her parents, she became a teenager.',
        100, '/img/dilemma.jpg'),
       (3, '778-3-16-14510-0', 'The witcher. Volume 1. The Last Wish', 'Andrzej Sapkowski', 'HORROR', 27.49, 332,
        'Geralt is a witcher, that is, someone who deals with murdering various beasts for money. Interestingly, it would be really difficult to call him an ordinary person, just as he is never and never will be an average witcher.',
        50, '/img/witcher1.jpg'),
       (4, '122-3-16-98710-0', 'The witcher. Volume 2. The sword of destiny', 'Andrzej Sapkowski', 'HORROR', 31.68, 400,
        'The second volume of the popular series about The Witcher, a monster slayer who will stop at nothing. What adventure will he meet this time? Geralt does not have an easy life, despite the fact that he is perfectly trained in what he does and has an undeniable talent',
        75, '/img/witcher2.jpg'),
       (5, '455-9-22-59870-0', 'The witcher. Volume 3. Blood of the Elves', 'Andrzej Sapkowski', 'HORROR', 31.70, 339,
        'Blood of the Elves "is the first of the five parts of the saga about the Witcher Geralt. The king of Polish fantasy, Andrzej Sapkowski, treats readers with great and engaging literature at the highest level.',
        50, '/img/witcher3.jpg'),
       (6, '222-1-88-542310-0', 'Macbeth', 'William Shakespeare', 'MYSTERY', 5.78, 144,
        'Macbeth edition complete without abbreviations and cuts in content. In this issue you will find answers to the questions from the textbook - "sure thing on the test", that is, an indication of the issues that usually appear in questions from a given reading in all knowledge tests, as well as in textbooks and tests.',
        100, '/img/macbeth.jpg');


insert into APP_USER(id, firstname, lastname, email, password, is_enabled)
VALUES (1, 'Sina', 'Mousavi', 'admin', '{bcrypt}$2a$12$.qv5C1te.SApmRF4Kp9P1uFJfE6rK0sBQrwiHJ3/U7V8cmtgxh4Mi', true),
       (2, 'Mohammadreza', 'Afshari', 'mohammadreza@gmail.com',
        '{bcrypt}$2a$12$.qv5C1te.SApmRF4Kp9P1uFJfE6rK0sBQrwiHJ3/U7V8cmtgxh4Mi', true),
       (3, 'Javad', 'Taheri', 'javad@gmail.com',
        '{bcrypt}$2a$12$.qv5C1te.SApmRF4Kp9P1uFJfE6rK0sBQrwiHJ3/U7V8cmtgxh4Mi', true),
       (4, 'Zahra', 'Babazade', 'zahra@gmail.com',
        '{bcrypt}$2a$12$.qv5C1te.SApmRF4Kp9P1uFJfE6rK0sBQrwiHJ3/U7V8cmtgxh4Mi', true),
       (5, 'Elahe', 'Mohseni', 'elahe@gmail.com', '{bcrypt}$2a$12$.qv5C1te.SApmRF4Kp9P1uFJfE6rK0sBQrwiHJ3/U7V8cmtgxh4Mi',
        true);

insert into APP_USER_Role(role, description)
VALUES ('ROLE_USER', 'default role for user');
insert into APP_USER_Role(role, description)
VALUES ('ROLE_ADMIN', 'admin role');
insert into APP_USER_roles(app_user_id, roles_id)
VALUES (1, 2);
insert into APP_USER_roles(app_user_id, roles_id)
VALUES (2, 1);
insert into APP_USER_roles(app_user_id, roles_id)
VALUES (3, 1);
insert into APP_USER_roles(app_user_id, roles_id)
VALUES (4, 1);
insert into APP_USER_roles(app_user_id, roles_id)
VALUES (5, 1);

-- alter sequence app_user_pkey_seq restart with 18;
