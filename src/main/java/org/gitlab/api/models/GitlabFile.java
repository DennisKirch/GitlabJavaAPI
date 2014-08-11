package org.gitlab.api.models;

import groovyjarjarantlr.RecognitionException;
import groovyjarjarantlr.TokenStreamException;
import groovyjarjarantlr.collections.AST;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.groovy.antlr.AntlrASTProcessor;
import org.codehaus.groovy.antlr.SourceBuffer;
import org.codehaus.groovy.antlr.UnicodeEscapingReader;
import org.codehaus.groovy.antlr.UnicodeLexerSharedInputState;
import org.codehaus.groovy.antlr.parser.GroovyLexer;
import org.codehaus.groovy.antlr.parser.GroovyRecognizer;
import org.codehaus.groovy.antlr.treewalker.SourceCodeTraversal;
import org.codehaus.jackson.annotate.JsonProperty;
import org.gitlab.api.helper.JavaDocCommentVisitor;

import javax.xml.bind.DatatypeConverter;

public class GitlabFile {
	public static final String URL = "/repository/files";

	@JsonProperty("file_name")
	private String _file_name;

	@JsonProperty("file_path")
	private String _file_path;

	@JsonProperty("size")
	private Integer _size;

	@JsonProperty("encoding")
	private String _encoding;

	@JsonProperty("content")
	private String _content;

	@JsonProperty("ref")
	private String _ref;

	@JsonProperty("blob_id")
	private String _blob_id;

	@JsonProperty("commit_id")
	private String _commit_id;

	public String getFileName() {
		return _file_name;
	}

	public void setFileName(String file_name) {
		this._file_name = file_name;
	}

	public String getFilePath() {
		return _file_path;
	}

	public void setFilePath(String file_path) {
		this._file_path = file_path;
	}

	public Integer getSize() {
		return _size;
	}

	public void setSize(Integer size) {
		this._size = size;
	}

	public String getEncoding() {
		return _encoding;
	}

	public void setEncoding(String encoding) {
		this._encoding = encoding;
	}

	public String getContent() {
		return _content;
	}
	
	public List<String> getJavaDocComments() throws IOException, RecognitionException, TokenStreamException {
		byte[] decodedContentInByte = DatatypeConverter
				.parseBase64Binary(this._content);

		ByteArrayInputStream in = new ByteArrayInputStream(decodedContentInByte);
		InputStreamReader inStreamReader = new InputStreamReader(in);
//		FileReader reader = new FileReader("new.txt");
		SourceBuffer sourceBuffer = new SourceBuffer();
        UnicodeEscapingReader unicodeReader = new UnicodeEscapingReader(inStreamReader, sourceBuffer);
        UnicodeLexerSharedInputState inputState = new UnicodeLexerSharedInputState(unicodeReader);
        GroovyLexer lexer = new GroovyLexer(inputState);
        unicodeReader.setLexer(lexer);
        GroovyRecognizer parser = GroovyRecognizer.make(lexer);
		parser.compilationUnit();
        
        AST ast = parser.getAST();
        
        List<String> javaDocComments = new ArrayList<String>();
        JavaDocCommentVisitor visitor = new JavaDocCommentVisitor(sourceBuffer, javaDocComments);
        AntlrASTProcessor traverser = new SourceCodeTraversal(visitor);
        
        traverser.process(ast);
        
        return javaDocComments;
	}

	public List<String> getAuthors() throws IOException {
		final String AUTHOR_ANNOTATION = "@author";
		
		List<String> comments;
		try {
			comments = getJavaDocComments();
		} catch (Exception e) {
			comments = new ArrayList<String>();
		}
		List<String> authors = new ArrayList<String>();
		for(String comment : comments) {
			int authorIndex = -1;
			do{
				authorIndex = comment.indexOf(AUTHOR_ANNOTATION, authorIndex);
				if(authorIndex > -1){
					int i = comment.indexOf("\n", authorIndex);
					int j = comment.indexOf("\r", authorIndex);
					int newLineIndex = i;
					if(i > -1 && j > -1){
						newLineIndex = Math.min(i, j);
					}
					else if (j > 0){
						newLineIndex = j;
					}
					if(newLineIndex > -1){
						String author = comment.substring(authorIndex + AUTHOR_ANNOTATION.length(), comment.indexOf("\n", newLineIndex));
						authors.add(author.trim());
						authorIndex = newLineIndex;
					}
				}
			} while(authorIndex > -1);
		}
		if(authors.isEmpty()){
			authors.add("UNKNOWN");
		}

		return authors;
	}

	public byte[] getContetDecoded() {
		return DatatypeConverter.parseBase64Binary(this._content);
	}

	public void setContent(String content) {
		this._content = content;
	}

	public String getRef() {
		return _ref;
	}

	public void setRef(String ref) {
		this._ref = ref;
	}

	public String getBlobId() {
		return _blob_id;
	}

	public void setBlobId(String blob_id) {
		this._blob_id = blob_id;
	}

	public String getCommitId() {
		return _commit_id;
	}

	public void setCommitId(String commit_id) {
		this._commit_id = commit_id;
	}

	public static String getUrl() {
		return URL;
	}
}
