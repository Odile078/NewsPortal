* CREATE DATABASE companynews;



* CREATE TABLE departments (id serial PRIMARY KEY,name varchar, description varchar);CREATE TABLE

* CREATE TABLE employees(id serial PRIMARY KEY,name varchar, position varchar, role varchar,department_id int);

* CREATE TABLE employees_departments(id serial PRIMARY KEY,employee_id int, department_id int);

* CREATE TABLE generalnews(id serial PRIMARY KEY,title varchar, writtenby varchar, content varchar,createdat timestamp, employee_id int);

* CREATE TABLE employees_generalnews(id serial PRIMARY KEY, employee_id int,generalnews_id int);

* CREATE TABLE departmentnews(id serial PRIMARY KEY,title varchar, writtenby varchar, content varchar,createdat timestamp, employee_id int,department_id int);

* CREATE TABLE employees_departmentnews(id serial PRIMARY KEY, employee_id int,departmentnews_id int);

* CREATE TABLE departments_departmentnews(id serial PRIMARY KEY, department_id int,departmentnews_id int);