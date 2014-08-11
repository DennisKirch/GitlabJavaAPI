package org.gitlab.api.models;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;
import org.gitlab.api.http.GitlabHTTPRequestor;

public class GitlabProject {
	
	GitlabHTTPRequestor requestor;

    public static final String URL = "/projects";
    private String currentTailUrl = "";

    private Integer _id;
    private String _name;

    @JsonProperty("name_with_namespace")
    private String _nameWithNamespace;

    private String _description;

    @JsonProperty("default_branch")
    private String _defaultBranch;

    private GitlabUser _owner;
    private boolean _public;
    private String _path;

    @JsonProperty("visibility_level")
    private Integer _visibilityLevel;

    @JsonProperty("path_with_namespace")
    private String _pathWithNamespace;

    @JsonProperty("issues_enabled")
    private boolean _issuesEnabled;

    @JsonProperty("merge_requests_enabled")
    private boolean _mergeRequestsEnabled;

    @JsonProperty("snippets_enabled")
    private boolean _snippetsEnabled;

    @JsonProperty("wall_enabled")
    private boolean _wallEnabled;

    @JsonProperty("wiki_enabled")
    private boolean _wikiEnabled;

    @JsonProperty("created_at")
    private Date _createdAt;

    @JsonProperty("ssh_url_to_repo")
    private String _sshUrl;

    @JsonProperty("http_url_to_repo")
    private String _httpUrl;

    private GitlabNamespace _namespace;
    
    public void setRequestor(GitlabHTTPRequestor requestor){
    	this.requestor = requestor;
    }
    
    public void setCurrentTailUrl(String tailUrl){
    	this.currentTailUrl = tailUrl;
    }
    
    private GitlabHTTPRequestor retrieve(){
    	return this.requestor;
    }

    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getNameWithNamespace() {
        return _nameWithNamespace;
    }

    public void setNameWithNamespace(String nameWithNamespace) {
        this._nameWithNamespace = nameWithNamespace;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public String getDefaultBranch() {
        return _defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        _defaultBranch = defaultBranch;
    }

    public Integer getVisibilityLevel() {
        return _visibilityLevel;
    }

    public void setVisibilityLevel(Integer visibilityLevel) {
        this._visibilityLevel = visibilityLevel;
    }

    public GitlabUser getOwner() {
        return _owner;
    }

    public void setOwner(GitlabUser owner) {
        _owner = owner;
    }

    public String getPath() {
        return _path;
    }

    public void setPath(String path) {
        _path = path;
    }

    public String getPathWithNamespace() {
        return _pathWithNamespace;
    }

    public void setPathWithNamespace(String pathWithNamespace) {
        _pathWithNamespace = pathWithNamespace;
    }

    public boolean isIssuesEnabled() {
        return _issuesEnabled;
    }

    public void setIssuesEnabled(boolean issuesEnabled) {
        _issuesEnabled = issuesEnabled;
    }

    public boolean isMergeRequestsEnabled() {
        return _mergeRequestsEnabled;
    }

    public void setMergeRequestsEnabled(boolean mergeRequestsEnabled) {
        _mergeRequestsEnabled = mergeRequestsEnabled;
    }

    public boolean isSnippetsEnabled() {
        return _snippetsEnabled;
    }

    public void setSnippetsEnabled(boolean snippetsEnabled) {
        this._snippetsEnabled = snippetsEnabled;
    }

    public boolean isWallEnabled() {
        return _wallEnabled;
    }

    public void setWallEnabled(boolean wallEnabled) {
        _wallEnabled = wallEnabled;
    }

    public boolean isWikiEnabled() {
        return _wikiEnabled;
    }

    public void setWikiEnabled(boolean wikiEnabled) {
        _wikiEnabled = wikiEnabled;
    }

    public Date getCreatedAt() {
        return _createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        _createdAt = createdAt;
    }

    public String getSshUrl() {
        return _sshUrl;
    }

    public void setSshUrl(String sshUrl) {
        _sshUrl = sshUrl;
    }

    public String getHttpUrl() {
        return _httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        _httpUrl = httpUrl;
    }

    public GitlabNamespace getNamespace() {
        return _namespace;
    }

    public void setNamespace(GitlabNamespace namespace) {
        _namespace = namespace;
    }

    public boolean isPublic() {
        return _public;
    }

    public void setPublic(boolean aPublic) {
        _public = aPublic;
    }
    
    public List<GitlabTreeElement> getRepositoryTree(String path, String branchRef) throws IOException {
    	StringBuilder sb = new StringBuilder(this.currentTailUrl);
    	sb.append(GitlabTreeElement.URL);
    	sb.append(branchRef.length()>0 ? "?ref=" + branchRef : "");
    	if(path.length() > 0){
    		sb.append((sb.indexOf("?") > -1 ? "&path=" : "?path=") + path);
    	}
    	
    	String tailUrl = sb.toString();
    	GitlabTreeElement[] tree = retrieve().to(tailUrl, GitlabTreeElement[].class);
    	return GitlabTreeElement.createTreeElementListWithUrlAndRequestor(tailUrl, tree, this.requestor);
    }
    
    public List<GitlabTreeElement> getRepositoryTree(String branchRef) throws IOException {
        return getRepositoryTree("", branchRef);
    }
    
    public List<GitlabTreeElement> getRepositoryTree() throws IOException {
        return getRepositoryTree("", "");
    }
}
