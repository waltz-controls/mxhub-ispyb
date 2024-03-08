/*******************************************************************************
 * This file is part of ISPyB.
 * 
 * ISPyB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ISPyB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with ISPyB.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors : S. Delageniere, R. Leal, L. Launer, K. Levik, S. Veyrier, P. Brenchereau, M. Bodin, A. De Maria Antolinos
 ******************************************************************************/
package ispyb.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

// ----------------------------------------------------------------------------
/**
 * A simple class for loading java.util.Properties backed by .properties files
 * deployed as classpath resources. See individual methods for details.
 *
 * @author (C) <a href="http://www.javaworld.com/columns/jw-qna-index.shtml">Vlad Roubtsov</a>, 2003
 */
public
abstract class PropertyLoader
{
	// public: ................................................................


	/**
	 * A convenience overload of {@link #loadProperties(String)}
	 * that uses the current thread's context classloader. A better strategy
	 * would be to use techniques shown in
	 * http://www.javaworld.com/javaworld/javaqa/2003-06/01-qa-0606-load.html
	 */
	//TODO check all calls to this method to be compliant with new semantic
	public static Properties loadProperties (final String uri)
	{
		try {
			Properties result= new Properties();

			URI _uri = new URI(uri);
			String protocol = _uri.getScheme();
			URL url = getUrl(uri, protocol);
			//TODO do we need to close this stream?
			result.load(url.openStream());

			return result;
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

	private static URL getUrl(String uri, String protocol) throws MalformedURLException {
		if(protocol.equalsIgnoreCase("classpath"))
		return new URL(null, uri, new URLStreamHandler() {
			@Override
			protected URLConnection openConnection(URL url) throws IOException {
				final URL resourceUrl = Thread.currentThread ().getContextClassLoader ().getResource(url.getPath());
				return resourceUrl.openConnection();
			}
		});
		else
			return new URL(uri);
	}

	// protected: .............................................................

	// package: ...............................................................

	// private: ...............................................................

	public static InputStream GetFile (String name, final String suffix)
		{
		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		if (name == null) throw new IllegalArgumentException ("null input: name");

		if (name.startsWith ("/")) name = name.substring (1);

		if (name.endsWith (suffix)) name = name.substring (0, name.length () - suffix.length ());

		System.out.println("------------> " + name + "  :: " + suffix);
		InputStream in = null;
		try
		{
			if (loader == null) loader = ClassLoader.getSystemClassLoader ();
			name = name.replace ('.', '/');
			if (! name.endsWith (suffix)) name = name.concat (suffix);
			// returns null on lookup failures:
			in = loader.getResourceAsStream (name);
		}
		catch (Exception e)
		{
		in = null;
		}
		if (THROW_ON_LOAD_FAILURE && (in == null))
		{
			throw new IllegalArgumentException ("could not load [" + name + "]" +
				" as " + (LOAD_AS_RESOURCE_BUNDLE
				? "a resource bundle"
				: "a classloader resource"));
		}
		return in;
		}

	private PropertyLoader () {} // this class is not extentible

	private static final boolean THROW_ON_LOAD_FAILURE = true;
	private static final boolean LOAD_AS_RESOURCE_BUNDLE = false;
	private static final String SUFFIX = ".properties";

} // end of class
// ----------------------------------------------------------------------------
