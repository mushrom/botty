import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

public class BotMain {
	public BotIrc irc;
	public SecurityManager manager;
	private final String Server = "irc.freenode.net";

	public BotMain( ){
		try {
			irc = new BotIrc( Server );
		} catch ( IOException e ){
			System.err.println( "Could not connect." );
			System.exit( -1 );
		}

		manager = new SecurityManager( );
		BotLoop( );
	}

	public void IrcAction( IrcMessage msg ){
		String[] args = msg.message.split( "\\s+" );
		String replyTo;
		BotUser user = manager.getUser( msg.nick );
		Calendar d = Calendar.getInstance( );

		if ( msg.channel.charAt( 0 ) == '#' )
			replyTo = msg.channel;
		else
			replyTo = msg.nick;

		if ( user == null && args[0].equals( "login" )){
			irc.IrcSendMsg( new IrcMessage( msg.nick, replyTo, msg.nick + ": Logging you in." ));
			user = new BotUser( msg.nick );
			manager.addUser( user );
			irc.IrcSendMsg( new IrcMessage( msg.nick, replyTo, msg.nick + ": Your token is \"" + user.token + "\"." ));

		} else if ( args[0].equals( "ping" )){
			irc.IrcSendMsg( new IrcMessage( msg.nick, replyTo, msg.nick + ": Pong!" ));

		} else if ( args[0].equals( "date" )){
			irc.IrcSendMsg( new IrcMessage( msg.nick, replyTo, msg.nick + ": The date is " + 
				( d.get( Calendar.MONTH ) + 1 ) + "/" + d.get( Calendar.DATE ) + ". The time is " +
				  d.get( Calendar.HOUR_OF_DAY ) + ":" + d.get( Calendar.MINUTE ) + "." ));

		} else if ( args[0].equals( "help" )){
			irc.IrcSendMsg( new IrcMessage( msg.nick, replyTo, msg.nick + ": commands: login, ping, date, admininfo, refresh, set, logout." ));

		// Everything below needs a login.
		} else if ( user == null ){
			irc.IrcSendMsg( new IrcMessage( msg.nick, replyTo, msg.nick + ": You are not logged in." ));

		} else if ( args[0].equals( "admininfo" )){
			if ( manager.isAdmin( user ))
				irc.IrcSendMsg( new IrcMessage( msg.nick, replyTo, msg.nick + ": You are the admin." ));
			else
				irc.IrcSendMsg( new IrcMessage( msg.nick, replyTo, msg.nick + ": You are not the admin." ));

		} else if ( args.length < 2 ){
			irc.IrcSendMsg( new IrcMessage( msg.nick, replyTo, msg.nick + ": Usage: [command] [user token] [options]" ));

		// Everything below requires an argument
		} else if ( user.checkToken( args[1] )){
			if ( args[0].equals( "refresh" )){
				irc.IrcSendMsg( new IrcMessage( msg.nick, replyTo, msg.nick + ": Refreshing token." ));
				user.refreshToken( );
				irc.IrcSendMsg( new IrcMessage( msg.nick, replyTo, msg.nick + ": Your token is \"" + user.token + "\"" ));

			} else if ( args[0].equals( "set" ) && args.length > 2 ){
				irc.IrcSendMsg( new IrcMessage( msg.nick, replyTo, msg.nick + ": Setting token." ));
				user.setToken( args[2] );
				irc.IrcSendMsg( new IrcMessage( msg.nick, replyTo, msg.nick + ": Your token is \"" + user.token + "\"" ));

			} else if ( args[0].equals( "logout" )){
				irc.IrcSendMsg( new IrcMessage( msg.nick, replyTo, msg.nick + ": Logging you out." ));
				manager.delUser( user );
			}
		} else {
			irc.IrcSendMsg( new IrcMessage( msg.nick, replyTo, msg.nick + ": Invalid token." ));
		}
	}

	public void BotLoop( ){
		IrcMessage msg;

		irc.IrcUser( "botty" );
		irc.IrcNick( "botty" );
		irc.IrcWaitForPing( );
		irc.IrcJoin( "#bots" );

		while( true ){
			msg = irc.IrcRecvMsg( );
			System.out.printf( "[%s] %s: %s\n", msg.channel, msg.nick, msg.message );
			IrcAction( msg );
		}
	};

	public static void main( String[] args ){
		BotMain prog = new BotMain( );
	}
}
