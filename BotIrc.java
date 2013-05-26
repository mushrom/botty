import java.io.*;
import java.net.Socket;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class BotIrc {

	private String IrcServer;
	private int IrcPort = 6667;
	private Socket IrcSocket;
	private PrintWriter out;
	private BufferedReader in;

	public BotIrc( String Server ) throws IOException {
		IrcServer = Server;
		try {
			IrcSocket = new Socket( Server, IrcPort );
		} catch ( IOException e ){
			System.err.println( "Could not connect to host" );
			System.exit( -1 );
		}

		out = new PrintWriter( IrcSocket.getOutputStream( ), true );
		in  = new BufferedReader( new InputStreamReader( IrcSocket.getInputStream( )));
	}

	public void IrcSendRaw( String message ){
		out.println( message );
	}

	public String IrcRecvRaw( ) {
		String line;
		try {
			line = in.readLine( );
			System.out.println( "[bot] " + line );
			return line;
		} catch ( IOException e ){}

		return null;
	}

	public void IrcSendMsg( IrcMessage msg ){
		out.printf( "PRIVMSG %s :%s\r\n", msg.channel, msg.message );
	}

	public void IrcUser( String user ){
		out.printf( "USER %s %s %s :%s\r\n", user, user, user, user );
	}

	public void IrcNick( String nick ){
		out.printf( "NICK %s\r\n", nick );
	}

	public void IrcJoin( String channel ){
		out.printf( "JOIN %s\r\n", channel );
	}

	public void IrcPart( String channel, String message ){
		out.printf( "PART %s :%s\r\n", channel, message );
	}

	public void IrcWaitForPing( ){
		boolean gotPing = false;
		Pattern pingPat = Pattern.compile( "^PING" );
		Matcher pingMat;
		String line;
		
		while ( !gotPing ){
			line = IrcRecvRaw( );
			pingMat = pingPat.matcher( line );

			if ( pingMat.find( )){ 
				IrcSendRaw( line.replace( "PING", "PONG" ));
				System.out.println( "Sent pong" );
				gotPing = true;
			}
		}
	}

	public IrcMessage IrcRecvMsg( ){
		boolean gotMsg = false;
		Pattern pat = Pattern.compile( "PRIVMSG" );
		Pattern pingPat = Pattern.compile( "^PING" );
		IrcMessage ret;

		Matcher mat;
		Matcher pingMat;

		String line;
		
		while ( !gotMsg ){
			line = IrcRecvRaw( );
			mat = pat.matcher( line );
			pingMat = pingPat.matcher( line );

			if ( mat.find( )){
				return new IrcMessage( line );
			} else if ( pingMat.find( )){ 
				IrcSendRaw( line.replace( "PING", "PONG" ));
				System.out.println( "Sent pong" );
			}
			
		}

		return null;
	}

}
