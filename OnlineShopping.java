import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class OnlineShopping extends JFrame {

    // ========== DATA ==========
    static class Product {
        int id;
        String name, category, description;
        double price;
        double rating;

        Product(int id, String name, String category, double price, double rating, String description) {
            this.id = id;
            this.name = name;
            this.category = category;
            this.price = price;
            this.rating = rating;
            this.description = description;
        }
    }

    static class CartItem {
        Product product;
        int quantity;

        CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
    }

    private List<Product> products = new ArrayList<>();
    private List<CartItem> cart = new ArrayList<>();

    // UI Components
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel productGridPanel;
    private JPanel cartPanel;
    private JPanel orderPanel;
    private JLabel cartCountLabel;
    private JTextField searchField;

    public OnlineShopping() {
        setTitle("ShopEase - Online Shopping Platform");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 600));

        initProducts();
        initUI();
        showHome();
    }

    // ========== SAMPLE PRODUCTS ==========
    private void initProducts() {
        products.add(new Product(1, "Wireless Bluetooth Headphones", "Electronics", 2499, 4.5,
                "Premium wireless headphones with noise cancellation and 30-hour battery."));
        products.add(new Product(2, "Smart Watch Pro", "Electronics", 5999, 4.7,
                "Feature-rich smartwatch with heart rate, GPS and 7-day battery."));
        products.add(new Product(3, "Men's Casual Sneakers", "Fashion", 1899, 4.3,
                "Comfortable breathable sneakers with cushioned sole."));
        products.add(new Product(4, "Women's Summer Dress", "Fashion", 1299, 4.6,
                "Elegant floral cotton dress, lightweight and perfect for summer."));
        products.add(new Product(5, "Modern Table Lamp", "Home", 999, 4.4,
                "Minimalist LED lamp with adjustable brightness."));
        products.add(new Product(6, "Yoga Mat Premium", "Sports", 799, 4.8,
                "Extra thick non-slip eco-friendly yoga mat."));
        products.add(new Product(7, "Wireless Mouse", "Electronics", 699, 4.2,
                "Ergonomic silent wireless mouse with adjustable DPI."));
        products.add(new Product(8, "Cotton T-Shirt Pack", "Fashion", 999, 4.5,
                "Pack of 3 premium soft cotton t-shirts."));
        products.add(new Product(9, "Ceramic Coffee Mug Set", "Home", 599, 4.6,
                "Set of 4 stylish microwave-safe ceramic mugs."));
        products.add(new Product(10, "Dumbbell Set 10kg", "Sports", 1499, 4.7,
                "Adjustable dumbbell set for home workouts."));
        products.add(new Product(11, "Portable Bluetooth Speaker", "Electronics", 1799, 4.4,
                "Waterproof speaker with powerful bass and 12-hour playtime."));
        products.add(new Product(12, "Leather Wallet", "Fashion", 899, 4.3,
                "Genuine leather RFID-protected bifold wallet."));
    }

    // ========== UI SETUP ==========
    private void initUI() {
        // Top Navigation Bar
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBackground(Color.WHITE);
        navBar.setBorder(new EmptyBorder(10, 20, 10, 20));
        navBar.setPreferredSize(new Dimension(0, 65));

        JLabel logo = new JLabel("ShopEase");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        logo.setForeground(new Color(37, 99, 235));
        logo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logo.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showHome();
            }
        });

        // Search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        searchPanel.setOpaque(false);
        searchField = new JTextField(25);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setPreferredSize(new Dimension(280, 35));
        JButton searchBtn = createStyledButton("Search", new Color(37, 99, 235));
        searchBtn.addActionListener(e -> searchProducts());
        searchField.addActionListener(e -> searchProducts());
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);

        // Cart button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        JButton homeBtn = createStyledButton("Home", new Color(100, 116, 139));
        homeBtn.addActionListener(e -> showHome());

        JButton cartBtn = createStyledButton("Cart", new Color(37, 99, 235));
        cartCountLabel = new JLabel("0");
        cartCountLabel.setForeground(Color.WHITE);
        cartCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cartCountLabel.setOpaque(true);
        cartCountLabel.setBackground(new Color(239, 68, 68));
        cartCountLabel.setBorder(new EmptyBorder(2, 7, 2, 7));

        JPanel cartBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        cartBtnPanel.setOpaque(false);
        cartBtnPanel.add(cartBtn);
        cartBtnPanel.add(cartCountLabel);

        cartBtn.addActionListener(e -> showCart());

        rightPanel.add(homeBtn);
        rightPanel.add(cartBtnPanel);

        navBar.add(logo, BorderLayout.WEST);
        navBar.add(searchPanel, BorderLayout.CENTER);
        navBar.add(rightPanel, BorderLayout.EAST);

        // Main content with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(241, 245, 249));

        // Home / Products page
        JPanel homePage = createHomePage();
        mainPanel.add(homePage, "HOME");

        // Cart page
        cartPanel = new JPanel(new BorderLayout());
        cartPanel.setBackground(new Color(241, 245, 249));
        mainPanel.add(cartPanel, "CART");

        orderPanel = new JPanel(new BorderLayout());
        orderPanel.setBackground(new Color(241, 245, 249));
        mainPanel.add(orderPanel, "ORDER_SUCCESS");

        // Layout
        setLayout(new BorderLayout());
        add(navBar, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // Footer
        JLabel footer = new JLabel("© 2026 ShopEase  |  Java Swing Frontend Project", SwingConstants.CENTER);
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.setForeground(Color.WHITE);
        footer.setOpaque(true);
        footer.setBackground(new Color(30, 41, 59));
        footer.setBorder(new EmptyBorder(12, 0, 12, 0));
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel createHomePage() {
        JPanel page = new JPanel(new BorderLayout());
        page.setBackground(new Color(241, 245, 249));

        // Categories
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        categoryPanel.setBackground(new Color(241, 245, 249));
        categoryPanel.setBorder(new EmptyBorder(15, 0, 5, 0));

        String[] categories = {"All", "Electronics", "Fashion", "Home", "Sports"};
        for (String cat : categories) {
            JButton btn = createStyledButton(cat, new Color(37, 99, 235));
            btn.addActionListener(e -> filterByCategory(cat));
            categoryPanel.add(btn);
        }

        // Product Grid
        productGridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        productGridPanel.setBackground(new Color(241, 245, 249));
        productGridPanel.setBorder(new EmptyBorder(10, 25, 25, 25));

        JScrollPane scroll = new JScrollPane(productGridPanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(new Color(241, 245, 249));

        page.add(categoryPanel, BorderLayout.NORTH);
        page.add(scroll, BorderLayout.CENTER);

        return page;
    }

    // ========== RENDER PRODUCTS ==========
    private void displayProducts(List<Product> list) {
        productGridPanel.removeAll();

        if (list.isEmpty()) {
            JLabel empty = new JLabel("No products found", SwingConstants.CENTER);
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            productGridPanel.add(empty);
        } else {
            for (Product p : list) {
                productGridPanel.add(createProductCard(p));
            }
        }

        productGridPanel.revalidate();
        productGridPanel.repaint();
    }

    private JPanel createProductCard(Product p) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(226, 232, 240), 1, true),
                new EmptyBorder(15, 15, 15, 15)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Icon / Image placeholder
        JLabel icon = new JLabel(getIconForCategory(p.category), SwingConstants.CENTER);
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
        icon.setBorder(new EmptyBorder(10, 0, 10, 0));

        JLabel name = new JLabel("<html><center>" + p.name + "</center></html>");
        name.setFont(new Font("Segoe UI", Font.BOLD, 14));
        name.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel category = new JLabel(p.category);
        category.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        category.setForeground(new Color(100, 116, 139));
        category.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel rating = new JLabel(String.format("★ %.1f", p.rating));
        rating.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rating.setForeground(new Color(245, 158, 11));
        rating.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel price = new JLabel("₹" + String.format("%,.0f", p.price));
        price.setFont(new Font("Segoe UI", Font.BOLD, 18));
        price.setForeground(new Color(37, 99, 235));
        price.setAlignmentX(Component.CENTER_ALIGNMENT);
        price.setBorder(new EmptyBorder(8, 0, 10, 0));

        JButton addBtn = createStyledButton("Add to Cart", new Color(37, 99, 235));
        addBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        addBtn.addActionListener(e -> {
            addToCart(p);
            JOptionPane.showMessageDialog(this, p.name + " added to cart!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        card.add(icon);
        card.add(name);
        card.add(Box.createVerticalStrut(4));
        card.add(category);
        card.add(Box.createVerticalStrut(4));
        card.add(rating);
        card.add(price);
        card.add(addBtn);

        // Click card to view details
        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                showProductDetails(p);
            }
        });

        return card;
    }

    private String getIconForCategory(String cat) {
        switch (cat) {
            case "Electronics": return "🎧";
            case "Fashion": return "👕";
            case "Home": return "🏠";
            case "Sports": return "⚽";
            default: return "📦";
        }
    }

    // ========== CART LOGIC ==========
    private void addToCart(Product product) {
        for (CartItem item : cart) {
            if (item.product.id == product.id) {
                item.quantity++;
                updateCartCount();
                return;
            }
        }
        cart.add(new CartItem(product, 1));
        updateCartCount();
    }

    private void updateCartCount() {
        int total = 0;
        for (CartItem item : cart) {
            total += item.quantity;
        }
        cartCountLabel.setText(String.valueOf(total));
    }

    private void showCart() {
        cartPanel.removeAll();

        if (cart.isEmpty()) {
            JLabel empty = new JLabel("<html><center><h2>Your cart is empty</h2><br>Add some products to get started!</center></html>",
                    SwingConstants.CENTER);
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            cartPanel.add(empty, BorderLayout.CENTER);
        } else {
            JPanel itemsPanel = new JPanel();
            itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
            itemsPanel.setBackground(new Color(241, 245, 249));
            itemsPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

            double total = 0;

            for (CartItem item : cart) {
                JPanel row = new JPanel(new BorderLayout(15, 0));
                row.setBackground(Color.WHITE);
                row.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(226, 232, 240), 1, true),
                        new EmptyBorder(12, 15, 12, 15)
                ));
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

                JLabel info = new JLabel("<html><b>" + item.product.name + "</b><br>" +
                        "₹" + String.format("%,.0f", item.product.price) + " × " + item.quantity +
                        " = <b>₹" + String.format("%,.0f", item.product.price * item.quantity) + "</b></html>");
                info.setFont(new Font("Segoe UI", Font.PLAIN, 14));

                JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
                actions.setOpaque(false);

                JButton minus = createStyledButton("−", new Color(100, 116, 139));
                JButton plus = createStyledButton("+", new Color(100, 116, 139));
                JButton remove = createStyledButton("Remove", new Color(239, 68, 68));

                minus.addActionListener(e -> {
                    item.quantity--;
                    if (item.quantity <= 0) cart.remove(item);
                    updateCartCount();
                    showCart();
                });
                plus.addActionListener(e -> {
                    item.quantity++;
                    updateCartCount();
                    showCart();
                });
                remove.addActionListener(e -> {
                    cart.remove(item);
                    updateCartCount();
                    showCart();
                });

                actions.add(minus);
                actions.add(new JLabel(String.valueOf(item.quantity)));
                actions.add(plus);
                actions.add(remove);

                row.add(info, BorderLayout.CENTER);
                row.add(actions, BorderLayout.EAST);

                itemsPanel.add(row);
                itemsPanel.add(Box.createVerticalStrut(10));

                total += item.product.price * item.quantity;
            }

            // Summary
            double shipping = total > 2000 ? 0 : 99;
            JPanel summary = new JPanel();
            summary.setLayout(new BoxLayout(summary, BoxLayout.Y_AXIS));
            summary.setBackground(Color.WHITE);
            summary.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(226, 232, 240), 1, true),
                    new EmptyBorder(20, 25, 20, 25)
            ));
            summary.setMaximumSize(new Dimension(400, 200));
            summary.setAlignmentX(Component.RIGHT_ALIGNMENT);

            summary.add(new JLabel("Subtotal:  ₹" + String.format("%,.0f", total)));
            summary.add(Box.createVerticalStrut(8));
            summary.add(new JLabel("Shipping:  ₹" + String.format("%,.0f", shipping)));
            summary.add(Box.createVerticalStrut(12));
            JLabel totalLabel = new JLabel("Total:  ₹" + String.format("%,.0f", total + shipping));
            totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            totalLabel.setForeground(new Color(37, 99, 235));
            summary.add(totalLabel);
            summary.add(Box.createVerticalStrut(15));

            JButton checkout = createStyledButton("Proceed to Checkout", new Color(16, 185, 129));
            checkout.setAlignmentX(Component.CENTER_ALIGNMENT);
            double orderTotal = total + shipping;
            checkout.addActionListener(e -> {
                cart.clear();
                updateCartCount();
                showOrderConfirmation(orderTotal);
            });
            summary.add(checkout);

            itemsPanel.add(Box.createVerticalStrut(20));
            itemsPanel.add(summary);

            JScrollPane scroll = new JScrollPane(itemsPanel);
            scroll.setBorder(null);
            scroll.getVerticalScrollBar().setUnitIncrement(16);
            cartPanel.add(scroll, BorderLayout.CENTER);
        }

        cartPanel.revalidate();
        cartPanel.repaint();
        cardLayout.show(mainPanel, "CART");
    }

    private void showOrderConfirmation(double orderTotal) {
        orderPanel.removeAll();

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(15, 23, 42));
        content.setBorder(new EmptyBorder(35, 40, 35, 40));

        JLabel heading = new JLabel("Order confirmed");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(new Color(248, 250, 252));
        heading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Thanks for shopping with ShopEase. We're getting your order ready.");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(203, 213, 225));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel status = new JLabel("✓  ORDER PLACED");
        status.setFont(new Font("Segoe UI", Font.BOLD, 13));
        status.setForeground(new Color(134, 239, 172));
        status.setOpaque(true);
        status.setBackground(new Color(20, 83, 45));
        status.setBorder(new EmptyBorder(9, 16, 9, 16));
        status.setAlignmentX(Component.CENTER_ALIGNMENT);

        String orderId = "SE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        JLabel reference = new JLabel("Order reference: " + orderId);
        reference.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        reference.setForeground(new Color(148, 163, 184));
        reference.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel summary = new JPanel(new GridLayout(0, 2, 15, 12));
        summary.setBackground(new Color(30, 41, 59));
        summary.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(71, 85, 105), 1, true),
                new EmptyBorder(22, 25, 22, 25)
        ));
        summary.setMaximumSize(new Dimension(560, 180));

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE, d MMM");
        summary.add(createOrderDetailLabel("Estimated delivery", false));
        summary.add(createOrderDetailLabel(
                LocalDate.now().plusDays(3).format(dateFormat) + " - " +
                        LocalDate.now().plusDays(5).format(dateFormat), true));
        summary.add(createOrderDetailLabel("Order status", false));
        summary.add(createOrderDetailLabel("Preparing for shipment", true));
        summary.add(createOrderDetailLabel("Total paid", false));
        summary.add(createOrderDetailLabel("₹" + String.format("%,.0f", orderTotal), true));

        JButton continueShopping = createStyledButton("Continue Shopping", new Color(37, 99, 235));
        continueShopping.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueShopping.addActionListener(e -> showHome());

        content.add(status);
        content.add(Box.createVerticalStrut(18));
        content.add(heading);
        content.add(Box.createVerticalStrut(8));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(8));
        content.add(reference);
        content.add(Box.createVerticalStrut(28));
        content.add(summary);
        content.add(Box.createVerticalStrut(24));
        content.add(continueShopping);

        orderPanel.add(content, BorderLayout.CENTER);
        orderPanel.revalidate();
        orderPanel.repaint();
        cardLayout.show(mainPanel, "ORDER_SUCCESS");
    }

    private JLabel createOrderDetailLabel(String text, boolean emphasized) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", emphasized ? Font.BOLD : Font.PLAIN, 14));
        label.setForeground(emphasized ? new Color(248, 250, 252) : new Color(203, 213, 225));
        return label;
    }

    // ========== HELPERS ==========
    private void showHome() {
        displayProducts(products);
        cardLayout.show(mainPanel, "HOME");
    }

    private void filterByCategory(String category) {
        if (category.equals("All")) {
            displayProducts(products);
        } else {
            List<Product> filtered = new ArrayList<>();
            for (Product p : products) {
                if (p.category.equals(category)) filtered.add(p);
            }
            displayProducts(filtered);
        }
        cardLayout.show(mainPanel, "HOME");
    }

    private void searchProducts() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            displayProducts(products);
            return;
        }
        List<Product> filtered = new ArrayList<>();
        for (Product p : products) {
            if (p.name.toLowerCase().contains(query) || p.category.toLowerCase().contains(query)) {
                filtered.add(p);
            }
        }
        displayProducts(filtered);
        cardLayout.show(mainPanel, "HOME");
    }

    private void showProductDetails(Product p) {
        String msg = "<html><body style='width:300px'>" +
                "<h2>" + p.name + "</h2>" +
                "<p><b>Category:</b> " + p.category + "</p>" +
                "<p><b>Price:</b> ₹" + String.format("%,.0f", p.price) + "</p>" +
                "<p><b>Rating:</b> ★ " + p.rating + "</p><br>" +
                "<p>" + p.description + "</p></body></html>";

        int choice = JOptionPane.showConfirmDialog(this, msg, "Product Details",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (choice == JOptionPane.OK_OPTION) {
            addToCart(p);
            JOptionPane.showMessageDialog(this, "Added to cart!");
        }
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ========== MAIN ==========
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            OnlineShopping app = new OnlineShopping();
            app.setVisible(true);
        });
    }
}