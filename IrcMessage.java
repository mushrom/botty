// Class for irc messages
public class IrcMessage {
	public String nick;
	public String channel;
	public String message;
	// Message split by spaces
	public String[] args;

	// Construct a new IrcMessage from given details
	public IrcMessage( String n_nick, String n_channel, String n_message ){
		nick = n_nick;
		channel = n_channel;
		message = n_message;
		args = n_message.split( "\\s+" );
	}

	// Parse a line of raw IRC. Assumes it is PRIVMSG.
	public IrcMessage( String raw_irc ){
		args = raw_irc.split( "\\s+" );
		nick = raw_irc.substring( 1, raw_irc.indexOf( '!' ));
		channel = args[2];
		message = raw_irc.substring( 1 );
		message = message.substring( message.indexOf( ':' ) + 1 );
		args = message.split( "\\s+" );
	}
}
