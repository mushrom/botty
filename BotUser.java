import java.util.Random;
import java.security.SecureRandom;
import java.math.BigInteger;
import java.lang.reflect.Field;

public class BotUser {
	// User's name
	public String name;
	// Security token
	public String token;
	// Every user gets their own random generator
	private Random rand = new SecureRandom( );
	// map directly to value fields, to reduce overhead
	private Field valueField;

	// Good symbols for generating tokens
	public char[] goodSyms = { 	'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 
					'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 
					'1', '2', '3', '4', '6', '7', '8', '9', '0' };
	
	// BotUser constructor
	public BotUser( String n_name ){
		name = n_name;
		generateToken( );

		try {
			valueField = String.class.getDeclaredField( "value" );
			valueField.setAccessible( true );
		} catch ( Exception e ){
		}
	}

	// Set new token
	public void setToken( String n_token ){
		token = n_token;
	}

	// Generate a new, secure token for the user. This is somewhat slow though.
	public void generateToken( ){
		setToken( new BigInteger( 64, rand ).toString( Character.MAX_RADIX ));
	}

	// Get character values from valueField
	public char[] getValues( String input ){
		char[] ret = {};
		try {
			ret = (char [])valueField.get( input ); 
		} catch ( IllegalArgumentException e ){
		} catch ( IllegalAccessException e ){}

		return ret;
	}

	// Refresh a user's key. This is a bit faster, and should be used after generateToken
	// makes a secure token.
	public void refreshToken( ){
		int i, p;
		char[] chars = getValues( token );
		
		for ( i = 0; i < 3; i++ ){
			p = rand.nextInt( chars.length - 1 );
			char r = goodSyms[rand.nextInt( goodSyms.length )];
			chars[p] = r;
		}
	}

	// Check whether a token is valid
	public boolean checkToken( String check ){
		// Cache small strings to increase speed
		if ( token.length( ) < 12 ){
			token = token.intern( );
			check = check.intern( );
		}

		return token.equals( check );
	}

}
