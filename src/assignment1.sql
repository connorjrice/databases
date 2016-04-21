/**
 * Class: CP344
 * Assignment: #1
 * Author:  Connor Rice
 * Created: Apr 21, 2016
 */

/** 1
    Retrieve all the employees' last names. Sort by last name.
 **/
SELECT last_name FROM employees GROUP BY last_name;

/** 2
    Retrieve all the data in the departments table. Sort by deptartment number.
 **/
SELECT * FROM departments GROUP BY dept_no;

/** 3 
    Retrieve all the first names of women with employee IDs less than 20,000. 
    Sort by first name.
 **/
SELECT first_name FROM employees 
WHERE employees.gender = "f"
AND emp_no < 20000;


/** 4 
    Retrieve all the first names of men with the last name "Morrow". 
    Sort by first name.
 **/
SELECT first_name FROM employees
WHERE employees.last_name = "Morrow"
GROUP BY first_name;


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
WHERE emp_no = 10018;

/** 7
    Retrieve employee number 10018's current department name.
 **/
SELECT dept_name FROM departments, current_dept_emp
WHERE departments.dept_no = current_dept_emp.dept_no
AND emp_no = 10018;


/** 8
    Retrieve a count of all the employees.
 **/
SELECT COUNT(emp_no) FROM employees;

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
SELECT dept_no, COUNT(emp_no) FROM current_dept_emp
GROUP BY dept_no;

/** 11
    Retrieve the last names of all employees who currently work in Sales and 
    have a last name that begins with 'K'. Sort by last name.
 **/
SELECT last_name FROM employees NATURAL JOIN current_dept_emp
WHERE current_dept_emp.dept_no = "d007"
AND employees.last_name LIKE "K%"
GROUP BY last_name;

/** 12
    Retrieve the counts of the number of employees with last name "Morrow"
    currently working in each of the departments. Include the department numbers
    in the result. Sort by department numbers.
 **/


/** 13
    Retrieve the last names of all employees who currently work in the same
    department as the employee with last name "Morrow" and first name "Barun".
    Sorty by last name.
 **/


/** 14
    Retrieve the full names of all the employees who have ever been managers.
    Sort by last name.
 **/


/** 15
    Retrieve the full names of all the employees who have ever made less
    than $39,000. Sort by last name.
 **/


/** 16
    Retrieve the full name of the employee who has had the highest salary.
 **/


/** 17
    Retrieve the average salaries for both men and women. Include the gender
    labels in the result. Sort by gender.
 **/


/** 18
    Retrieve the full names of the employees who are the oldest (they all share 
    the same birthday). Sort by last name.
 **/


/** 19
    Retrieve the counts of managers grouped by gender. Include the gender labels
    in the result. Sort by gender.
 **/


/** 20
    Retrieve the name of the department that has/had the oldest manager. 
    (Based on the manager's date of birth and not the longest term serving as
     manager). Sort by department name.
 **/


