package PoolGame;

import javax.swing.*;
import java.awt.*;

public class challengeScreenPanel extends JPanel
{
	  private JPanel container;
	  private ChatClient client;
	  private JButton accept, decline;
	  
    public challengeScreenPanel(ChallengeControl cc) 
    {
        this.setBackground(Color.BLUE); // Set background color to blue
        

        // Add a label to indicate this screen.
        JLabel label = new JLabel("You Have Been Challenged", JLabel.CENTER);
        label.setForeground(Color.WHITE); // Optional: Adjust text color for readability
        label.setFont(new Font("Arial", Font.BOLD, 16));

        // Set layout and add components.
        this.setLayout(new BorderLayout());
        this.add(label, BorderLayout.CENTER);
        
        
        
        
    }
    
    public void createChallenge()
    {
    	challengeScreenPanel challengeScreen = (challengeScreenPanel)container.getComponent(5);
    	ClientGUI clientGUI = (ClientGUI)SwingUtilities.getWindowAncestor(challengeScreen);
        CardLayout layout = (CardLayout) container.getLayout();
        layout.show(container, "5"); 
    }

}