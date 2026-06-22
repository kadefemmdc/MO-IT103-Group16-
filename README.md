# MO-IT103-Group16

Date Uploaded: 6/16/2026

Milestone 2: MotorPH Employee App – Record Management, Salary Computation, and Data Update

## Team Details

Katrina Anne Defe

Samantha Loraine Tadina

Geneveve Apostol

Gjoshan Agnon

Joy Mae Pedoy

## Program Details

The MotorPH Employee Management System is a Java-based desktop application that uses CSV files as its data source. The system begins with a login page where users enter their username and password. Two user roles are supported: Employee and Payroll Staff.

If the user logs in as an employee, the system allows them to access the payroll computation module, search for an employee record, and view payroll information based on attendance records stored in CSV files.

If the user logs in as payroll staff, the system provides access to Employee Record Management and Payroll Computation modules. Payroll staff can view, add, update, and delete employee records. All changes are automatically saved to the CSV files and remain available after restarting the application.

The payroll module reads employee and attendance records, computes hours worked, calculates gross pay, applies government deductions such as SSS, PhilHealth, Pag-IBIG, and Withholding Tax, and displays the resulting net pay.

## Features Implemented

### Login Authentication

* Username and password validation
* Role-based access control
* Employee and Payroll Staff accounts

### Employee Record Management

* View employee records
* Add employee records
* Update employee records
* Delete employee records
* View employee details

### Payroll Computation

* Search employee records
* Calculate hours worked
* Compute gross pay
* Compute government deductions
* Compute net pay

### Data Persistence

* Employee data stored in CSV files
* Attendance data stored in CSV files
* User credentials stored in CSV files
* Employee number tracking using a text file

## Test Accounts

### Payroll Staff

Username: admin

Password: admin123

### Employee

Username: user

Password: user123

