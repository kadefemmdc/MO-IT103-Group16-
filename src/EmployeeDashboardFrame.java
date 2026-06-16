import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EmployeeDashboardFrame extends JFrame {

    private JTextField employeeNumberField;
    private JComboBox<String> payPeriodComboBox;

    private JLabel nameValueLabel;
    private JLabel birthdayValueLabel;
    private JLabel positionValueLabel;
    private JLabel hourlyRateValueLabel;

    private JLabel hoursWorkedValueLabel;
    private JLabel cutoffPeriodValueLabel;
    private JLabel grossPayValueLabel;
    private JLabel sssValueLabel;
    private JLabel philHealthValueLabel;
    private JLabel pagibigValueLabel;
    private JLabel taxValueLabel;
    private JLabel netPayValueLabel;

    private JButton searchButton;
    private JButton computePayrollButton;
    private JButton resetButton;
    private JButton backButton;

    private CSVDataManager dataManager;
    private ArrayList<Employee> employees;
    private ArrayList<AttendanceRecord> attendanceRecords;

    private Employee selectedEmployee;
    private boolean payrollStaffMode;

    public EmployeeDashboardFrame() {
    this(false);
}

public EmployeeDashboardFrame(boolean payrollStaffMode) {
    this.payrollStaffMode = payrollStaffMode;

        dataManager = new CSVDataManager();
        employees = dataManager.loadEmployees();
        attendanceRecords = dataManager.loadAttendanceRecords();

        setTitle("MotorPH Employee App - Payroll Computation");
        setSize(670, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(7, 10, 7, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel title = new JLabel("MotorPH Employee Payroll", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(title, gbc);

        gbc.gridy = 1;

        JLabel instruction = new JLabel(
                "Search an employee, select a payroll cutoff period, then compute payroll.",
                SwingConstants.CENTER
        );
        instruction.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(instruction, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Employee Number:"), gbc);

        gbc.gridx = 1;
        employeeNumberField = new JTextField(15);
        panel.add(employeeNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Pay Period:"), gbc);

        gbc.gridx = 1;
        payPeriodComboBox = new JComboBox<>(new String[]{
                "June 1-15",
                "June 16-30",
                "July 1-15",
                "July 16-31",
                "August 1-15",
                "August 16-31",
                "September 1-15",
                "September 16-30",
                "October 1-15",
                "October 16-31",
                "November 1-15",
                "November 16-30",
                "December 1-15",
                "December 16-31"
        });
        payPeriodComboBox.setEnabled(false);
        panel.add(payPeriodComboBox, gbc);

        payPeriodComboBox.addActionListener(e -> clearPayrollLabels());

        gbc.gridx = 0;
        gbc.gridy = 4;
        searchButton = new JButton("Search Employee");
        panel.add(searchButton, gbc);

        gbc.gridx = 1;
        resetButton = new JButton("Reset Form");
        panel.add(resetButton, gbc);

        addSectionHeader(panel, gbc, "Employee Details", 5);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Employee Name:"), gbc);

        gbc.gridx = 1;
        nameValueLabel = new JLabel("-");
        panel.add(nameValueLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(new JLabel("Birthday:"), gbc);

        gbc.gridx = 1;
        birthdayValueLabel = new JLabel("-");
        panel.add(birthdayValueLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 8;
        panel.add(new JLabel("Position:"), gbc);

        gbc.gridx = 1;
        positionValueLabel = new JLabel("-");
        panel.add(positionValueLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 9;
        panel.add(new JLabel("Hourly Rate:"), gbc);

        gbc.gridx = 1;
        hourlyRateValueLabel = new JLabel("-");
        panel.add(hourlyRateValueLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;

        computePayrollButton = new JButton("Compute Payroll");
        computePayrollButton.setEnabled(false);
        panel.add(computePayrollButton, gbc);

        gbc.gridwidth = 1;

        addSectionHeader(panel, gbc, "Attendance Summary", 11);

        gbc.gridx = 0;
        gbc.gridy = 12;
        panel.add(new JLabel("Cutoff Period:"), gbc);

        gbc.gridx = 1;
        cutoffPeriodValueLabel = new JLabel("-");
        panel.add(cutoffPeriodValueLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 13;
        panel.add(new JLabel("Hours Worked:"), gbc);

        gbc.gridx = 1;
        hoursWorkedValueLabel = new JLabel("-");
        panel.add(hoursWorkedValueLabel, gbc);

        addSectionHeader(panel, gbc, "Salary Computation", 14);

        gbc.gridx = 0;
        gbc.gridy = 15;
        panel.add(new JLabel("Gross Pay:"), gbc);

        gbc.gridx = 1;
        grossPayValueLabel = new JLabel("-");
        panel.add(grossPayValueLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 16;
        panel.add(new JLabel("SSS:"), gbc);

        gbc.gridx = 1;
        sssValueLabel = new JLabel("-");
        panel.add(sssValueLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 17;
        panel.add(new JLabel("PhilHealth:"), gbc);

        gbc.gridx = 1;
        philHealthValueLabel = new JLabel("-");
        panel.add(philHealthValueLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 18;
        panel.add(new JLabel("Pag-IBIG:"), gbc);

        gbc.gridx = 1;
        pagibigValueLabel = new JLabel("-");
        panel.add(pagibigValueLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 19;
        panel.add(new JLabel("Withholding Tax:"), gbc);

        gbc.gridx = 1;
        taxValueLabel = new JLabel("-");
        panel.add(taxValueLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 20;
        panel.add(new JLabel("Net Pay:"), gbc);

        gbc.gridx = 1;
        netPayValueLabel = new JLabel("-");
        panel.add(netPayValueLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 21;
        gbc.gridwidth = 2;

        backButton = new JButton(
        payrollStaffMode ? "Back to Menu" : "Logout"
);
        panel.add(backButton, gbc);

        add(panel);

        searchButton.addActionListener(e -> searchEmployee());
        computePayrollButton.addActionListener(e -> computePayroll());
        resetButton.addActionListener(e -> resetForm());

       backButton.addActionListener(e -> {

    if (payrollStaffMode) {
        dispose();
        new MainMenuFrame();

    } else {

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame();
        }
    }
});

        employeeNumberField.getDocument().addDocumentListener(
                new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) {
                        clearOnEmployeeNumberChange();
                    }

                    public void removeUpdate(DocumentEvent e) {
                        clearOnEmployeeNumberChange();
                    }

                    public void changedUpdate(DocumentEvent e) {
                        clearOnEmployeeNumberChange();
                    }
                }
        );

        setVisible(true);
    }

    private void addSectionHeader(JPanel panel, GridBagConstraints gbc, String text, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setOpaque(false);

        JLabel sectionLabel = new JLabel(text);
        sectionLabel.setFont(new Font("Arial", Font.BOLD, 16));

        headerPanel.add(sectionLabel);
        panel.add(headerPanel, gbc);

        gbc.gridwidth = 1;
    }

    private void searchEmployee() {
        try {
            String employeeNumberText = employeeNumberField.getText().trim();

            if (employeeNumberText.isEmpty()) {
                throw new IllegalArgumentException("Please enter an employee number.");
            }

            if (!employeeNumberText.matches("-?[0-9]+")) {
                throw new IllegalArgumentException("Employee Number must be numeric.");
            }

            int employeeNumber = Integer.parseInt(employeeNumberText);

            if (employeeNumber <= 0) {
                throw new IllegalArgumentException("Employee Number must be greater than zero.");
            }

            selectedEmployee = dataManager.findEmployeeByNumber(employees, employeeNumber);

            if (selectedEmployee == null) {
                throw new IllegalArgumentException("Employee not found.");
            }

            nameValueLabel.setText(selectedEmployee.getFullName());
            birthdayValueLabel.setText(selectedEmployee.getBirthday());
            positionValueLabel.setText(selectedEmployee.getPosition());
            hourlyRateValueLabel.setText(formatMoney(selectedEmployee.getHourlyRate()));

            clearPayrollLabels();
            payPeriodComboBox.setSelectedIndex(0);
            payPeriodComboBox.setEnabled(true);
            computePayrollButton.setEnabled(true);

            JOptionPane.showMessageDialog(
                    this,
                    "Employee record loaded successfully.",
                    "Search Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (IllegalArgumentException ex) {
            showErrorMessage(ex.getMessage());
        }
    }

    private void computePayroll() {
        if (selectedEmployee == null) {
            showErrorMessage("Please search for an employee first.");
            return;
        }

        String selectedPeriod = payPeriodComboBox.getSelectedItem().toString();
        double totalHours = 0;

        for (AttendanceRecord record : attendanceRecords) {
            if (record.getEmployee().getEmployeeNumber() == selectedEmployee.getEmployeeNumber()
                    && isWithinSelectedPeriod(record.getDate(), selectedPeriod)) {
                totalHours += record.getHoursWorked();
            }
        }

        if (totalHours <= 0) {
            showErrorMessage("No attendance records found for this employee in the selected pay period.");
            return;
        }

        boolean applyDeductions = selectedPeriod.contains("16-");
        double monthlyGross = computeMonthlyGrossForSelectedPeriod(selectedPeriod);

        PayrollRecord payrollRecord = new PayrollRecord(
                selectedEmployee,
                selectedPeriod,
                totalHours,
                monthlyGross,
                applyDeductions
        );

        cutoffPeriodValueLabel.setText(selectedPeriod);
        hoursWorkedValueLabel.setText(String.format("%.2f", totalHours));
        grossPayValueLabel.setText(formatMoney(payrollRecord.getGrossPay()));
        sssValueLabel.setText(formatMoney(payrollRecord.getSSS()));
        philHealthValueLabel.setText(formatMoney(payrollRecord.getPhilHealth()));
        pagibigValueLabel.setText(formatMoney(payrollRecord.getPagibig()));
        taxValueLabel.setText(formatMoney(payrollRecord.getWithholdingTax()));
        netPayValueLabel.setText(formatMoney(payrollRecord.getNetPay()));

        JOptionPane.showMessageDialog(
                this,
                "Payroll computed successfully for " + selectedPeriod + ".",
                "Computation Complete",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private double computeMonthlyGrossForSelectedPeriod(String selectedPeriod) {
        String monthName = selectedPeriod.split(" ")[0];
        int targetMonth = getMonthNumber(monthName);
        double monthlyHours = 0;

        for (AttendanceRecord record : attendanceRecords) {
            if (record.getEmployee().getEmployeeNumber() == selectedEmployee.getEmployeeNumber()) {
                try {
                    LocalDate date = parseDate(record.getDate());

                    if (date.getMonthValue() == targetMonth) {
                        monthlyHours += record.getHoursWorked();
                    }

                } catch (Exception e) {
                    // Invalid date records are skipped.
                }
            }
        }

        return monthlyHours * selectedEmployee.getHourlyRate();
    }

    private int getMonthNumber(String monthName) {
        switch (monthName) {
            case "June": return 6;
            case "July": return 7;
            case "August": return 8;
            case "September": return 9;
            case "October": return 10;
            case "November": return 11;
            case "December": return 12;
            default: return 0;
        }
    }

    private boolean isWithinSelectedPeriod(String dateText, String selectedPeriod) {
        try {
            LocalDate date = parseDate(dateText);

            int month = date.getMonthValue();
            int day = date.getDayOfMonth();

            if (selectedPeriod.equals("June 1-15")) return month == 6 && day >= 1 && day <= 15;
            if (selectedPeriod.equals("June 16-30")) return month == 6 && day >= 16 && day <= 30;
            if (selectedPeriod.equals("July 1-15")) return month == 7 && day >= 1 && day <= 15;
            if (selectedPeriod.equals("July 16-31")) return month == 7 && day >= 16 && day <= 31;
            if (selectedPeriod.equals("August 1-15")) return month == 8 && day >= 1 && day <= 15;
            if (selectedPeriod.equals("August 16-31")) return month == 8 && day >= 16 && day <= 31;
            if (selectedPeriod.equals("September 1-15")) return month == 9 && day >= 1 && day <= 15;
            if (selectedPeriod.equals("September 16-30")) return month == 9 && day >= 16 && day <= 30;
            if (selectedPeriod.equals("October 1-15")) return month == 10 && day >= 1 && day <= 15;
            if (selectedPeriod.equals("October 16-31")) return month == 10 && day >= 16 && day <= 31;
            if (selectedPeriod.equals("November 1-15")) return month == 11 && day >= 1 && day <= 15;
            if (selectedPeriod.equals("November 16-30")) return month == 11 && day >= 16 && day <= 30;
            if (selectedPeriod.equals("December 1-15")) return month == 12 && day >= 1 && day <= 15;
            if (selectedPeriod.equals("December 16-31")) return month == 12 && day >= 16 && day <= 31;

        } catch (Exception e) {
            return false;
        }

        return false;
    }

    private LocalDate parseDate(String dateText) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        return LocalDate.parse(dateText, formatter);
    }

    private String formatMoney(double value) {
        return String.format("₱%,.2f", value);
    }

    private void clearOnEmployeeNumberChange() {
        selectedEmployee = null;

        nameValueLabel.setText("-");
        birthdayValueLabel.setText("-");
        positionValueLabel.setText("-");
        hourlyRateValueLabel.setText("-");

        clearPayrollLabels();

        payPeriodComboBox.setSelectedIndex(0);
        payPeriodComboBox.setEnabled(false);

        computePayrollButton.setEnabled(false);
    }

    private void resetForm() {
        employeeNumberField.setText("");
        payPeriodComboBox.setSelectedIndex(0);
        payPeriodComboBox.setEnabled(false);
        selectedEmployee = null;

        nameValueLabel.setText("-");
        birthdayValueLabel.setText("-");
        positionValueLabel.setText("-");
        hourlyRateValueLabel.setText("-");

        clearPayrollLabels();
        computePayrollButton.setEnabled(false);
    }

    private void clearPayrollLabels() {
        cutoffPeriodValueLabel.setText("-");
        hoursWorkedValueLabel.setText("-");
        grossPayValueLabel.setText("-");
        sssValueLabel.setText("-");
        philHealthValueLabel.setText("-");
        pagibigValueLabel.setText("-");
        taxValueLabel.setText("-");
        netPayValueLabel.setText("-");
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Validation Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}