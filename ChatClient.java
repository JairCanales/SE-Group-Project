package PoolGame;

import java.awt.CardLayout;
import java.awt.Component;
import java.io.Serializable;

import javax.swing.DefaultListModel;

import ocsf.client.AbstractClient;
import ocsf.server.ConnectionToClient;

public class ChatClient extends AbstractClient implements Serializable
{
  // Private data fields for storing the GUI controllers.
  private LoginControl loginControl;
  private CreateAccountControl createAccountControl;
  private DefaultListModel<String> playerListModel;
  private ContactsPanel contactControl;
  private ChallengeControl challengeControl;
  private User user;

  // Setters for the GUI controllers.
  public void setLoginControl(LoginControl loginControl)
  {
    this.loginControl = loginControl;
  }
  public void setChallengeControl(ChallengeControl challengeControl)
  {
    this.challengeControl = challengeControl;
  }
  public void setCotactControl(ContactsPanel contactControl)
  {
    this.contactControl = contactControl;
  }
  public void setCreateAccountControl(CreateAccountControl createAccountControl)
  {
    this.createAccountControl = createAccountControl;
  }
  
  public void setUser(User user)
  {
	  this.user = user;
  }
  
  public void setPlayerListModel(DefaultListModel<String> playerListModel)
  {
	  this.playerListModel = playerListModel;
  }
  
  public void addToPlayerListModel(User user)
  {
	  playerListModel.addElement(user.getUsername());
  }
  
  public User getUser()
  {
	  return user;
  }

  // Constructor for initializing the client with default settings.
  public ChatClient()
  {
    super("localhost", 8300);
  }
  
  // Method that handles messages from the server.
  public void handleMessageFromServer(Object arg0)
  {
    // If we received a String, figure out what this event is.
    if (arg0 instanceof String)
    {
      // Get the text of the message.
      String message = (String)arg0;
      
      // If we successfully logged in, tell the login controller.
      if (message.equals("LoginSuccessful"))
      {
        loginControl.loginSuccess();
      }
      
      // If we successfully created an account, tell the create account controller.
      else if (message.equals("CreateAccountSuccessful"))
      {
        createAccountControl.createAccountSuccess();
      }
    }
    
    else if (arg0 instanceof Challenge)
    {
    	Challenge ch = (Challenge)arg0;
    	
        if (ch.getUsername().equals(getUser().getUsername()))
        {
        	contactControl.createChallenge();
        }
    }
    

    else if (arg0 instanceof LoginData)
    {
    	LoginData data = (LoginData)arg0;
    	setUser(new User(data.getUsername(), data.getPassword()));
    	String newUser = user.getUsername();
    	playerListModel.addElement(newUser);
    }
    
    // If we received an Error, figure out where to display it.
    else if (arg0 instanceof Error)
    {
      // Get the Error object.
      Error error = (Error)arg0;
      
      // Display login errors using the login controller.
      if (error.getType().equals("Login"))
      {
        loginControl.displayError(error.getMessage());
      }
      
      // Display account creation errors using the create account controller.
      else if (error.getType().equals("CreateAccount"))
      {
        createAccountControl.displayError(error.getMessage());
      }
    }
  }  
  public void logout()
  {
      System.out.println("User session reset.");
  }
}
