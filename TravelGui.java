// ==================== TravelGui.java (NEW MAIN FILE - v4: SAFARI Dashboard) ====================
import models.*;
import services.*;
import threads.*;
import exceptions.*;
import interfaces.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import javax.swing.plaf.basic.BasicGraphicsUtils; // For a specific icon fix

public class TravelGui extends JFrame {

    // --- 1. Style & Color Palette ---
// --- 1. Style & Color Palette ---
// --- 1. Style & Color Palette ---
    private static class Style {
        // Gradient Colors for overall theme
        public static final Color GRADIENT_YELLOW = new Color(255, 230, 80);
        public static final Color GRADIENT_ORANGE = new Color(255, 160, 50);
        public static final Color GRADIENT_PINK = new Color(255, 80, 150);
        public static final Color GRADIENT_PURPLE = new Color(160, 90, 255);

        // Base Colors
        public static final Color COLOR_WHITE = new Color(255, 255, 255);
        public static final Color COLOR_BLACK = new Color(30, 30, 30);
        public static final Color COLOR_LIGHT_GRAY = new Color(245, 245, 245);
        public static final Color COLOR_MEDIUM_GRAY = new Color(200, 200, 200);
        public static final Color COLOR_DARK_GRAY = new Color(50, 50, 50);

        // Fonts
        public static final Font FONT_TITLE = new Font("Book Antiqua", Font.BOLD, 36);
        public static final Font FONT_HEADER = new Font("Book Antiqua", Font.BOLD, 22);
        public static final Font FONT_BODY = new Font("Book Antiqua", Font.PLAIN, 18);
        public static final Font FONT_BUTTON = new Font("Book Antiqua", Font.BOLD, 16);
        public static final Font FONT_LABEL = new Font("Book Antiqua", Font.PLAIN, 16);
        public static final Font FONT_SMALL_LABEL = new Font("Book Antiqua", Font.PLAIN, 14);
        public static final Font FONT_TAGLINE = new Font("Dancing Script", Font.PLAIN, 24);

        // Borders & Spacing
        public static final Border PADDING = new EmptyBorder(20, 20, 20, 20);
        public static final Border PADDING_SMALL = new EmptyBorder(10, 10, 10, 10);
        public static final int BORDER_RADIUS = 30; // For cards and buttons
        public static final int LOGIN_BOX_RADIUS = 40;
        public static final int FIELD_BORDER_RADIUS = 20;

        // --- ADD THIS LINE BACK ---
        public static final int SMALL_BORDER_RADIUS = 20; // Radius for general panels

        // Logo and Tagline
        public static final String SYSTEM_NAME = "SAFARI";
        public static final String TAGLINE = "Your Journey, Our Expertise.";
        public static final String LOGO_PATH = "/safari.png";
    }

    // --- 2. Backend Services ---
    private TravelAgency travelAgency;
    private BookingManager bookingManager;

    // --- 3. GUI Components ---
    private JPanel mainCardPanel; // The main panel using CardLayout
    private CardLayout mainCardLayout;
    private JTextArea logTextArea;

    // Data containers for cards
    private JPanel customerCardContainer;
    private JPanel packageCardContainer;
    private JPanel bookingCardContainer;

    // Dashboard components
    private JLabel statCustomers, statPackages, statBookings;
    private GraphPanel graphPanel;

    private DefaultComboBoxModel<Customer> customerComboBoxModel;
    private DefaultComboBoxModel<TravelPackage> packageComboBoxModel;

    // Header Components (for dynamic visibility)
    private JLabel headerProfileLabel;
    private RoundedButton headerLoginButton;
    private JPasswordField loginPasswordField;

    // Constants
    private final String LOGIN_PANEL = "LOGIN";
    private final String APP_PANEL = "APP";

    private boolean isLoggedIn = false;

    public TravelGui() {
        // --- 1. Initialize Backend ---
        travelAgency = new TravelAgency();
        bookingManager = new BookingManager();

        customerComboBoxModel = new DefaultComboBoxModel<>();
        packageComboBoxModel = new DefaultComboBoxModel<>();

        // --- 2. Setup Main Window ---
        setTitle(Style.SYSTEM_NAME + " - Travel & Tourism Management System");
        setSize(1280, 800);
        setMinimumSize(new Dimension(1000, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainCardLayout = new CardLayout();
        mainCardPanel = new JPanel(mainCardLayout);

        // --- 3. Create Panels ---
        JPanel loginPanel = createLoginPanel();
        JPanel mainAppPanel = createMainAppPanel();

        mainCardPanel.add(loginPanel, LOGIN_PANEL);
        mainCardPanel.add(mainAppPanel, APP_PANEL);

        add(mainCardPanel, BorderLayout.CENTER);

        // --- 4. Populate Initial Data ---
        populateInitialData();

        // Start on the login panel
        mainCardLayout.show(mainCardPanel, LOGIN_PANEL);
    }

    // ====================================================================
    // --- 1. LOGIN PANEL ---
    // ====================================================================

    // ====================================================================
    // --- 1. LOGIN PANEL ---
    // ====================================================================

    private JPanel createLoginPanel() {
        GradientPanel panel = new GradientPanel();
        panel.setLayout(new GridBagLayout());

        // --- MODIFIED: Increased login box radius ---
        JPanel loginBox = new RoundedPanel(Style.LOGIN_BOX_RADIUS, Style.COLOR_WHITE); // Rounded login box
        loginBox.setLayout(new BoxLayout(loginBox, BoxLayout.Y_AXIS));
        loginBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Style.COLOR_MEDIUM_GRAY, 1),
                new EmptyBorder(40, 40, 40, 40)
        ));

        // Logo and Title
        JLabel appTitle = new JLabel(Style.SYSTEM_NAME);
        appTitle.setFont(Style.FONT_TITLE.deriveFont(Font.ITALIC, 48f));
        appTitle.setForeground(Style.COLOR_BLACK);
        appTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel tagline = new JLabel(Style.TAGLINE);
        // --- MODIFIED: Changed tagline font ---
        tagline.setFont(Style.FONT_TAGLINE);
        tagline.setForeground(Style.COLOR_DARK_GRAY);
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- Logo Image (Optional) ---
        ImageIcon logoIcon = null;
        try {
            Image img = new ImageIcon(getClass().getResource(Style.LOGO_PATH)).getImage();
            Image scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            logoIcon = new ImageIcon(scaledImg);
        } catch (Exception e) {
            System.err.println("Could not load logo image: " + Style.LOGO_PATH + " - " + e.getMessage());
        }

        JLabel logoLabel;
        if (logoIcon != null) {
            logoLabel = new JLabel(logoIcon);
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        } else {
            logoLabel = new JLabel(Style.SYSTEM_NAME);
            logoLabel.setFont(Style.FONT_TITLE.deriveFont(Font.BOLD, 48f));
            logoLabel.setForeground(Style.COLOR_BLACK);
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        }

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(Style.FONT_LABEL);
        // --- MODIFIED: Centered admin/username ---
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField userField = new JTextField("admin");
        userField.setFont(Style.FONT_BODY);
        // --- MODIFIED: Centered admin/username ---
        userField.setAlignmentX(Component.CENTER_ALIGNMENT);
        userField.setHorizontalAlignment(JTextField.CENTER); // Center the text inside
        userField.setMaximumSize(new Dimension(300, 50)); // Increased height
        // --- MODIFIED: Added new rounded border ---
        userField.setBorder(new RoundedLineBorder(Style.COLOR_MEDIUM_GRAY, 1,
                Style.FIELD_BORDER_RADIUS, new Insets(10, 15, 10, 15)));

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(Style.FONT_LABEL);
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Already centered, but good to be explicit

        loginPasswordField = new JPasswordField("password"); // Assign to class field
        loginPasswordField.setFont(Style.FONT_BODY);
        loginPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT); // Already centered
        loginPasswordField.setHorizontalAlignment(JTextField.CENTER); // Center the text inside
        loginPasswordField.setMaximumSize(new Dimension(300, 50)); // Increased height
        // --- MODIFIED: Added new rounded border ---
        loginPasswordField.setBorder(new RoundedLineBorder(Style.COLOR_MEDIUM_GRAY, 1,
                Style.FIELD_BORDER_RADIUS, new Insets(10, 15, 10, 15)));

        JCheckBox showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.setFont(Style.FONT_SMALL_LABEL);
        showPasswordCheckbox.setOpaque(false);
        // --- MODIFIED: Centered checkbox ---
        showPasswordCheckbox.setAlignmentX(Component.CENTER_ALIGNMENT);
        showPasswordCheckbox.addActionListener(e -> {
            if (showPasswordCheckbox.isSelected()) {
                loginPasswordField.setEchoChar((char) 0); // Show characters
            } else {
                loginPasswordField.setEchoChar('*'); // Hide characters
            }
        });

        JLabel errorLabel = new JLabel(" ");
        errorLabel.setFont(Style.FONT_LABEL);
        errorLabel.setForeground(Color.RED);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedButton loginButton = new RoundedButton("Login"); // Custom button for gradient
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginBox.add(logoLabel);
        if (logoIcon == null) {
            loginBox.add(Box.createRigidArea(new Dimension(0, 10)));
            loginBox.add(appTitle);
        }
        loginBox.add(Box.createRigidArea(new Dimension(0, 5)));
        loginBox.add(tagline);
        loginBox.add(Box.createRigidArea(new Dimension(0, 40)));
        loginBox.add(userLabel);
        loginBox.add(Box.createRigidArea(new Dimension(0, 5)));
        loginBox.add(userField);
        loginBox.add(Box.createRigidArea(new Dimension(0, 20)));
        loginBox.add(passLabel);
        loginBox.add(Box.createRigidArea(new Dimension(0, 5)));
        loginBox.add(loginPasswordField);
        loginBox.add(Box.createRigidArea(new Dimension(0, 5))); // Added space
        loginBox.add(showPasswordCheckbox); // Add show password checkbox
        loginBox.add(Box.createRigidArea(new Dimension(0, 20)));
        loginBox.add(errorLabel);
        loginBox.add(Box.createRigidArea(new Dimension(0, 10)));
        loginBox.add(loginButton);

        panel.add(loginBox); // Add the loginBox to the center of the gradient panel

        // --- Login Action ---
        ActionListener loginAction = e -> {
            String username = userField.getText();
            String password = new String(loginPasswordField.getPassword());

            if ("admin".equals(username) && "password".equals(password)) {
                isLoggedIn = true;
                mainCardLayout.show(mainCardPanel, APP_PANEL);
                updateHeaderButtons(); // Update header to show admin profile
                refreshAllData(); // Load data into dashboard and cards
                errorLabel.setText(" "); // Clear error
                loginPasswordField.setText("");
                showPasswordCheckbox.setSelected(false);
                loginPasswordField.setEchoChar('*');
            } else {
                errorLabel.setText("Invalid username or password.");
            }
        };
        loginButton.addActionListener(loginAction);
        loginPasswordField.addActionListener(loginAction); // Allow login on Enter

        return panel;
    }

    // ====================================================================
    // --- 2. MAIN APPLICATION PANEL (Header + Tabs) ---
    // ====================================================================

// ====================================================================
    // --- 2. MAIN APPLICATION PANEL (Header + Tabs) ---
    // ====================================================================

    private JPanel createMainAppPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Style.COLOR_LIGHT_GRAY);

        // --- 2a. Header Panel ---
        GradientPanel headerPanel = new GradientPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        // SAFARI Logo/Text on the left
        JLabel safariLogoLabel;
        ImageIcon headerLogoIcon = null;
        try {
            Image img = new ImageIcon(getClass().getResource(Style.LOGO_PATH)).getImage();
            Image scaledImg = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            headerLogoIcon = new ImageIcon(scaledImg);
        } catch (Exception e) {
            // Fallback to text
        }

        if (headerLogoIcon != null) {
            safariLogoLabel = new JLabel(Style.SYSTEM_NAME, headerLogoIcon, JLabel.LEFT);
            safariLogoLabel.setIconTextGap(10); // Space between icon and text
        } else {
            safariLogoLabel = new JLabel(Style.SYSTEM_NAME);
        }

        safariLogoLabel.setFont(Style.FONT_HEADER);
        safariLogoLabel.setForeground(Style.COLOR_WHITE);

        headerPanel.add(safariLogoLabel, BorderLayout.WEST);

        // Right side of header: Profile/Login/Signup
        JPanel headerRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        headerRightPanel.setOpaque(false);

        headerLoginButton = new RoundedButton("Login / Sign Up");
        headerLoginButton.setFont(Style.FONT_SMALL_LABEL);
        headerLoginButton.addActionListener(e -> mainCardLayout.show(mainCardPanel, LOGIN_PANEL));

        headerProfileLabel = new JLabel("Admin");
        headerProfileLabel.setFont(Style.FONT_LABEL);
        headerProfileLabel.setForeground(Style.COLOR_WHITE);
        headerProfileLabel.setIcon(UIManager.getIcon("OptionPane.userIcon"));
        headerProfileLabel.setHorizontalTextPosition(JLabel.LEFT); // Icon on right
        headerProfileLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Profile popup menu
        JPopupMenu profileMenu = new JPopupMenu();
        profileMenu.setFont(Style.FONT_SMALL_LABEL);

        JMenuItem viewProfileItem = new JMenuItem("View Profile");
        viewProfileItem.setFont(Style.FONT_SMALL_LABEL);
        viewProfileItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Admin Information:\nUsername: admin\nEmail: admin@safari.com\nRole: Administrator",
                "Admin Profile", JOptionPane.INFORMATION_MESSAGE));

        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.setFont(Style.FONT_SMALL_LABEL);
        logoutItem.addActionListener(e -> {
            isLoggedIn = false;
            mainCardLayout.show(mainCardPanel, LOGIN_PANEL);
            updateHeaderButtons();
        });

        profileMenu.add(viewProfileItem);
        profileMenu.addSeparator();
        profileMenu.add(logoutItem);

        headerProfileLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                profileMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        headerRightPanel.add(headerLoginButton);
        headerRightPanel.add(headerProfileLabel);

        headerPanel.add(headerRightPanel, BorderLayout.EAST);

        panel.add(headerPanel, BorderLayout.NORTH);

        // --- 2b. Main Content (Tabs) ---
        logTextArea = new JTextArea();
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(Style.FONT_BODY);

        tabbedPane.addTab("Dashboard", createDashboardPanel());
        tabbedPane.addTab("Customers", createCardListPanel("customer"));
        tabbedPane.addTab("Packages", createCardListPanel("package"));
        tabbedPane.addTab("Bookings", createCardListPanel("booking"));
        tabbedPane.addTab("System", createSystemPanel());

        panel.add(tabbedPane, BorderLayout.CENTER);

        // --- 2c. Log Area (REMOVED) ---
        // The log area is now moved to the System panel.

        // We still need to instantiate logTextArea for the redirect

        // We still need to redirect streams so the new log area works
        redirectSystemStreams();
        updateHeaderButtons(); // Initial state

        return panel;
    }

    private void updateHeaderButtons() {
        if (headerLoginButton != null && headerProfileLabel != null) {
            headerLoginButton.setVisible(!isLoggedIn);
            headerProfileLabel.setVisible(isLoggedIn);
        }
    }

    // ====================================================================
    // --- 3. DASHBOARD TAB ---
    // ====================================================================

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBorder(Style.PADDING);
        panel.setBackground(Style.COLOR_LIGHT_GRAY);

        // --- 3a. Stats Cards Panel ---
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        statsPanel.setOpaque(false);

        statCustomers = new JLabel("0");
        statPackages = new JLabel("0");
        statBookings = new JLabel("0");

        statsPanel.add(createStatCard("Total Customers", statCustomers, Style.GRADIENT_YELLOW));
        statsPanel.add(createStatCard("Total Packages", statPackages, Style.GRADIENT_ORANGE));
        statsPanel.add(createStatCard("Total Bookings", statBookings, Style.GRADIENT_PINK));

        panel.add(statsPanel, BorderLayout.NORTH);

        // --- 3b. Graph Panel ---
        graphPanel = new GraphPanel();
        panel.add(graphPanel, BorderLayout.CENTER);

        return panel;
    }

    // Helper for Dashboard: Stat Card
    private JPanel createStatCard(String title, JLabel numberLabel, Color color) {
        JPanel card = new RoundedPanel(Style.BORDER_RADIUS, Style.COLOR_WHITE);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createMatteBorder(8, 0, 0, 0, color)); // Top border as color indicator

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Style.FONT_HEADER.deriveFont(Font.BOLD, 20f));
        titleLabel.setForeground(Style.COLOR_BLACK);
        titleLabel.setBorder(new EmptyBorder(20, 20, 10, 20));

        numberLabel.setFont(Style.FONT_TITLE.deriveFont(Font.BOLD, 48f));
        numberLabel.setForeground(Style.COLOR_BLACK);
        numberLabel.setHorizontalAlignment(JLabel.CENTER);
        numberLabel.setBorder(new EmptyBorder(10, 20, 20, 20));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(numberLabel, BorderLayout.CENTER);
        return card;
    }

    // Helper for Dashboard: Graph Panel
    private class GraphPanel extends JPanel {
        private int[] data = {0, 0, 0};

        public GraphPanel() {
            setBackground(Style.COLOR_WHITE);
            setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Style.COLOR_MEDIUM_GRAY),
                    "System Overview",
                    0, 0, Style.FONT_HEADER, Style.COLOR_BLACK
            ));
        }

        public void updateData(int customers, int packages, int bookings) {
            data[0] = customers;
            data[1] = packages;
            data[2] = bookings;
            repaint(); // Trigger a redraw
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int maxVal = Math.max(10, Math.max(data[0], Math.max(data[1], data[2]))); // Ensure min height
            int width = getWidth();
            int height = getHeight();
            int barWidth = width / 5;
            int gap = width / 10;
            int x = gap;

            String[] labels = {"Customers", "Packages", "Bookings"};
            Color[] colors = {Style.GRADIENT_YELLOW, Style.GRADIENT_ORANGE, Style.GRADIENT_PINK};

            for (int i = 0; i < data.length; i++) {
                int barHeight = (int) (((double)data[i] / maxVal) * (height * 0.75));
                int y = height - barHeight - 50; // 50px for label

                g2.setColor(colors[i]);
                g2.fill(new RoundRectangle2D.Double(x, y, barWidth, barHeight, 20, 20));

                g2.setColor(Style.COLOR_BLACK);
                g2.setFont(Style.FONT_BODY);
                g2.drawString(labels[i], x + barWidth/2 - (g2.getFontMetrics().stringWidth(labels[i])/2), height - 20);

                g2.setFont(Style.FONT_HEADER);
                g2.drawString(String.valueOf(data[i]), x + barWidth/2 - (g2.getFontMetrics().stringWidth(String.valueOf(data[i]))/2), y - 10);

                x += barWidth + gap;
            }
        }
    }

    // ====================================================================
    // --- 4. CARD LIST TABS (Customers, Packages, Bookings) ---
    // ====================================================================

    private JPanel createCardListPanel(String type) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(Style.PADDING);
        panel.setBackground(Style.COLOR_LIGHT_GRAY);

        // --- 4a. The Form Panel (South) ---
        JPanel formPanel = createFormPanel(type);
        panel.add(formPanel, BorderLayout.SOUTH);

        // --- 4b. The Card Container (Center) ---
        JPanel cardContainer = new JPanel();
        cardContainer.setLayout(new BoxLayout(cardContainer, BoxLayout.Y_AXIS));
        cardContainer.setBackground(Style.COLOR_LIGHT_GRAY);

        // Assign the container to the correct global variable
        switch (type) {
            case "customer": customerCardContainer = cardContainer; break;
            case "package": packageCardContainer = cardContainer; break;
            case "booking": bookingCardContainer = cardContainer; break;
        }

        JScrollPane scrollPane = new JScrollPane(cardContainer);
        scrollPane.setBorder(BorderFactory.createLineBorder(Style.COLOR_MEDIUM_GRAY));
        scrollPane.getViewport().setBackground(Style.COLOR_LIGHT_GRAY);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // Helper for Card List: Create appropriate form
    private JPanel createFormPanel(String type) {
        JPanel formPanel = new RoundedPanel(Style.SMALL_BORDER_RADIUS, Style.COLOR_WHITE);
        formPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15)); // Use FlowLayout for dynamic fields
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Style.COLOR_MEDIUM_GRAY),
                "Add New " + type,
                0, 0, Style.FONT_HEADER.deriveFont(Font.BOLD, 18f), Style.COLOR_BLACK
        ));

        switch (type) {
            case "customer":
                JTextField cId = new JTextField(10);
                JTextField cName = new JTextField(15);
                JTextField cEmail = new JTextField(20);
                JTextField cPhone = new JTextField(12);
                RoundedButton cAdd = new RoundedButton("Add Customer");

                formPanel.add(new JLabel("ID:")); formPanel.add(cId);
                formPanel.add(new JLabel("Name:")); formPanel.add(cName);
                formPanel.add(new JLabel("Email:")); formPanel.add(cEmail);
                formPanel.add(new JLabel("Phone:")); formPanel.add(cPhone);
                formPanel.add(cAdd);

                cAdd.addActionListener(e -> {
                    Customer c = new Customer(cId.getText(), cName.getText(), cEmail.getText(), cPhone.getText());
                    travelAgency.addCustomer(c);
                    System.out.println("Added Customer: " + c.getName());
                    refreshAllData();
                    cId.setText(""); cName.setText(""); cEmail.setText(""); cPhone.setText("");
                });
                break;

            case "package":
                // Complex package form, keeping simple for now. Can be expanded similar to previous version.
                formPanel.setLayout(new GridLayout(4, 2, 10, 10)); // For a more structured layout
                JTextField pId = new JTextField();
                JTextField pDest = new JTextField();
                JTextField pPrice = new JTextField();
                JCheckBox pDomestic = new JCheckBox("Domestic (or International)");
                RoundedButton pAdd = new RoundedButton("Add Package");

                formPanel.add(new JLabel("ID:")); formPanel.add(pId);
                formPanel.add(new JLabel("Destination:")); formPanel.add(pDest);
                formPanel.add(new JLabel("Base Price:")); formPanel.add(pPrice);
                formPanel.add(pDomestic); formPanel.add(pAdd);

                pAdd.addActionListener(e -> {
                    try {
                        String id = pId.getText();
                        String dest = pDest.getText();
                        double price = Double.parseDouble(pPrice.getText());
                        TravelPackage pkg;
                        if (pDomestic.isSelected()) {
                            pkg = new DomesticPackage(id, dest, price, "Default State", false); // Simplified
                        } else {
                            pkg = new InternationalPackage(id, dest, price, "Default Country", false, 1); // Simplified
                        }
                        travelAgency.addPackage(pkg);
                        System.out.println("Added Package: " + pkg.getDestination());
                        refreshAllData();
                        pId.setText(""); pDest.setText(""); pPrice.setText("");
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(this, "Price must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                break;

            case "booking":
                formPanel.setLayout(new GridLayout(3, 2, 10, 10));
                JTextField bId = new JTextField();
                JTextField bDate = new JTextField(java.time.LocalDate.now().toString());
                JComboBox<Customer> cBox = new JComboBox<>(customerComboBoxModel); // <-- CHANGED
                JComboBox<TravelPackage> pBox = new JComboBox<>(packageComboBoxModel); // <-- CHANGED
                RoundedButton bAdd = new RoundedButton("Add Booking");

                formPanel.add(new JLabel("ID:")); formPanel.add(bId);
                formPanel.add(new JLabel("Customer:")); formPanel.add(cBox);
                formPanel.add(new JLabel("Date:")); formPanel.add(bDate);
                formPanel.add(new JLabel("Package:")); formPanel.add(pBox);
                formPanel.add(new JLabel()); // Spacer
                formPanel.add(bAdd);

                bAdd.addActionListener(e -> {
                    bookingManager.addBooking(bId.getText(), (Customer)cBox.getSelectedItem(),
                            (TravelPackage)pBox.getSelectedItem(), bDate.getText());
                    System.out.println("Added Booking: " + bId.getText());
                    refreshAllData();
                    bId.setText("");
                });
                break;
        }
        // Style all form components
        for (Component c : formPanel.getComponents()) {
            if (c instanceof JLabel) ((JLabel) c).setFont(Style.FONT_LABEL);
            if (c instanceof JTextField) {
                ((JTextField) c).setFont(Style.FONT_BODY);
                ((JTextField) c).setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Style.COLOR_MEDIUM_GRAY, 1),
                        new EmptyBorder(5, 10, 5, 10)
                ));
            }
            if (c instanceof JComboBox) {
                ((JComboBox) c).setFont(Style.FONT_BODY);
            }
            if (c instanceof JCheckBox) {
                ((JCheckBox) c).setFont(Style.FONT_LABEL);
                ((JCheckBox) c).setOpaque(false);
            }
        }
        return formPanel;
    }

    // Helper for Card List: Create a Customer Card
    private JPanel createCustomerCard(Customer c) {
        JPanel card = new RoundedPanel(Style.BORDER_RADIUS, Style.COLOR_WHITE); // White background, rounded
        card.setLayout(new BorderLayout(15, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                new GradientBorder(Style.BORDER_RADIUS, new Color[] {Style.GRADIENT_YELLOW.darker(), Style.GRADIENT_ORANGE.darker()}, 1.0f, 0.0f), // Gradient border
                new EmptyBorder(15, 20, 15, 20) // Internal padding
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        card.setMinimumSize(new Dimension(200, 100)); // Ensure it's not too small

        JLabel name = new JLabel(c.getName() + " (ID: " + c.getCustomerId() + ")");
        name.setFont(Style.FONT_HEADER);
        name.setIcon(UIManager.getIcon("OptionPane.questionIcon"));
        card.add(name, BorderLayout.NORTH);

        JPanel details = new JPanel(new GridLayout(1, 2));
        details.setOpaque(false);
        JLabel email = new JLabel("Email: " + c.getEmail());
        JLabel phone = new JLabel("Phone: " + c.getPhone());
        email.setFont(Style.FONT_LABEL);
        phone.setFont(Style.FONT_LABEL);
        details.add(email);
        details.add(phone);
        card.add(details, BorderLayout.CENTER);

        return card;
    }

    // Helper for Card List: Create a Package Card
    private JPanel createPackageCard(TravelPackage p) {
        JPanel card = new RoundedPanel(Style.BORDER_RADIUS, Style.COLOR_WHITE);
        card.setLayout(new BorderLayout(15, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                new GradientBorder(Style.BORDER_RADIUS, new Color[] {Style.GRADIENT_ORANGE.darker(), Style.GRADIENT_PINK.darker()}, 1.0f, 0.0f),
                new EmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        card.setMinimumSize(new Dimension(200, 100));

        String type = (p instanceof DomesticPackage) ? "Domestic" : "International";
        JLabel name = new JLabel(p.getDestination() + " [" + type + "]");
        name.setFont(Style.FONT_HEADER);
        name.setIcon(UIManager.getIcon("FileView.floppyDriveIcon"));
        card.add(name, BorderLayout.NORTH);

        JLabel price = new JLabel("Base Price: $" + String.format("%.2f", p.getBasePrice()) + "  Calculated Price: $" + String.format("%.2f", p.calculatePrice()));
        price.setFont(Style.FONT_LABEL);
        card.add(price, BorderLayout.SOUTH);

        return card;
    }

    // Helper for Card List: Create a Booking Card
    private JPanel createBookingCard(Booking b) {
        JPanel card = new RoundedPanel(Style.BORDER_RADIUS, Style.COLOR_WHITE);
        card.setLayout(new BorderLayout(15, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
                new GradientBorder(Style.BORDER_RADIUS, new Color[] {Style.GRADIENT_PINK.darker(), Style.GRADIENT_PURPLE.darker()}, 1.0f, 0.0f),
                new EmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        card.setMinimumSize(new Dimension(200, 100));

        JLabel idCustomer = new JLabel(b.getBookingId() + " - " + b.getCustomer().getName());
        idCustomer.setFont(Style.FONT_HEADER);
        idCustomer.setIcon(UIManager.getIcon("FileView.computerIcon"));
        card.add(idCustomer, BorderLayout.NORTH);

        JPanel detailsAndActions = new JPanel(new BorderLayout(10, 0));
        detailsAndActions.setOpaque(false);

        JPanel details = new JPanel(new GridLayout(2, 1));
        details.setOpaque(false);
        JLabel pkgDest = new JLabel("Package: " + b.getTravelPackage().getDestination());
        JLabel dateStatus = new JLabel("Date: " + b.getBookingDate() + " | Status: " + b.getPaymentStatus());
        pkgDest.setFont(Style.FONT_LABEL);
        dateStatus.setFont(Style.FONT_LABEL);
        details.add(pkgDest);
        details.add(dateStatus);
        detailsAndActions.add(details, BorderLayout.CENTER);

        RoundedButton processBtn = new RoundedButton("Process Payment");
        processBtn.setFont(Style.FONT_SMALL_LABEL);
        processBtn.setPreferredSize(new Dimension(160, 40)); // Fixed size for consistency

        if (!"Pending".equals(b.getPaymentStatus())) {
            processBtn.setText("Processed");
            processBtn.setEnabled(false);
            processBtn.setBackground(Style.COLOR_MEDIUM_GRAY); // Grey out processed button
        }
        processBtn.addActionListener(e -> {
            processBtn.setText("Processing...");
            processBtn.setEnabled(false);
            processBtn.setBackground(Style.COLOR_MEDIUM_GRAY); // Keep grey during processing
            BookingProcessorThread thread = new BookingProcessorThread(b);
            thread.start();
            Timer timer = new Timer(2500, evt -> refreshAllData());
            timer.setRepeats(false);
            timer.start();
        });
        detailsAndActions.add(processBtn, BorderLayout.EAST);

        card.add(detailsAndActions, BorderLayout.CENTER); // Add the combined panel to the card

        return card;
    }

    // ====================================================================
    // --- 5. SYSTEM TAB ---
    // ====================================================================

// ====================================================================
    // --- 5. SYSTEM TAB ---
    // ====================================================================

    private JPanel createSystemPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20)); // Gaps
        panel.setBorder(Style.PADDING);
        panel.setBackground(Style.COLOR_LIGHT_GRAY);

        // --- NEW: Log Card (Left Side) ---

        // 1. Create the subtle gradient for the card's background
        Paint logGradient = new LinearGradientPaint(
                new Point2D.Float(0, 0),
                new Point2D.Float(0, 300), // Gradient height
                new float[]{0.0f, 1.0f},
                new Color[]{new Color(255, 255, 255, 220), new Color(245, 245, 245, 200)} // Semi-transparent white to light-gray
        );

        // 2. Create the rounded panel with the gradient
        JPanel logCard = new RoundedPanel(Style.SMALL_BORDER_RADIUS, logGradient);
        logCard.setLayout(new BorderLayout());
        logCard.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10), // Padding
                "System Log",
                0, 0, Style.FONT_HEADER.deriveFont(Font.BOLD, 18f), Style.COLOR_BLACK
        ));

        // 3. Configure the logTextArea (it was already created as a class field)
        logTextArea.setFont(Style.FONT_SMALL_LABEL);
        logTextArea.setForeground(Style.COLOR_BLACK); // Black font
        logTextArea.setOpaque(false); // Make it transparent
        logTextArea.setEditable(false);

        // 4. Configure the scroll pane
        JScrollPane logScrollPane = new JScrollPane(logTextArea);
        logScrollPane.setOpaque(false); // Make scroll pane transparent
        logScrollPane.getViewport().setOpaque(false); // Make viewport transparent
        logScrollPane.setBorder(BorderFactory.createEmptyBorder()); // No border

        // 5. Add scroll pane to the card
        logCard.add(logScrollPane, BorderLayout.CENTER);

        // --- Button Container (Right Side) ---
        JPanel buttonContainer = new RoundedPanel(Style.SMALL_BORDER_RADIUS, Style.COLOR_WHITE);
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS)); // Vertical layout
        buttonContainer.setBorder(new EmptyBorder(40, 40, 40, 40));

        RoundedButton saveButton = new RoundedButton("Save Bookings to File");
        RoundedButton loadButton = new RoundedButton("Read Bookings from File");
        RoundedButton statsButton = new RoundedButton("Show Agency Statistics");

        // Center-align buttons
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        statsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonContainer.add(saveButton);
        buttonContainer.add(Box.createRigidArea(new Dimension(0, 25))); // Space between buttons
        buttonContainer.add(loadButton);
        buttonContainer.add(Box.createRigidArea(new Dimension(0, 25)));
        buttonContainer.add(statsButton);

        // Add a wrapper panel to prevent buttons from stretching
        JPanel eastPanel = new JPanel(new GridBagLayout()); // Use GridBag to center
        eastPanel.setOpaque(false);
        eastPanel.add(buttonContainer);

        // Add both main components to the panel
        panel.add(logCard, BorderLayout.CENTER);
        panel.add(eastPanel, BorderLayout.EAST);

        saveButton.addActionListener(e -> {
            FileHandler.saveBookingsToFile(bookingManager.getAllBookings(), "bookings.txt");
        });
        loadButton.addActionListener(e -> {
            FileHandler.readBookingsFromFile("bookings.txt");
        });
        statsButton.addActionListener(e -> {
            travelAgency.displayStatistics();
        });

        return panel;
    }

    // ====================================================================
    // --- 6. CUSTOM STYLED COMPONENTS ---
    // ====================================================================

    // --- Custom Gradient Panel for backgrounds ---
    private class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create(); // Use a copy
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            Point2D start = new Point2D.Float(0, 0);
            Point2D end = new Point2D.Float(w, h);
            float[] fractions = {0.0f, 0.33f, 0.66f, 1.0f};
            Color[] colors = {Style.GRADIENT_YELLOW, Style.GRADIENT_ORANGE, Style.GRADIENT_PINK, Style.GRADIENT_PURPLE};
            LinearGradientPaint gp = new LinearGradientPaint(start, end, fractions, colors);

            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
            g2d.dispose();
        }
    }

    // --- Custom Rounded Panel for cards and login box ---
// --- Custom Rounded Panel for cards and login box ---
    private class RoundedPanel extends JPanel {
        private int cornerRadius;
        private Paint backgroundPaint; // Changed from Color to Paint

        public RoundedPanel(int cornerRadius, Color backgroundColor) {
            this.cornerRadius = cornerRadius;
            this.backgroundPaint = backgroundColor; // Store as Paint
            setOpaque(false);
        }

        // --- NEW CONSTRUCTOR ---
        // Add this new constructor to accept gradients
        public RoundedPanel(int cornerRadius, Paint backgroundPaint) {
            this.cornerRadius = cornerRadius;
            this.backgroundPaint = backgroundPaint;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setPaint(backgroundPaint); // Use setPaint
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
            g2.dispose();
        }
    }

    // --- Custom Rounded Button with Gradient ---
    private class RoundedButton extends JButton {
        private Color[] gradientColors = {Style.GRADIENT_PINK.brighter(), Style.GRADIENT_PURPLE};
        private Color[] hoverColors = {Style.GRADIENT_ORANGE, Style.GRADIENT_PINK};
        private Color[] pressedColors = {Style.GRADIENT_YELLOW, Style.GRADIENT_ORANGE};

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorder(new EmptyBorder(12, 25, 12, 25)); // Bigger padding
            setFont(Style.FONT_BUTTON);
            setForeground(Style.COLOR_WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    if (isEnabled()) {
                        currentColors = hoverColors;
                        repaint();
                    }
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    if (isEnabled()) {
                        currentColors = gradientColors;
                        repaint();
                    }
                }
                @Override
                public void mousePressed(MouseEvent e) {
                    if (isEnabled()) {
                        currentColors = pressedColors;
                        repaint();
                    }
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (isEnabled()) {
                        currentColors = hoverColors; // Return to hover state after release
                        repaint();
                    }
                }
            });
            currentColors = gradientColors; // Initial colors
        }

        private Color[] currentColors; // Store current colors for painting

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (!isEnabled()) {
                g2.setColor(Style.COLOR_MEDIUM_GRAY);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), Style.BORDER_RADIUS, Style.BORDER_RADIUS));
            } else {
                Point2D start = new Point2D.Float(0, 0);
                Point2D end = new Point2D.Float(getWidth(), getHeight());
                LinearGradientPaint gp = new LinearGradientPaint(start, end, new float[]{0f, 1f}, currentColors);
                g2.setPaint(gp);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), Style.BORDER_RADIUS, Style.BORDER_RADIUS));
            }

            g2.dispose();
            super.paintComponent(g); // Paint text and icon
        }
    }

    // --- Custom Gradient Border for cards ---
    private class GradientBorder implements Border {
        private int radius;
        private Color[] colors;
        private float x1, y1, x2, y2; // Start and end points for gradient vector
        private int borderWidth = 3;

        public GradientBorder(int radius, Color[] colors, float x1, float y1) {
            this.radius = radius;
            this.colors = colors;
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x1 + 1; // Small offset to create a horizontal gradient
            this.y2 = y1;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Create a slightly larger path for the outer border
            RoundRectangle2D outerRect = new RoundRectangle2D.Double(x, y, width, height, radius, radius);

            // Create a path for the inner area
            RoundRectangle2D innerRect = new RoundRectangle2D.Double(
                    x + borderWidth, y + borderWidth,
                    width - borderWidth * 2, height - borderWidth * 2,
                    radius - borderWidth, radius - borderWidth
            );

            // Use Area to create the border shape
            java.awt.geom.Area borderArea = new java.awt.geom.Area(outerRect);
            borderArea.subtract(new java.awt.geom.Area(innerRect));

            // Define the gradient across the full component
            Point2D start = new Point2D.Float(0, 0);
            Point2D end = new Point2D.Float(c.getWidth(), c.getHeight());
            LinearGradientPaint gp = new LinearGradientPaint(start, end, new float[]{0f, 1f}, colors);
            g2d.setPaint(gp);
            g2d.fill(borderArea);

            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(borderWidth, borderWidth, borderWidth, borderWidth);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }

    // --- NEW: Custom Rounded Line Border for text fields ---
    private class RoundedLineBorder extends EmptyBorder {
        private int radius;
        private Color color;
        private int thickness;

        public RoundedLineBorder(Color color, int thickness, int radius, Insets insets) {
            super(insets);
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));

            // Draw the rounded rectangle
            // The -thickness is to keep the border "inside" the component's bounds
            g2.draw(new RoundRectangle2D.Double(x + thickness/2.0, y + thickness/2.0,
                    width - thickness, height - thickness,
                    radius, radius));
            g2.dispose();
        }
    }
    // ====================================================================
    // --- 7. DATA & UTILITY METHODS ---
    // ====================================================================

    private void populateInitialData() {
        System.out.println("Populating initial data...");
        Customer c1 = new Customer("C001", "John Doe", "john@email.com", "123-456-7890");
        Customer c2 = new Customer("C002", "Jane Smith", "jane@email.com", "098-765-4321");
        travelAgency.addCustomer(c1);
        travelAgency.addCustomer(c2);

        TravelPackage pkg1 = new DomesticPackage("PKG001", "Goa", 500, "Goa", true);
        TravelPackage pkg2 = new InternationalPackage("PKG002", "Paris", 2000, "France", true, 2);
        TravelPackage pkg3 = new DomesticPackage("PKG003", "Kerala", 600, "Kerala", false);
        travelAgency.addPackage(pkg1);
        travelAgency.addPackage(pkg2);
        travelAgency.addPackage(pkg3);

        Booking b1 = new Booking("BK001", c1, pkg1, "2025-11-01");
        bookingManager.addBooking(b1);
        bookingManager.addBooking("BK002", c2, pkg2, "2025-11-15");
        System.out.println("Initial data populated.");
    }

    private void refreshAllData() {
        System.out.println("Refreshing all GUI data...");

        // --- 1. Get latest data ---
        Collection<Customer> customers = travelAgency.getCustomers();
        List<TravelPackage> packages = travelAgency.getPackages();
        List<Booking> bookings = bookingManager.getAllBookings();

        // --- 2. Update Dashboard Stats ---
        int cCount = customers.size();
        int pCount = packages.size();
        int bCount = bookings.size();

        statCustomers.setText(String.valueOf(cCount));
        statPackages.setText(String.valueOf(pCount));
        statBookings.setText(String.valueOf(bCount));

        // --- 3. Update Dashboard Graph ---
        if (graphPanel != null) {
            graphPanel.updateData(cCount, pCount, bCount);
        }

        // --- 4. Update Customer Cards ---
        if (customerCardContainer != null) {
            customerCardContainer.removeAll();
            for (Customer c : customers) {
                customerCardContainer.add(createCustomerCard(c));
                customerCardContainer.add(Box.createRigidArea(new Dimension(0, 15))); // More space between cards
            }
        }

        // --- 5. Update Package Cards ---
        if (packageCardContainer != null) {
            packageCardContainer.removeAll();
            for (TravelPackage p : packages) {
                packageCardContainer.add(createPackageCard(p));
                packageCardContainer.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

        // --- 6. Update Booking Cards ---
        if (bookingCardContainer != null) {
            bookingCardContainer.removeAll();
            for (Booking b : bookings) {
                bookingCardContainer.add(createBookingCard(b));
                bookingCardContainer.add(Box.createRigidArea(new Dimension(0, 15)));
            }
        }

// --- 7. Refresh ComboBoxes in Booking Form ---
        customerComboBoxModel.removeAllElements();
        packageComboBoxModel.removeAllElements();

        for (Customer c : customers) {
            customerComboBoxModel.addElement(c);
        }
        for (TravelPackage p : packages) {
            packageComboBoxModel.addElement(p);
        }

        // --- 8. Repaint all ---
        revalidate();
        repaint();
        System.out.println("GUI refresh complete.");
    }

    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                updateLog(String.valueOf((char) b));
            }
            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                updateLog(new String(b, off, len));
            }
            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };
        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }

    private void updateLog(String text) {
        SwingUtilities.invokeLater(() -> {
            if (logTextArea != null) {
                logTextArea.append(text);
                logTextArea.setCaretPosition(logTextArea.getDocument().getLength());
            }
        });
    }

    // ====================================================================
    // --- 8. MAIN METHOD ---
    // ====================================================================

    public static void main(String[] args) {
        try {
            // Use Nimbus L&F as a base for a modern look, then customize
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set global font styles for consistency
        UIManager.put("Label.font", Style.FONT_LABEL);
        UIManager.put("TextField.font", Style.FONT_BODY);
        UIManager.put("PasswordField.font", Style.FONT_BODY);
        UIManager.put("ComboBox.font", Style.FONT_BODY);
        UIManager.put("TabbedPane.font", Style.FONT_BODY);
        UIManager.put("Button.font", Style.FONT_BUTTON); // Ensure default buttons also have big font
        UIManager.put("CheckBox.font", Style.FONT_LABEL);
        UIManager.put("TitledBorder.font", Style.FONT_HEADER.deriveFont(Font.BOLD, 18f));

        // Fix for JCheckBox text cutting off with custom L&F
        // This provides a standard icon to help the layout manager calculate size correctly
        UIManager.put("CheckBox.icon", new javax.swing.plaf.metal.MetalCheckBoxIcon());

        // Run the GUI
        SwingUtilities.invokeLater(() -> {
            TravelGui gui = new TravelGui();
            gui.setLocationRelativeTo(null); // Center the window
            gui.setVisible(true);
        });
    }
} // This is the final closing brace for the Trave