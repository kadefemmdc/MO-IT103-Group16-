import javax.swing.*;
import java.awt.*;

public class MainMenuFrame extends JFrame {

    public MainMenuFrame() {
        setTitle("MotorPH Employee App");
        setSize(400, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel title = new JLabel("MotorPH Employee App", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JButton recordsButton = new JButton("Employee Record Management");
        JButton payrollButton = new JButton("Payroll Computation");

        recordsButton.setFocusPainted(false);
        payrollButton.setFocusPainted(false);

        recordsButton.setFocusable(false);
        payrollButton.setFocusable(false);

        recordsButton.addActionListener(e -> {
            dispose();
            new EmployeeRecordManagementFrame();
        });

        payrollButton.addActionListener(e -> {
            dispose();
            new EmployeeDashboardFrame();
        });

        panel.add(title);
        panel.add(recordsButton);
        panel.add(payrollButton);

        add(panel);
        setVisible(true);
    }
}