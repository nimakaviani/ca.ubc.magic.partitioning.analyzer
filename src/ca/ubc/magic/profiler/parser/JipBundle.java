package ca.ubc.magic.profiler.parser;

import java.util.ArrayList;
import java.util.List;

public class JipBundle {

	private String _loaderID;
	private String _bundleSymbolicName;
	private String _exportPackage;
	private String _includeResource;
	private String _privatePackage;
	private String _importPackage;
	private String _conditionalPackage;
	private String _bundleName;
	private String _bundleManifestVersion;
	private String _bundleVersion;
	private String _serviceComponent;
	private String _bundleDescription;
	private String _bundleLicense;
	private String _bundleVendor;
	
	private List<JipBundleService> _serviceList;

	public static final String BUNDLE_SYMBOLIC_NAME = "Bundle-SymbolicName";
	public static final String BUNDLE_NAME = "Bundle-Name";
	public static final String BUNDLE_VERSION = "Bundle-Version";
	public static final String IMPORT_PACKAGE ="Import-Package";
	public static final String EXPORT_PACKAGE ="Export-Package";
	public static final String BUNDLE_DESCRIPTION ="Bundle-Description";
	public static final String BUNDLE_LICENSE ="Bundle-License";
	public static final String BUNDLE_VENDOR ="Bundle-Vendor";
	public static final String INCLUDE_RESOURCE ="Include-Resource";
	public static final String PRIVATE_PACKAGE ="Private-Package";
	public static final String CONDITIONAL_PACKAGE ="Conditional-Package";
	public static final String BUNDLE_MANIFEST_VERSION ="Bundle-ManifestVersion";
	public static final String SERVICE_COMPONENT ="Service-Component";
	public static final String SERVICE = "service";

	public JipBundle(String loaderID){
		_loaderID = loaderID;
		_serviceList = new ArrayList<JipBundleService>();
	}

	public String getLoaderID(){
		return _loaderID;
	}
	public void setLoaderID(String loaderID){
		this._loaderID = loaderID;
	}

	public String getBundleSymbolicName(){
		return _bundleSymbolicName;
	}
	public void setBundleSymbolicName(String bundleSymbolicName){
		this._bundleSymbolicName = bundleSymbolicName;
	}


	public String getExportPackage(){
		return _exportPackage;
	}
	public void setExportPackage(String exportPackage){
		this._exportPackage = exportPackage;
	}

	public String getIncludeResource(){
		return _includeResource;
	}
	public void setIncludeResource(String includeResource){
		this._includeResource = includeResource;
	}

	public String getPrivatePackage(){
		return _privatePackage;
	}
	public void setPrivatePackage(String privatePackage){
		this._privatePackage = privatePackage;
	}

	public  String getImportPackage(){
		return _importPackage;
	}
	public void setImportPackage(String importPackage){
		this._importPackage = importPackage;
	}

	public String  getConditionalPackage(){
		return _conditionalPackage;
	}
	public void setConditionalPackage(String conditionalPackage){
		this._conditionalPackage = conditionalPackage;
	}

	public String getBundleName(){
		return _bundleName;
	}
	public void setBundleName(String bundleName){
		this._bundleName = bundleName;
	}

	public String getBundleManifestVersion(){
		return _bundleManifestVersion;
	}
	public void setBundleManifestVersion(String manifestVersion){
		this._bundleManifestVersion = manifestVersion;
	}

	public String getBundleVersion(){
		return _bundleVersion;
	}
	public void setBundleVersion(String version){
		this._bundleVersion = version;
	}

	public String getServiceComponent(){
		return _serviceComponent;
	}
	public void setServiceComponent(String serviceComponent){
		this._serviceComponent = serviceComponent;
	}

	public String getBundleDescription(){
		return _bundleDescription;
	}
	public void setBundleDescription(String bundleDescription){
		this._bundleDescription = bundleDescription;
	}

	public String getBundleLicense(){
		return _bundleLicense;
	}
	public void setBundleLicense(String bundleLicense){
		this._bundleLicense = bundleLicense;
	}

	public String getBundleVendor(){
		return _bundleVendor;
	}
	public void setBundleVendor(String bundleVendor){
		this._bundleVendor = bundleVendor;
	}
	
	public void setService( String classStr, String id,
							String in, String out, String total){
		_serviceList.add(new JipBundleService(classStr, id, in, out, total));
	}
	
	public void endService(){
		
	}
	
	public List<JipBundleService> getServiceList(){
		return _serviceList;
	}

}
