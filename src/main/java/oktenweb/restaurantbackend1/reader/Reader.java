package oktenweb.restaurantbackend1.reader;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Reader {

    public void reply(String reply){

        final JFrame frame = new JFrame("Error inserting data");
        frame.setSize(200, 100);
        frame.setLocationRelativeTo(null);
        JButton button = new JButton(reply);
        JPanel panel = new JPanel();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        // Add button to JPanel
        panel.add(button);
        frame.getContentPane().add(panel);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        //JOptionPane.showMessageDialog(frame, reply);
    }
}
