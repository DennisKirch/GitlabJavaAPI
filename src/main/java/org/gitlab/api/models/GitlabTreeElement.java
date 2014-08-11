package org.gitlab.api.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.gitlab.api.http.GitlabHTTPRequestor;

public class GitlabTreeElement {
	
	GitlabHTTPRequestor requestor;

	public static final String URL = "/repository/tree";
	private String currentTailUrl = "";

    @JsonProperty("id")
    private String _id;
    
    @JsonProperty("name")
    private String _name;
    
    @JsonProperty("type")
    private String _type;
    
    @JsonProperty("mode")
    private String _mode;
    
    public void setRequestor(GitlabHTTPRequestor requestor){
    	this.requestor = requestor;
    }

    public void setCurrentTailUrl(String tailUrl){
    	this.currentTailUrl = tailUrl;
    }

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        _id = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }
    
    public String getType() {
        return _type;
    }

    public void setType(String type) {
    	_type = type;
    }
    
    public String getMode() {
        return _mode;
    }

    public void setMode(String mode) {
    	_mode = mode;
    }
    
    public boolean isFile() {
    	return this._type.equals("blob");
    }
    
    public boolean isDirectory() {
    	return this._type.equals("tree");
    }
    
    public GitlabFile getGitlabFile() throws IOException{
    	String fileAccessTailUrl = this.currentTailUrl.replace(URL, GitlabFile.URL);
    	fileAccessTailUrl = fileAccessTailUrl.replace("path=", "file_path=");
    	fileAccessTailUrl += this._name + "/";
    	GitlabFile file = retrieve().to(fileAccessTailUrl, GitlabFile.class);
    	
    	return file;
    }
    
    public List<GitlabTreeElement> listFiles() throws IOException{
    	StringBuilder sb = new StringBuilder(this.currentTailUrl);
    	sb.append(this._name);
    	sb.append("/");
    	String tailUrl = sb.toString();
    	GitlabTreeElement[] tree = retrieve().to(tailUrl, GitlabTreeElement[].class);
    	return createTreeElementListWithUrlAndRequestor(tailUrl, tree, this.requestor);
    }
    
    protected static List<GitlabTreeElement> createTreeElementListWithUrlAndRequestor(String tailUrl, GitlabTreeElement[] tree, GitlabHTTPRequestor requestor){
    	List<GitlabTreeElement> resultTree = new ArrayList<GitlabTreeElement>();
    	for(int i = 0; i < tree.length; i++){
    		tree[i].setCurrentTailUrl(tailUrl);
    		tree[i].setRequestor(requestor);
    		resultTree.add(tree[i]);
    	}
        return resultTree;
    }
    
    public GitlabHTTPRequestor retrieve() {
        return this.requestor;
    }
}
