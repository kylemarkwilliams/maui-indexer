package maui.vocab.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openrdf.elmo.ElmoModule;
import org.openrdf.elmo.sesame.SesameManager;
import org.openrdf.elmo.sesame.SesameManagerFactory;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.nativerdf.NativeStore;

public class VocabularyStoreImpl implements VocabularyStore {

	private String RDFFile;
	private SesameManager manager;
	private String format;
	private String name;
	private String sesameStore;

	private NativeStore store;
	private Repository repository;
	private SesameManagerFactory factory;

	public VocabularyStoreImpl(String propertiesFile) {
		Properties properties = new Properties();
		try {
			FileInputStream fis = new FileInputStream(propertiesFile);
			properties.load(fis);
			this.RDFFile = properties.getProperty("rdf");
			this.sesameStore = properties.getProperty("sesameStore");
			String activateSesameStore = properties.getProperty("activateSesameStore");
			Boolean activeSesame = new Boolean(activateSesameStore);
			this.format = properties.getProperty("format");
			this.name = properties.getProperty("name");
			if (this.sesameStore != null && activeSesame==true)
				this.initSesameRepository();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initSesameRepository() {
		this.store = new NativeStore(new File(this.sesameStore));
		this.repository = new SailRepository(store);
		try {
			repository.initialize();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ElmoModule module = new ElmoModule();
		this.factory = new SesameManagerFactory(module, repository);
		this.manager = factory.createElmoManager();
	}

	@Override
	public String getRDFFile() {
		// TODO Auto-generated method stub
		return this.RDFFile;
	}

	@Override
	public SesameManager getSesameManager() {
		// TODO Auto-generated method stub
		return this.manager;
	}

	@Override
	public String getVocabularyFormat() {
		// TODO Auto-generated method stub
		return this.format;
	}

	@Override
	public String getVocabularyName() {
		// TODO Auto-generated method stub
		return this.name;
	}

	@Override
	public String getStore() {
		return this.sesameStore;
	}

	public void close() {
		if (this.manager != null) {
			this.manager.close();
			System.out.println("Manager closed OK");
			this.factory.close();
			System.out.println("Factory  closed OK");
			try {
				this.repository.shutDown();
				System.out.println("Repository closed OK");
			} catch (RepositoryException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
