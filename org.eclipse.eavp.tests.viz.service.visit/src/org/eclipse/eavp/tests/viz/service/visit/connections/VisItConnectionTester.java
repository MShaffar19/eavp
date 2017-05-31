/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.eavp.tests.viz.service.visit.connections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.eavp.viz.service.connections.VizConnection;
import org.eclipse.eavp.viz.service.visit.connections.VisItConnection;
import org.eclipse.eavp.viz.service.visit.connections.VisItVizConnectionPreferences;
import org.eclipse.remote.core.IRemoteConnection;
import org.eclipse.remote.core.IRemoteConnectionChangeListener;
import org.eclipse.remote.core.IRemoteConnectionType;
import org.eclipse.remote.core.IRemoteConnectionType.Service;
import org.eclipse.remote.core.IRemoteConnectionWorkingCopy;
import org.eclipse.remote.core.IRemoteServicesManager;
import org.eclipse.remote.core.RemoteConnectionChangeEvent;
import org.eclipse.remote.core.exception.RemoteConnectionException;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceObjects;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * Tests {@link VisItConnection}'s implementation of {@link VizConnection}.
 * 
 * @author Jordan Deyton
 *
 */
public class VisItConnectionTester {

	/**
	 * The connection that is tested in each test.
	 */
	private VisItConnection connection;

	/**
	 * Initializes the class variables used in each test.
	 */
	@Before
	public void beforeEachTest() {
		connection = new VisItConnection();
	}

	/**
	 * Checks the default connection properties for VisIt connections.
	 */
	@Test
	public void checkDefaultProperties() {

		// Check the default values for the basic connection properties.
		assertEquals("Connection1", connection.getName());
		assertNull(connection.getDescription());
		assertEquals("localhost", connection.getHost());
		assertEquals(9600, connection.getPort());
		assertEquals("", connection.getPath());

		// Check that they can be set.
		String name = "newname";
		String host = "newhost";
		int port = 9601;
		String path = "/some/path";

		assertTrue(connection.setName(name));
		assertEquals(name, connection.getName());
		assertTrue(connection.setHost(host));
		assertEquals(host, connection.getHost());
		assertTrue(connection.setPort(port));
		assertEquals(port, connection.getPort());
		assertTrue(connection.setPath(path));
		assertEquals(path, connection.getPath());

		// The description cannot be set.
		assertFalse(connection.setDescription("VisIt"));
		assertNull(connection.getDescription());

		return;
	}

	/**
	 * Checks that the property names for the default connection properties have
	 * been changed to new, VisIt-specific property names.
	 */
	@Test
	public void checkDefaultPropertyNames() {

		// Check that the default property names are no longer valid.
		assertNull(connection.getProperty("Name"));
		assertNull(connection.getProperty("Description"));
		assertNull(connection.getProperty("Host"));
		assertNull(connection.getProperty("Port"));
		assertNull(connection.getProperty("Path"));

		// Check the new values for the property names.
		assertEquals("Connection1", connection.getProperty("connId"));
		assertEquals("localhost", connection.getProperty("url"));
		assertEquals("9600", connection.getProperty("port"));
		assertEquals("", connection.getProperty("visDir"));

		// Check that they can be set.
		String name = "newname";
		String host = "newhost";
		String port = "9601";
		String path = "/some/path";

		assertTrue(connection.setProperty("connId", name));
		assertEquals(name, connection.getProperty("connId"));
		assertTrue(connection.setProperty("url", host));
		assertEquals(host, connection.getProperty("url"));
		assertTrue(connection.setProperty("port", port));
		assertEquals(port, connection.getProperty("port"));
		assertTrue(connection.setProperty("visDir", path));
		assertEquals(path, connection.getProperty("visDir"));

		// This should also affect the getters for these default properties.
		assertEquals(name, connection.getName());
		assertEquals(host, connection.getHost());
		assertEquals(Integer.parseInt(port), connection.getPort());
		assertEquals(path, connection.getPath());

		// Trying to set the same values should fail.
		assertFalse(connection.setProperty("connId", name));
		assertFalse(connection.setProperty("url", host));
		assertFalse(connection.setProperty("port", port));
		assertFalse(connection.setProperty("visDir", path));

		// We should not be able to set the port to an invalid port.
		port = "-1";
		assertFalse(connection.setProperty("localGatewayPort", port));
		port = "66000";
		assertFalse(connection.setProperty("localGatewayPort", port));

		return;
	}

	/**
	 * Checks the "username" and "password" properties (these are for the VisIt
	 * session).
	 */
	@Test
	public void checkUsernameProperties() {

		assertNull(connection.getProperty("username"));
		assertEquals("notused", connection.getProperty("password"));

		String username = "user";
		String password = "blah";

		// We should be able to set the username, but not the password.
		assertTrue(connection.setProperty("username", username));
		assertEquals(username, connection.getProperty("username"));
		assertFalse(connection.setProperty("password", password));
		assertEquals("notused", connection.getProperty("password"));

		// Trying to set the same values should fail.
		assertFalse(connection.setProperty("username", username));
		assertFalse(connection.setProperty("password", password));

		return;
	}

	/**
	 * Checks the "gateway" and "localGatewayPort" properties (these are for a
	 * proxy host and port for the connection).
	 */
	@Test
	public void checkGatewayProperties() {

		assertNull(connection.getProperty("gateway"));
		assertEquals("22", connection.getProperty("localGatewayPort"));

		String gateway = "eclipse";
		String gatewayPort = "9700";

		// We should be able to set both.
		assertTrue(connection.setProperty("gateway", gateway));
		assertEquals(gateway, connection.getProperty("gateway"));
		assertTrue(connection.setProperty("localGatewayPort", gatewayPort));
		assertEquals(gatewayPort, connection.getProperty("localGatewayPort"));

		// Trying to set the same values should fail.
		assertFalse(connection.setProperty("gateway", gateway));
		assertFalse(connection.setProperty("localGatewayPort", gatewayPort));

		// We should not be able to set the gateway port to invalid ports.
		gatewayPort = "-1";
		assertFalse(connection.setProperty("localGatewayPort", gatewayPort));
		gatewayPort = "bob";
		assertFalse(connection.setProperty("localGatewayPort", gatewayPort));

		return;
	}

	/**
	 * Checks the "windowWidth", "windowHeight", and "windowId" properties.
	 */
	@Test
	public void checkWindowProperties() {

		assertEquals("1340", connection.getProperty("windowWidth"));
		assertEquals("1020", connection.getProperty("windowHeight"));
		assertEquals("1", connection.getProperty("windowId"));

		String windowWidth = "800";
		String windowHeight = "600";
		String windowId = "2";

		// We should be able to all of them.
		assertTrue(connection.setProperty("windowWidth", windowWidth));
		assertEquals(windowWidth, connection.getProperty("windowWidth"));
		assertTrue(connection.setProperty("windowHeight", windowHeight));
		assertEquals(windowHeight, connection.getProperty("windowHeight"));
		assertTrue(connection.setProperty("windowId", windowId));
		assertEquals(windowId, connection.getProperty("windowId"));

		// Trying to set the same values should fail.
		assertFalse(connection.setProperty("windowWidth", windowWidth));
		assertFalse(connection.setProperty("windowHeight", windowHeight));
		assertFalse(connection.setProperty("windowId", windowId));

		// Check input validation for the window dimensions.
		windowWidth = "0";
		assertFalse(connection.setProperty("windowWidth", windowWidth));
		windowWidth = "alice";
		assertFalse(connection.setProperty("windowWidth", windowWidth));
		windowHeight = "-1";
		assertFalse(connection.setProperty("windowHeight", windowHeight));
		windowHeight = "carol";
		assertFalse(connection.setProperty("windowHeight", windowHeight));

		// Check validation for the window ID (must be -1, or 1 through 16).
		windowId = "-1";
		assertTrue(connection.setProperty("windowId", windowId));
		assertEquals(windowId, connection.getProperty("windowId"));
		for (int i = 1; i <= 16; i++) {
			windowId = Integer.toString(i);
			assertTrue(connection.setProperty("windowId", windowId));
			assertEquals(windowId, connection.getProperty("windowId"));
		}
		assertFalse(connection.setProperty("windowId", "-2"));
		assertFalse(connection.setProperty("windowId", "0"));
		assertFalse(connection.setProperty("windowId", "17"));
		assertFalse(connection.setProperty("windowId", "charlie"));

		return;

	}

	/**
	 * Check that a VisItVizConnectionPreferences will correctly configure the
	 * connection.
	 */
	@Test
	public void checkSetPreferences() {
		BundleContext context = new BundleContext() {

			@Override
			public String getProperty(String key) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Bundle getBundle() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Bundle installBundle(String location, InputStream input)
					throws BundleException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Bundle installBundle(String location)
					throws BundleException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Bundle getBundle(long id) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Bundle[] getBundles() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void addServiceListener(ServiceListener listener,
					String filter) throws InvalidSyntaxException {
				// TODO Auto-generated method stub

			}

			@Override
			public void addServiceListener(ServiceListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public void removeServiceListener(ServiceListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public void addBundleListener(BundleListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public void removeBundleListener(BundleListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public void addFrameworkListener(FrameworkListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public void removeFrameworkListener(FrameworkListener listener) {
				// TODO Auto-generated method stub

			}

			@Override
			public ServiceRegistration<?> registerService(String[] clazzes,
					Object service, Dictionary<String, ?> properties) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceRegistration<?> registerService(String clazz,
					Object service, Dictionary<String, ?> properties) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <S> ServiceRegistration<S> registerService(Class<S> clazz,
					S service, Dictionary<String, ?> properties) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <S> ServiceRegistration<S> registerService(Class<S> clazz,
					ServiceFactory<S> factory,
					Dictionary<String, ?> properties) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceReference<?>[] getServiceReferences(String clazz,
					String filter) throws InvalidSyntaxException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceReference<?>[] getAllServiceReferences(String clazz,
					String filter) throws InvalidSyntaxException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ServiceReference<?> getServiceReference(String clazz) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <S> ServiceReference<S> getServiceReference(Class<S> clazz) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <S> Collection<ServiceReference<S>> getServiceReferences(
					Class<S> clazz, String filter)
					throws InvalidSyntaxException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public <S> S getService(ServiceReference<S> reference) {
				return (S) new FakeRemoteServicesManager();
			}

			@Override
			public boolean ungetService(ServiceReference<?> reference) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public <S> ServiceObjects<S> getServiceObjects(
					ServiceReference<S> reference) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public File getDataFile(String filename) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Filter createFilter(String filter)
					throws InvalidSyntaxException {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Bundle getBundle(String location) {
				// TODO Auto-generated method stub
				return null;
			}

		};

		// Set up a connection
		TestVisItVizConnectionPreferences prefs = new TestVisItVizConnectionPreferences(
				context);
		prefs.setName("Test Connection");
		prefs.setPort(1234);
		prefs.setConnectionName("Remote Connection");
		prefs.setExecutablePath("path/to/visit.exe");
		prefs.setProxy("Test Proxy");
		prefs.setProxyPort(9876);

		// Set the preferences to the connection
		connection.setPreferences(prefs);

		// Check that the connection was properly configured
		assertEquals(prefs.getHostName(), connection.getHost());
		assertEquals(prefs.getName(), connection.getName());
		assertEquals(prefs.getPort(), connection.getPort());
		assertEquals(prefs.getUsername(), connection.getProperty("username"));
		assertEquals(prefs.getExecutablePath(), connection.getPath());
		assertEquals(prefs.getProxy(), connection.getProperty("gateway"));
		assertEquals(Integer.toString(prefs.getProxyPort()),
				connection.getProperty("localGatewayPort"));

	}

	/**
	 * A minimal IRemoteServicesManager implementation which will return a SSH
	 * service that has a single connection whose working copy will each
	 * property name as its value when getProperty() is invoked. This class is
	 * for use with testing the RCPVizConnectionPreferences by injecting this
	 * class as a dependency. The returned connection will have the name "Remote
	 * Connection"
	 * 
	 * @author Robert Smith
	 *
	 */
	public class FakeRemoteServicesManager implements IRemoteServicesManager {

		@Override
		public void addRemoteConnectionChangeListener(
				IRemoteConnectionChangeListener arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void fireRemoteConnectionChangeEvent(
				RemoteConnectionChangeEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public List<IRemoteConnectionType> getAllConnectionTypes() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<IRemoteConnection> getAllRemoteConnections() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IRemoteConnectionType getConnectionType(String arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IRemoteConnectionType getConnectionType(URI arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<IRemoteConnectionType> getConnectionTypesByService(
				Class<? extends Service>... arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<IRemoteConnectionType> getConnectionTypesSupporting(
				Class<? extends org.eclipse.remote.core.IRemoteConnection.Service>... arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IRemoteConnectionType getLocalConnectionType() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<IRemoteConnectionType> getRemoteConnectionTypes() {
			ArrayList<IRemoteConnectionType> types = new ArrayList<IRemoteConnectionType>();
			IRemoteConnectionType ssh = new IRemoteConnectionType() {

				@Override
				public boolean canAdd() {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean canEdit() {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean canRemove() {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public IRemoteConnection getConnection(String arg0) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public IRemoteConnection getConnection(URI arg0) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public List<String> getConnectionServices() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public List<IRemoteConnection> getConnections() {
					ArrayList<IRemoteConnection> connections = new ArrayList<IRemoteConnection>();
					connections.add(new IRemoteConnection() {

						@Override
						public void addConnectionChangeListener(
								IRemoteConnectionChangeListener arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public void close() {
							// TODO Auto-generated method stub

						}

						@Override
						public void fireConnectionChangeEvent(int arg0) {
							// TODO Auto-generated method stub

						}

						@Override
						public String getAttribute(String arg0) {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public IRemoteConnectionType getConnectionType() {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public String getName() {
							return "Remote Connection";
						}

						@Override
						public String getProperty(String arg0) {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public String getSecureAttribute(String arg0) {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public <T extends Service> T getService(Class<T> arg0) {
							// TODO Auto-generated method stub
							return null;
						}

						@Override
						public IRemoteConnectionWorkingCopy getWorkingCopy() {
							return new IRemoteConnectionWorkingCopy() {

								@Override
								public void addConnectionChangeListener(
										IRemoteConnectionChangeListener arg0) {
									// TODO Auto-generated method stub

								}

								@Override
								public void close() {
									// TODO Auto-generated method stub

								}

								@Override
								public void fireConnectionChangeEvent(
										int arg0) {
									// TODO Auto-generated method stub

								}

								@Override
								public String getAttribute(String arg0) {
									// Simply return every attribute's name.
									return arg0;
								}

								@Override
								public IRemoteConnectionType getConnectionType() {
									// TODO Auto-generated method stub
									return null;
								}

								@Override
								public String getName() {
									// TODO Auto-generated method stub
									return null;
								}

								@Override
								public String getProperty(String arg0) {
									// TODO Auto-generated method stub
									return null;
								}

								@Override
								public String getSecureAttribute(String arg0) {
									// TODO Auto-generated method stub
									return null;
								}

								@Override
								public <T extends Service> T getService(
										Class<T> arg0) {
									// TODO Auto-generated method stub
									return null;
								}

								@Override
								public IRemoteConnectionWorkingCopy getWorkingCopy() {
									// TODO Auto-generated method stub
									return null;
								}

								@Override
								public <T extends Service> boolean hasService(
										Class<T> arg0) {
									// TODO Auto-generated method stub
									return false;
								}

								@Override
								public boolean isOpen() {
									// TODO Auto-generated method stub
									return false;
								}

								@Override
								public void open(IProgressMonitor arg0)
										throws RemoteConnectionException {
									// TODO Auto-generated method stub

								}

								@Override
								public void removeConnectionChangeListener(
										IRemoteConnectionChangeListener arg0) {
									// TODO Auto-generated method stub

								}

								@Override
								public IRemoteConnection getOriginal() {
									// TODO Auto-generated method stub
									return null;
								}

								@Override
								public boolean isDirty() {
									// TODO Auto-generated method stub
									return false;
								}

								@Override
								public IRemoteConnection save()
										throws RemoteConnectionException {
									// TODO Auto-generated method stub
									return null;
								}

								@Override
								public void setAttribute(String arg0,
										String arg1) {
									// TODO Auto-generated method stub

								}

								@Override
								public void setName(String arg0) {
									// TODO Auto-generated method stub

								}

								@Override
								public void setSecureAttribute(String arg0,
										String arg1) {
									// TODO Auto-generated method stub

								}

							};
						}

						@Override
						public <T extends Service> boolean hasService(
								Class<T> arg0) {
							// TODO Auto-generated method stub
							return false;
						}

						@Override
						public boolean isOpen() {
							// TODO Auto-generated method stub
							return false;
						}

						@Override
						public void open(IProgressMonitor arg0)
								throws RemoteConnectionException {
							// TODO Auto-generated method stub

						}

						@Override
						public void removeConnectionChangeListener(
								IRemoteConnectionChangeListener arg0) {
							// TODO Auto-generated method stub

						}

					});
					return connections;
				}

				@Override
				public String getId() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public String getName() {
					return "SSH";
				}

				@Override
				public List<String> getProcessServices() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public IRemoteServicesManager getRemoteServicesManager() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public String getScheme() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public <T extends Service> T getService(Class<T> arg0) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public List<String> getServices() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public <T extends org.eclipse.remote.core.IRemoteConnection.Service> boolean hasConnectionService(
						Class<T> arg0) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public <T extends org.eclipse.remote.core.IRemoteProcess.Service> boolean hasProcessService(
						Class<T> arg0) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public <T extends Service> boolean hasService(Class<T> arg0) {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public IRemoteConnectionWorkingCopy newConnection(String arg0)
						throws RemoteConnectionException {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public void removeConnection(IRemoteConnection arg0)
						throws RemoteConnectionException {
					// TODO Auto-generated method stub

				}

			};
			types.add(ssh);
			return types;
		}

		@Override
		public void removeRemoteConnectionChangeListener(
				IRemoteConnectionChangeListener arg0) {
			// TODO Auto-generated method stub

		}
	}

	/**
	 * An extension of VisItVizConnectionPreferences which allows a custom
	 * BundleContext to be set to the class for testing purposes.
	 * 
	 * @author Robert Smith
	 *
	 */
	private class TestVisItVizConnectionPreferences
			extends VisItVizConnectionPreferences {

		/**
		 * The Nullary Constructor.
		 * 
		 * @param context
		 *            The bundle context from which to draw the
		 *            IRemoteServicesManager.
		 */
		public TestVisItVizConnectionPreferences(BundleContext context) {
			super();
			this.context = context;
		}

		/**
		 * The constructor for the serialized representation.
		 * 
		 * @param data
		 *            The serialized representation of the preferences.
		 * @param context
		 *            The bundle context from which to draw the
		 *            IRemoteServicesManager.
		 */
		public TestVisItVizConnectionPreferences(String data,
				BundleContext context) {
			super(data);
			this.context = context;
		}
	}

}