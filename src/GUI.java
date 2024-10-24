import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI
{
    private JFrame frame;
    private JPanel homePanel;
    private JPanel mainPanel;
    private JTextField classNameField;
    private JPanel classPanel;

    Data data = new Data();

    public GUI(Data data)
    {
        this.data = data;
    }

    public void Run()
    {
        // create a new frame
        frame = new JFrame("UML Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new CardLayout()); // cardlayout for multiple 'screens'

        // home panel
        homePanel = createHomePanel();
        //  main panel (edit panel)
        mainPanel = createMainPanel();

        // add both panels to the frame
        frame.add(homePanel, "Home");
        frame.add(mainPanel, "Main");

        frame.setSize(800, 600);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    

        //Create a menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create the "File" menu
        JMenu fileMenu = new JMenu("File");

        // Create "Load" menu item
        JMenuItem loadItem = new JMenuItem("Load");
        loadItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Load functionality here
                JOptionPane.showMessageDialog(frame, "Load action triggered");
            }
        });

        // Create "Save" menu item
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Save functionality here
                JOptionPane.showMessageDialog(frame, "Save action triggered");
            }
        });

        // Create "Exit" menu item
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Add items to the file menu
        fileMenu.add(loadItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator(); // Adds a separator line
        fileMenu.add(exitItem);

        // Add the file menu to the menu bar
        menuBar.add(fileMenu);

        // Set the menu bar on the frame
        frame.setJMenuBar(menuBar);
    }

    //function for creating home panel, has options to load uml or create new
    private JPanel createHomePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1)); // layout for buttons

        JButton newDiagramButton = new JButton("Create New UML Diagram");
        JButton loadDiagramButton = new JButton("Load UML Diagram");

        //new button pressed
        newDiagramButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // switches to main page for new UML
                CardLayout cl = (CardLayout) frame.getContentPane().getLayout();
                cl.show(frame.getContentPane(), "Main");
            }
        });

        // load button pressed
        loadDiagramButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Load UML Diagram clicked");
                // load logic
            }
        });

        JLabel homeLabel = new JLabel("Unhandled Exceptions UML Editor");

        panel.add(homeLabel);
        panel.add(newDiagramButton);
        panel.add(loadDiagramButton);

        return panel;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Input field for class name
        classNameField = new JTextField(15);
        JButton addClassButton = new JButton("Add Class");
        addClassButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String className = classNameField.getText();
                //format string [] for command parsing
                String [] command = new String[2];
                command[0] = "addclass";
                command[1] = className.trim();
                //send command to controller
                //CLI.CommandParsing(command);
                System.out.println("Adding class: " + className);
                classNameField.setText(""); // clears input box
                //update view??
            }
        });

        // panel that displays a class
        classPanel = new JPanel();
        classPanel.setLayout(new BoxLayout(classPanel, BoxLayout.Y_AXIS));

        // adds components to class panel
        JPanel inputPanel = new JPanel();
        inputPanel.add(classNameField);
        inputPanel.add(addClassButton);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(classPanel), BorderLayout.CENTER);

        return panel;
    }

}
