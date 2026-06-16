import javax.swing.*;
import java.awt.*;

public class MainMenuFrame extends JFrame {

    public MainMenuFrame() {
        setTitle("MotorPH Employee App");
        setSize(400, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel title = new JLabel("MotorPH Employee App", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JButton recordsButton = new JButton("Employee Record Management");
        JButton payrollButton = new JButton("Payroll Computation");
        JButton logoutButton = new JButton("Logout");

        recordsButton.setFocusPainted(false);
        payrollButton.setFocusPainted(false);
        logoutButton.setFocusPainted(false);

        recordsButton.setFocusable(false);
        payrollButton.setFocusable(false);
        logoutButton.setFocusable(false);

        recordsButton.addActionListener(e -> {
            dispose();
            new EmployeeRecordManagementFrame();
        });

        payrollButton.addActionListener(e -> {
            dispose();
            new EmployeeDashboardFrame(true);
        });

        logoutButton.addActionListener(e -> {
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
        });

        panel.add(title);
        panel.add(recordsButton);
        panel.add(payrollButton);
        panel.add(logoutButton);

        add(panel);
        setVisible(true);
    }
}