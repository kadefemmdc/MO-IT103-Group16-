import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JPanel mainPanel;

    private static final String USERS_FILE = "resources/users.csv";

    public LoginFrame() {

        setTitle("MotorPH Employee App - Login");
        setSize(500, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel(
                "MotorPH Employee Management System",
                SwingConstants.CENTER
        );
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));

        JLabel subtitleLabel = new JLabel(
                "Please enter your username and password to access the system.",
                SwingConstants.CENTER
        );
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        gbc.gridy = 1;
        mainPanel.add(subtitleLabel, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        mainPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        mainPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;

        loginButton = new JButton("Login");
        loginButton.setFocusPainted(false);
        loginButton.setFocusable(false);
        mainPanel.add(loginButton, gbc);

        add(mainPanel);

        loginButton.addActionListener(e -> validateLogin());

        setVisible(true);
    }

    private void validateLogin() {
        String inputUsername = usernameField.getText().trim();
        String inputPassword = new String(passwordField.getPassword()).trim();

        if (inputUsername.isEmpty()) {
            showError("Please enter username.");
            return;
        }

        if (inputPassword.isEmpty()) {
            showError("Please enter password.");
            return;
        }

        String role = checkCredentials(inputUsername, inputPassword);

        if (role == null) {
            showError("Invalid username or password.");
            return;
        }

        JOptionPane.showMessageDialog(
                this,
                "Login successful. Role: " + role,
                "Login Success",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();

        if (role.equalsIgnoreCase("Payroll Staff")) {
            new MainMenuFrame();
        } else if (role.equalsIgnoreCase("Employee")) {
            new EmployeeDashboardFrame();
        } else {
            showError("Unknown role assigned to this account.");
        }
    }

    private String checkCredentials(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(USERS_FILE))) {
            br.readLine();

            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split(",", -1);

                if (data.length < 3) {
                    continue;
                }

                String storedUsername = data[0].trim();
                String storedPassword = data[1].trim();
                String storedRole = data[2].trim();

                if (username.equals(storedUsername) && password.equals(storedPassword)) {
                    return storedRole;
                }
            }

        } catch (Exception e) {
            showError("Unable to read users.csv file.");
        }

        return null;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this,
                message,
                "Login Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}