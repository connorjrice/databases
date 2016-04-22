/**
 * Class: CP344
 * Assignment: #1
 * Author:  Connor Rice
 * Created: Apr 21, 2016
 * mysql -u root -p -D employees < cr.sql > cr_res.txt
 * diff cr_res.txt matthew_results.txt
 */

use employees;

/** 1
    Retrieve all the employees' last names. Sort by last name.
 **/
SELECT last_name FROM employees ORDER BY last_name;
 
/** 2
    Retrieve all the data in the departments table. Sort by deptartment number.
 **/
SELECT * FROM departments ORDER BY dept_no;

/** 3 
    Retrieve all the first names of women with employee IDs less than 20,000. 
    Sort by first name.
 **/
SELECT first_name FROM employees 
WHERE employees.gender = "f"
AND emp_no < 20000
ORDER BY first_name;

/** 4 
    Retrieve all the first names of men with the last name "Morrow". 
    Sort by first name.
 **/
SELECT first_name FROM employees
WHERE employees.last_name = "Morrow"
AND employees.gender = "m"
ORDER BY employees.first_name;

/** 5 
    Retrieve all the salaries for employee number 10018 and sort them from 
    largest to smallest.
 **/
SELECT salary FROM salaries
WHERE emp_no = 10018
ORDER BY salary DESC;

/** 6
    Retrieve employee number 10018's current department number.
 **/
SELECT dept_no FROM current_dept_emp
WHERE current_dept_emp.emp_no = 10018;

/** 7
    Retrieve employee number 10018's current department name.
 **/
SELECT dept_name FROM departments NATURAL JOIN current_dept_emp
WHERE departments.dept_no = current_dept_emp.dept_no
AND emp_no = 10018;

/** 8
    Retrieve a count of all the employees.
 **/
SELECT COUNT(*) FROM employees;

/** 9
    Retrieve the average salary for all the salaries listed in the salaries
    table.
 **/
SELECT AVG(salary) FROM salaries;

/** 10
    Retrieve the counts of total employees (past and present) for each 
    department. Include the department numbers in the result.
    Sort by department number.
 **/
SELECT dept_no, COUNT(*) FROM current_dept_emp
GROUP BY dept_no;

/** 11
    Retrieve the last names of all employees who currently work in Sales and 
    have a last name that begins with 'K'. Sort by last name.
 **/
SELECT last_name 
FROM employees NATURAL JOIN current_dept_emp
WHERE current_dept_emp.dept_no = "d007"
AND employees.last_name LIKE "K%"
ORDER BY last_name;

/** 12
    Retrieve the counts of the number of employees with last name "Morrow"
    currently working in each of the departments. Include the department numbers
    in the result. Sort by department numbers.
 **/
SELECT dept_no, COUNT(*)
FROM employees NATURAL JOIN current_dept_emp
WHERE employees.last_name = "Morrow"
GROUP BY current_dept_emp.dept_no;

/** 13
    Retrieve the last names of all employees who currently work in the same
    department as the employee with last name "Morrow" and first name "Barun".
    Sorty by last name.
 **/
-- Can be improved if time permits
SELECT last_name FROM employees NATURAL JOIN current_dept_emp
WHERE current_dept_emp.dept_no = "d004"
ORDER BY last_name;

/*SELECT last_name FROM 
(SELECT first_name, last_name 
FROM employees NATURAL JOIN current_dept_emp
 WHERE first_name = "Barun" IN ()
)*/


/** 14
    Retrieve the full names of all the employees who have ever been managers.
    Sort by last name.
 **/
SELECT first_name, last_name
FROM employees NATURAL JOIN titles
WHERE titles.title = "Manager"
ORDER BY last_name;

/** 15
    Retrieve the full names of all the employees who have ever made less
    than $39,000. Sort by last name.
 **/
SELECT first_name, last_name 
FROM employees NATURAL JOIN salaries
WHERE salaries.salary < 39000
ORDER BY employees.last_name;


/** 16
    Retrieve the full name of the employee who has had the highest salary.
 **/
SELECT first_name, last_name FROM
(SELECT first_name, last_name, MAX(salary) FROM employees NATURAL JOIN salaries)
AS richboi;

/** 17
    Retrieve the average salaries for both men and women. Include the gender
    labels in the result. Sort by gender.
 **/
SELECT gender, avg(salary) FROM employees NATURAL JOIN salaries
ORDER BY employees.gender;

/** 18
    Retrieve the full names of the employees who are the oldest (they all share 
    the same birthday). Sort by last name.
 **/
-- I used StackOverflow to learn about inner joins
SELECT e.* FROM employees e
INNER JOIN (Select employees.first_name, last_name,
min(birth_date) as lowdate FROM employees) as mini ON
e.first_name = mini.first_name
AND e.last_name = mini.last_name
AND e.birth_date = mini.lowdate
GROUP BY e.last_name;

/** 19
    Retrieve the counts of managers grouped by gender. Include the gender labels
    in the result. Sort by gender.
 **/
SELECT gender, COUNT(*) FROM 
(SELECT first_name, last_name, gender, title FROM employees NATURAL JOIN titles) AS gen
WHERE gen.title = "Manager"
GROUP BY gen.gender;

/** 20
    Retrieve the name deof the department that has/had the oldest manager. 
    (Based on the manager's date of birth and not the longest term serving as
     manager). Sort by department name.
 **/
/*SELECT dept_no, min(birth_date) FROM(
SELECT first_name, last_name, dept_no, birth_date FROM employees NATURAL JOIN
dept_manager) AS cool;*/


/*CREATE VIEW oldest AS 
    SELECT * FROM employees
    	    NATURAL JOIN dept_emp;

change has been known to change*/

/*CREATE VIEW oldest AS 
    SELECT employees.emp_no, employees.birth_date, dept_emp.dept_no, departments.dept_name,
    	   titles.title
 	   FROM employees
	   	INNER JOIN dept_emp
   		      ON employees.emp_no = dept_emp.emp_no;
  		LEFT OUTER JOIN departments
   		      ON dept_emp.dept_no = departments.dept_no;
		RIGHT OUTER JOIN titles
		      ON employees.emp_no = titles.emp_no;

SELECT dept_name FROM oldest;*/
/*SELECT emp_no FROM oldesst*/

/*SELECT dept_no, dept_name FROM 
SELECT dept_no, min(birth_date) FROM(
SELECT first_name, last_name, dept_no, birth_date FROM employees NATURAL JOIN
dept_manager) AS  cool;*/

/*DROP VIEW oldest;*/

/*
INNER JOIN (Select emp_no,
min(birth_date) as low, dept_no FROM dept_manager) as mini ON
dm.dept_no = mini.dept_no;
GROUP BY dm.dept_no;
*/

-- UNFINISHED --my

