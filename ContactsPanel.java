package PoolGame;

import java.awt.*;
import java.io.IOException;

import javax.swing.*;

public class ContactsPanel extends JPanel
{
  //private CardLayout cardLayout;
  private ChatClient client;
  private JPanel container;
  private DefaultListModel<String> playerListModel;
  private JList<String> contactList;
  private JButton challengeButton;
  
  // Constructor for the contacts panel.
  public ContactsPanel(ChatClient client, JPanel container, DefaultListModel<String> playerListModel)
  {
    //this.cardLayout = cardLayout;
	this.client = client;
    this.container = container;
    this.playerListModel = playerListModel;
	this.setBackground(Color.BLUE);
	//this.playerListModel = playerListModel;
	
    // Create a list of example contacts.
    this.playerListModel = playerListModel;
    contactList = new JList<>(playerListModel);
    contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    contactList.setLayoutOrientation(JList.VERTICAL);
    contactList.setFont(contactList.getFont().deriveFont(Font.PLAIN, 16));
    contactList.setBackground(Color.WHITE);
    contactList.setForeground(Color.BLACK);
    
    contactList.addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) 
        {
        	challengeButton.setEnabled(contactList.getSelectedValue() != null);
        }
    });

    // Use BorderLayout to lay out the components in this panel.
    this.setLayout(new BorderLayout());

    // Create the contacts list in the center.
    JScrollPane contactScrollPane = new JScrollPane(contactList);
    contactScrollPane.setPreferredSize(new Dimension(300, 200));
    this.add(contactScrollPane, BorderLayout.CENTER);
    
    // Create the contacts label in the north.
    JLabel label = new JLabel("Players Looking for a Game", JLabel.CENTER);
    label.setForeground(Color.WHITE);
    this.add(label, BorderLayout.NORTH);    

    // Create the buttons in the south.
    JPanel buttonsPanel = new JPanel(new GridLayout(2, 1));
    buttonsPanel.setBackground(Color.BLUE);
    
    JPanel contactButtons = new JPanel();
    contactButtons.setBackground(Color.BLUE);
    
    challengeButton = new JButton("Challenge Player");
    challengeButton.setEnabled(false);
    challengeButton.addActionListener(e -> {
        String selectedPlayer = contactList.getSelectedValue();
        if (selectedPlayer != null) 
        {
            System.out.println("Challenging: " + selectedPlayer);
            
            try 
            {
				client.sendToServer(new Challenge(selectedPlayer, client, false));
			} catch (IOException e1) 
            {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
/*
            // Switch to the pool game panel
            CardLayout layout = (CardLayout) container.getLayout();
            layout.show(container, "PoolGame"); // Use the identifier "PoolGame"

            // Maximize the window for the pool game
            Component[] components = container.getComponents();
            for (Component component : components) 
            {
                if (component instanceof PoolGamePanel) 
                {
                    ((PoolGamePanel) component).maximizeWindow();
                    break;
                }
            }
            */
        }
    });
    contactButtons.add(challengeButton);
    
    JPanel logoutButtonPanel = new JPanel();
    logoutButtonPanel.setBackground(Color.BLUE);
    JButton logoutButton = new JButton("Log Out");
    logoutButton.addActionListener(e -> {
        if (container != null) {
            System.out.println("Logging out...");
            CardLayout layout = (CardLayout) container.getLayout();
            layout.show(container, "2"); // Switch to login screen

            // Call the logout method in ChatClient
            if (client != null) 
            {
                client.logout(); 
            }
        } else {
            System.err.println("Container is not initialized. Cannot log out.");
        }
    });
    logoutButtonPanel.add(logoutButton);
    
    buttonsPanel.add(contactButtons);
    buttonsPanel.add(logoutButtonPanel);
    this.add(buttonsPanel, BorderLayout.SOUTH);
       
   
}
  
  public void createChallenge()
  {
  	challengeScreenPanel challengeScreen = (challengeScreenPanel)container.getComponent(5);
  	ClientGUI clientGUI = (ClientGUI)SwingUtilities.getWindowAncestor(challengeScreen);
    CardLayout layout = (CardLayout)container.getLayout();
    layout.show(container, "5"); 
  }
  

  
  public void updatePlayerList(DefaultListModel<String> newPlayerList) 
  {
	  playerListModel.clear();
      for (int i = 0; i < newPlayerList.size(); i++) {
          playerListModel.addElement(newPlayerList.getElementAt(i));
      }
  }
}
  
