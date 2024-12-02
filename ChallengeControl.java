package PoolGame;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ChallengeControl implements ActionListener
{
	 // Private data fields for the container and chat client.
	  private JPanel container;
	  private ChatClient client;
	  
	  // Constructor for the create account controller.
	  public ChallengeControl(JPanel container, ChatClient client)
	  {
	    this.container = container;
	    this.client = client;
	  }
	  
	  // Handle button clicks.
	  public void actionPerformed(ActionEvent ae)
	  {
	    // Get the name of the button clicked.
	    String command = ae.getActionCommand();

	    // The Cancel button takes the user back to the initial panel.
	    if (command == "Accept")
	    {

	    }

	    // The Submit button creates a new account.
	    else if (command == "Decline")
	    {
 
	    }
	  }

	  
	  // Method that displays a message in the error label.
	  public void displayError(String error)
	  {
	    CreateAccountPanel createAccountPanel = (CreateAccountPanel)container.getComponent(2);
	    createAccountPanel.setError(error);
	  }
}
