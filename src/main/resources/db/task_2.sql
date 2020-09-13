CREATE TABLE Grades (
  Grade INT NOT NULL PRIMARY KEY,
  Min_mark INT NOT NULL,
  Max_mark INT NOT NULL
);

CREATE TABLE Students (
  ID INT NOT NULL PRIMARY KEY,
  Name VARCHAR(50) NOT NULL,
  Marks INT NOT NULL
);

INSERT INTO Grades VALUES (1, 0, 9);
INSERT INTO Grades VALUES (2, 10, 19);
INSERT INTO Grades VALUES (3, 20, 29);
INSERT INTO Grades VALUES (4, 30, 39);
INSERT INTO Grades VALUES (5, 40, 49);
INSERT INTO Grades VALUES (6, 50, 59);
INSERT INTO Grades VALUES (7, 60, 69);
INSERT INTO Grades VALUES (8, 70, 79);
INSERT INTO Grades VALUES (9, 80, 89);
INSERT INTO Grades VALUES (10, 90, 99);

INSERT INTO Students VALUES (1, 'Иван', 88);
INSERT INTO Students VALUES (2, 'Дмитрий', 68);
INSERT INTO Students VALUES (3, 'Сергей', 99);
INSERT INTO Students VALUES (4, 'Михаил', 78);
INSERT INTO Students VALUES (5, 'Андрей', 63);
INSERT INTO Students VALUES (6, 'Николай', 81);

/*
SELECT name, grade, marks FROM Students
JOIN Grades
ON min_mark <= marks AND marks <= max_mark
WHERE grade > 7
UNION
SELECT NULL, grade, marks FROM Students
JOIN Grades
ON min_mark <= marks AND marks <= max_mark
WHERE grade < 8
ORDER BY grade DESC, marks;
 */