CREATE TABLE Hackers (
  ID INT NOT NULL PRIMARY KEY,
  Name VARCHAR(50) NOT NULL
);

CREATE TABLE Submissions (
  ID INT NOT NULL PRIMARY KEY,
  Hacker_ID INT NOT NULL,
  Challenge_ID INT NOT NULL,
  Score INT NOT NULL,
  FOREIGN KEY (Hacker_ID) REFERENCES Hackers(ID)
);

INSERT INTO Hackers VALUES (1, 'Иван');
INSERT INTO Hackers VALUES (2, 'Мария');
INSERT INTO Hackers VALUES (3, 'Дмитрий');
INSERT INTO Hackers VALUES (4, 'Сергей');
INSERT INTO Hackers VALUES (5, 'Юлия');
INSERT INTO Hackers VALUES (6, 'Валентина');
INSERT INTO Hackers VALUES (7, 'Михаил');
INSERT INTO Hackers VALUES (8, 'Андрей');
INSERT INTO Hackers VALUES (9, 'Николай');
INSERT INTO Hackers VALUES (10, 'Денис');

INSERT INTO Submissions VALUES (1, 1, 2, 91);
INSERT INTO Submissions VALUES (2, 1, 3, 73);
INSERT INTO Submissions VALUES (3, 1, 3, 86);
INSERT INTO Submissions VALUES (4, 2, 3, 57);
INSERT INTO Submissions VALUES (5, 2, 3, 12);
INSERT INTO Submissions VALUES (6, 2, 5, 23);
INSERT INTO Submissions VALUES (7, 3, 2, 3);
INSERT INTO Submissions VALUES (8, 3, 2, 35);
INSERT INTO Submissions VALUES (9, 3, 3, 15);
INSERT INTO Submissions VALUES (10, 3, 4, 50);
INSERT INTO Submissions VALUES (11, 3, 4, 27);
INSERT INTO Submissions VALUES (12, 4, 3, 19);
INSERT INTO Submissions VALUES (13, 4, 5, 43);
INSERT INTO Submissions VALUES (14, 5, 1, 77);
INSERT INTO Submissions VALUES (15, 5, 2, 6);
INSERT INTO Submissions VALUES (16, 5, 2, 99);
INSERT INTO Submissions VALUES (17, 6, 2, 29);
INSERT INTO Submissions VALUES (18, 6, 3, 84);
INSERT INTO Submissions VALUES (19, 6, 3, 83);
INSERT INTO Submissions VALUES (20, 7, 1, 0);
INSERT INTO Submissions VALUES (21, 7, 3, 100);

/*
SELECT h.id, name, SUM(max_score) total_score FROM
  (
    SELECT hacker_id, MAX(score) max_score FROM Submissions
    GROUP BY hacker_id, challenge_id
  ) sub
    JOIN Hackers h
         ON sub.hacker_id = h.id
GROUP BY h.id, name
HAVING SUM(max_score) > 0
ORDER BY total_score DESC, name;
*/