import javax.swing.*;
import java.awt.*;

public class GUI
{
    Data data = new Data();

    public GUI(Data data)
    {
        this.data = data;
    }

    public void Run()
    {//game loop for swift?
        //System.out.println("Can this be reached?");
        //MOVE THIS CODE TO INIT FUNCTION AND WORK INTO A GAME LOOP TO STOP PRINTING INFINITELY
        JFrame frame = new JFrame();
        JButton button = new JButton("Click me");
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 1));
        panel.add(button);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Unhandled Exceptions UML");
        frame.pack();
        frame.setVisible(true);

    }
}
