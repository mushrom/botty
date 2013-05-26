import java.util.ArrayList;

// This class manages the admin account and other vital bot resources
public class SecurityManager {

	// This string should never be changed, so we're making it final.
	private static final String ADMIN = "Admin";
	// A list of authorized users
	private ArrayList<BotUser> users;

	// Constructor for SecurityManager
	public SecurityManager( ){
		users = new ArrayList<BotUser>();
	}

	// Add a new authorized user
	public void addUser( BotUser user ){
		users.add( user );
	}

	// remove an authorized user
	public void delUser( BotUser user ){
		users.remove( user );
	}

	// get an authorized user
	public BotUser getUser( String name ){
		int i;
		for ( i = 0; i < users.size( ); i++ ){
			if ( users.get(i).name.equals( name ))
				return users.get(i);
		}

		return null;
	}

	// Return the name of the current admin.
	public String getAdmin( ){
		return ADMIN;
	}
	
	// Check the ADMIN variable to see if the user is the admin
	public boolean isAdmin( BotUser user ){
		return ADMIN.equals( user.name );
	}
}
