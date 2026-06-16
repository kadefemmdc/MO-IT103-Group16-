import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class EmployeeRecordManagementFrame extends JFrame {

    private CSVDataManager dataManager;
    private ArrayList<Employee> employees;

    private JTable employeeTable;
    private DefaultTableModel tableModel;

    private JTextField employeeNumberField;
    private JTextField lastNameField;
    private JTextField firstNameField;
    private JSpinner birthdaySpinner;
    private JTextField positionField;
    private JTextField hourlyRateField;

    private JButton addButton;
    private JButton updateButton;
    private JButton viewButton;
    private JButton clearButton;
    private JButton refreshButton;
    private JButton deleteButton;
    private JButton backButton;

    public EmployeeRecordManagementFrame() {
        dataManager = new CSVDataManager();

        setTitle("MotorPH Employee App - Employee Record Management");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        createTable();
        createForm();
        loadTableData();

        setVisible(true);
    }

    private void createTable() {
        String[] columns = {
                "Employee #",
                "Last Name",
                "First Name",
                "Birthday",
                "Position",
                "Hourly Rate"
        };

        tableModel = new DefaultTableModel(columns, 0);
        employeeTable = new JTable(tableModel);

        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedEmployeeToForm();
            }
        });

        add(new JScrollPane(employeeTable), BorderLayout.CENTER);
    }

    private void createForm() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Employee Record Form"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        employeeNumberField = new JTextField(15);
        employeeNumberField.setEditable(false);

        lastNameField = new JTextField(15);
        firstNameField = new JTextField(15);
        birthdaySpinner = createBirthdaySpinner();
        positionField = new JTextField(15);
        hourlyRateField = new JTextField(15);

        addField(formPanel, gbc, "Employee #:", employeeNumberField, 0);
        addField(formPanel, gbc, "Last Name:", lastNameField, 1);
        addField(formPanel, gbc, "First Name:", firstNameField, 2);
        addField(formPanel, gbc, "Birthday:", birthdaySpinner, 3);
        addField(formPanel, gbc, "Position:", positionField, 4);
        addField(formPanel, gbc, "Hourly Rate:", hourlyRateField, 5);

JPanel buttonContainer = new JPanel(new GridLayout(2, 1, 5, 5));

addButton = new JButton("Add Employee");
updateButton = new JButton("Update Employee");
viewButton = new JButton("View Details");
clearButton = new JButton("Clear Fields");
refreshButton = new JButton("Refresh Table");
deleteButton = new JButton("Delete Employee");
backButton = new JButton("Back to Menu");

JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
topButtonPanel.add(addButton);
topButtonPanel.add(updateButton);
topButtonPanel.add(deleteButton);

JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
bottomButtonPanel.add(viewButton);
bottomButtonPanel.add(refreshButton);
bottomButtonPanel.add(clearButton);
bottomButtonPanel.add(backButton);

buttonContainer.add(topButtonPanel);
buttonContainer.add(bottomButtonPanel);

gbc.gridx = 0;
gbc.gridy = 6;
gbc.gridwidth = 2;
formPanel.add(buttonContainer, gbc);

        add(formPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addEmployee());
        updateButton.addActionListener(e -> updateEmployee());
        viewButton.addActionListener(e -> viewEmployeeDetails());
        clearButton.addActionListener(e -> clearFields());
        refreshButton.addActionListener(e -> loadTableData());
        deleteButton.addActionListener(e -> deleteSelectedEmployee());
        backButton.addActionListener(e -> {
     dispose();
    new MainMenuFrame();
});
    }

    private JSpinner createBirthdaySpinner() {
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.YEAR, -100);
        Date earliestDate = calendar.getTime();

        calendar = Calendar.getInstance();
        Date latestDate = calendar.getTime();

        SpinnerDateModel model = new SpinnerDateModel(
                latestDate,
                earliestDate,
                latestDate,
                Calendar.DAY_OF_MONTH
        );

        JSpinner spinner = new JSpinner(model);
        spinner.setEditor(new JSpinner.DateEditor(spinner, "MM/dd/yyyy"));

        return spinner;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void loadTableData() {
        employees = dataManager.loadEmployees();
        employees.sort(Comparator.comparingInt(Employee::getEmployeeNumber));

        tableModel.setRowCount(0);

        for (Employee employee : employees) {
            tableModel.addRow(new Object[]{
                    employee.getEmployeeNumber(),
                    employee.getLastName(),
                    employee.getFirstName(),
                    employee.getBirthday(),
                    employee.getPosition(),
                    employee.getHourlyRate()
            });
        }

        clearFields();
    }

    private void addEmployee() {
        try {
            int employeeNumber = Integer.parseInt(employeeNumberField.getText().trim());

            if (dataManager.findEmployeeByNumber(employees, employeeNumber) != null) {
                throw new IllegalArgumentException("Employee number already exists.");
            }

            Employee newEmployee = createEmployeeFromForm(employeeNumber, null);

            if (!dataManager.addEmployee(newEmployee)) {
                throw new IllegalArgumentException("Employee record was not saved.");
            }

            JOptionPane.showMessageDialog(this, "Employee record added successfully.");
            loadTableData();

        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        } catch (Exception ex) {
            showError("Unexpected error. Please check your input.");
        }
    }

    private void updateEmployee() {
        try {
            int selectedRow = employeeTable.getSelectedRow();

            if (selectedRow == -1) {
                throw new IllegalArgumentException("Please select an employee record to update.");
            }

            int employeeNumber = Integer.parseInt(employeeNumberField.getText().trim());
            Employee originalEmployee = dataManager.findEmployeeByNumber(employees, employeeNumber);

            if (originalEmployee == null) {
                throw new IllegalArgumentException("Selected employee could not be found.");
            }

            Employee updatedEmployee = createEmployeeFromForm(employeeNumber, originalEmployee);

            if (!dataManager.updateEmployee(updatedEmployee)) {
                throw new IllegalArgumentException("Employee record was not updated.");
            }

            JOptionPane.showMessageDialog(this, "Employee record updated successfully.");
            loadTableData();

        } catch (IllegalArgumentException ex) {
            showError(ex.getMessage());
        } catch (Exception ex) {
            showError("Unexpected error. Please check your input.");
        }
    }

    private Employee createEmployeeFromForm(int employeeNumber, Employee original) {
        String lastName = lastNameField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String birthday = getValidatedBirthday();
        String position = positionField.getText().trim();
        String hourlyRateText = hourlyRateField.getText().trim();

        validateName(lastName, "Last name");
        validateName(firstName, "First name");
        validatePosition(position);
        double hourlyRate = validateHourlyRate(hourlyRateText);

        if (original == null) {
            return new Employee(
                    employeeNumber,
                    lastName,
                    firstName,
                    birthday,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "Regular",
                    position,
                    "",
                    0,
                    0,
                    0,
                    0,
                    0,
                    hourlyRate
            );
        }

        return new Employee(
                employeeNumber,
                lastName,
                firstName,
                birthday,
                original.getAddress(),
                original.getPhoneNumber(),
                original.getSssNumber(),
                original.getPhilHealthNumber(),
                original.getTinNumber(),
                original.getPagibigNumber(),
                original.getStatus(),
                position,
                original.getSupervisor(),
                original.getBasicSalary(),
                original.getRiceSubsidy(),
                original.getPhoneAllowance(),
                original.getClothingAllowance(),
                original.getGrossSemiMonthlyRate(),
                hourlyRate
        );
    }

    private void loadSelectedEmployeeToForm() {
        int selectedRow = employeeTable.getSelectedRow();

        if (selectedRow == -1) {
            return;
        }

        employeeNumberField.setText(tableModel.getValueAt(selectedRow, 0).toString());
        lastNameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
        firstNameField.setText(tableModel.getValueAt(selectedRow, 2).toString());
        positionField.setText(tableModel.getValueAt(selectedRow, 4).toString());
        hourlyRateField.setText(tableModel.getValueAt(selectedRow, 5).toString());

        setBirthdaySpinnerValue(tableModel.getValueAt(selectedRow, 3).toString());
    }

    private void viewEmployeeDetails() {
        int selectedRow = employeeTable.getSelectedRow();

        if (selectedRow == -1) {
            showError("Please select an employee record first.");
            return;
        }

        String details =
                "Employee Number: " + tableModel.getValueAt(selectedRow, 0) +
                "\nLast Name: " + tableModel.getValueAt(selectedRow, 1) +
                "\nFirst Name: " + tableModel.getValueAt(selectedRow, 2) +
                "\nBirthday: " + tableModel.getValueAt(selectedRow, 3) +
                "\nPosition: " + tableModel.getValueAt(selectedRow, 4) +
                "\nHourly Rate: " + tableModel.getValueAt(selectedRow, 5);

        JOptionPane.showMessageDialog(
                this,
                details,
                "Employee Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void deleteSelectedEmployee() {
        int selectedRow = employeeTable.getSelectedRow();

        if (selectedRow == -1) {
            showError("Please select an employee record to delete.");
            return;
        }

        int employeeNumber = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete employee #" + employeeNumber + "?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            if (dataManager.deleteEmployee(employeeNumber)) {
                JOptionPane.showMessageDialog(this, "Employee record deleted successfully.");
                loadTableData();
            } else {
                showError("Employee record could not be deleted.");
            }
        }
    }

    private void validateName(String name, String fieldName) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }

        if (name.length() < 2) {
            throw new IllegalArgumentException(fieldName + " must be at least 2 characters long.");
        }

        if (!name.matches("[A-Za-zÑñ\\s'-]+")) {
            throw new IllegalArgumentException(fieldName + " must contain letters only.");
        }
    }

    private void validatePosition(String position) {
        if (position.isEmpty()) {
            throw new IllegalArgumentException("Position is required.");
        }

        if (position.length() < 2) {
            throw new IllegalArgumentException("Position must be at least 2 characters long.");
        }

        if (!position.matches("[A-Za-zÑñ\\s&/-]+")) {
            throw new IllegalArgumentException("Position must contain valid letters or symbols only.");
        }
    }

    private double validateHourlyRate(String hourlyRateText) {
        if (hourlyRateText.isEmpty()) {
            throw new IllegalArgumentException("Hourly rate is required.");
        }

        if (!hourlyRateText.matches("[0-9]+(\\.[0-9]{1,2})?")) {
            throw new IllegalArgumentException("Hourly rate must be numeric and may only have up to 2 decimal places.");
        }

        double hourlyRate = Double.parseDouble(hourlyRateText);

        if (hourlyRate <= 0) {
            throw new IllegalArgumentException("Hourly rate must be greater than zero.");
        }

        if (hourlyRate > 10000) {
            throw new IllegalArgumentException("Hourly rate is too high.");
        }

        return hourlyRate;
    }

    private String getValidatedBirthday() {
        try {
            Date selectedDate = (Date) birthdaySpinner.getValue();
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            formatter.setLenient(false);
            return formatter.format(selectedDate);

        } catch (Exception e) {
            throw new IllegalArgumentException("Please enter a valid birthday.");
        }
    }

    private void setBirthdaySpinnerValue(String birthdayText) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            formatter.setLenient(false);
            birthdaySpinner.setValue(formatter.parse(birthdayText));
        } catch (Exception e) {
            birthdaySpinner.setValue(getDefaultBirthday());
        }
    }

    private void clearFields() {
        employeeNumberField.setText(String.valueOf(dataManager.getNextEmployeeNumber()));
        lastNameField.setText("");
        firstNameField.setText("");
        positionField.setText("");
        hourlyRateField.setText("");
        birthdaySpinner.setValue(getDefaultBirthday());
        employeeTable.clearSelection();
    }

    private Date getDefaultBirthday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -25);
        return calendar.getTime();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Input Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}