import java.io.*;
import java.util.ArrayList;

public class CSVDataManager {
    private static final String EMPLOYEE_FILE = "resources/MotorPH_Employee Data - Employee Details.csv";
    private static final String ATTENDANCE_FILE = "resources/MotorPH_Employee Data - Attendance Record.csv";

    private static final String EMPLOYEE_HEADER =
            "Employee #,Last Name,First Name,Birthday,Address,Phone Number,SSS #,Philhealth #,TIN #,Pag-ibig #,Status,Position,Immediate Supervisor,Basic Salary,Rice Subsidy,Phone Allowance,Clothing Allowance,Gross Semi-monthly Rate,Hourly Rate";

    public ArrayList<Employee> loadEmployees() {
        ArrayList<Employee> employees = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_FILE))) {
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = parseCSVLine(line);

                employees.add(new Employee(
                        Integer.parseInt(get(data, 0)),
                        get(data, 1),
                        get(data, 2),
                        get(data, 3),
                        get(data, 4),
                        get(data, 5),
                        get(data, 6),
                        get(data, 7),
                        get(data, 8),
                        get(data, 9),
                        get(data, 10),
                        get(data, 11),
                        get(data, 12),
                        parseNumber(get(data, 13)),
                        parseNumber(get(data, 14)),
                        parseNumber(get(data, 15)),
                        parseNumber(get(data, 16)),
                        parseNumber(get(data, 17)),
                        parseNumber(get(data, 18))
                ));
            }

        } catch (Exception e) {
            System.out.println("Error reading employee file: " + e.getMessage());
        }

        return employees;
    }

    public ArrayList<AttendanceRecord> loadAttendanceRecords() {
        ArrayList<AttendanceRecord> attendanceRecords = new ArrayList<>();
        ArrayList<Employee> employees = loadEmployees();

        try (BufferedReader br = new BufferedReader(new FileReader(ATTENDANCE_FILE))) {
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = parseCSVLine(line);

                int employeeNumber = Integer.parseInt(get(data, 0));
                String date = get(data, 3);
                String loginTime = get(data, 4);
                String logoutTime = get(data, 5);

                Employee employee = findEmployeeByNumber(employees, employeeNumber);

                if (employee != null) {
                    attendanceRecords.add(new AttendanceRecord(employee, date, loginTime, logoutTime));
                }
            }

        } catch (Exception e) {
            System.out.println("Error reading attendance file: " + e.getMessage());
        }

        return attendanceRecords;
    }

    public boolean addEmployee(Employee employee) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EMPLOYEE_FILE, true))) {
            writer.newLine();
            writer.write(employeeToCSV(employee));
            return true;

        } catch (Exception e) {
            System.out.println("Error writing employee file: " + e.getMessage());
            return false;
        }
    }

    public boolean updateEmployee(Employee updatedEmployee) {
        ArrayList<Employee> employees = loadEmployees();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EMPLOYEE_FILE))) {
            writer.write(EMPLOYEE_HEADER);
            writer.newLine();

            for (Employee employee : employees) {
                if (employee.getEmployeeNumber() == updatedEmployee.getEmployeeNumber()) {
                    writer.write(employeeToCSV(updatedEmployee));
                } else {
                    writer.write(employeeToCSV(employee));
                }

                writer.newLine();
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error updating employee: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteEmployee(int employeeNumber) {
        ArrayList<Employee> employees = loadEmployees();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(EMPLOYEE_FILE))) {
            writer.write(EMPLOYEE_HEADER);
            writer.newLine();

            for (Employee employee : employees) {
                if (employee.getEmployeeNumber() != employeeNumber) {
                    writer.write(employeeToCSV(employee));
                    writer.newLine();
                }
            }

            return true;

        } catch (Exception e) {
            System.out.println("Error deleting employee: " + e.getMessage());
            return false;
        }
    }

    public int getNextEmployeeNumber() {
        ArrayList<Employee> employees = loadEmployees();
        int highestNumber = 10000;

        for (Employee employee : employees) {
            if (employee.getEmployeeNumber() > highestNumber) {
                highestNumber = employee.getEmployeeNumber();
            }
        }

        return highestNumber + 1;
    }

    public Employee findEmployeeByNumber(ArrayList<Employee> employees, int employeeNumber) {
        for (Employee employee : employees) {
            if (employee.getEmployeeNumber() == employeeNumber) {
                return employee;
            }
        }

        return null;
    }

    private String employeeToCSV(Employee employee) {
        return csv(employee.getEmployeeNumber()) + "," +
                csv(employee.getLastName()) + "," +
                csv(employee.getFirstName()) + "," +
                csv(employee.getBirthday()) + "," +
                csv(employee.getAddress()) + "," +
                csv(employee.getPhoneNumber()) + "," +
                csv(employee.getSssNumber()) + "," +
                csv(employee.getPhilHealthNumber()) + "," +
                csv(employee.getTinNumber()) + "," +
                csv(employee.getPagibigNumber()) + "," +
                csv(employee.getStatus()) + "," +
                csv(employee.getPosition()) + "," +
                csv(employee.getSupervisor()) + "," +
                csv(employee.getBasicSalary()) + "," +
                csv(employee.getRiceSubsidy()) + "," +
                csv(employee.getPhoneAllowance()) + "," +
                csv(employee.getClothingAllowance()) + "," +
                csv(employee.getGrossSemiMonthlyRate()) + "," +
                csv(employee.getHourlyRate());
    }

    private String get(String[] data, int index) {
        if (index >= data.length) {
            return "";
        }

        return data[index].trim();
    }

    private double parseNumber(String value) {
        try {
            String cleaned = value.replaceAll("[^0-9.\\-]", "");

            if (cleaned.isEmpty()) {
                return 0.0;
            }

            return Double.parseDouble(cleaned);

        } catch (Exception e) {
            return 0.0;
        }
    }

    private String csv(Object value) {
        String text = String.valueOf(value == null ? "" : value);

        if (text.contains(",") || text.contains("\"")) {
            text = text.replace("\"", "\"\"");
            return "\"" + text + "\"";
        }

        return text;
    }

    private String[] parseCSVLine(String line) {
        ArrayList<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                insideQuotes = !insideQuotes;

            } else if (c == ',' && !insideQuotes) {
                values.add(current.toString());
                current.setLength(0);

            } else {
                current.append(c);
            }
        }

        values.add(current.toString());
        return values.toArray(new String[0]);
    }
}