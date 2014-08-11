
package org.gitlab.api.helper;

import org.codehaus.groovy.antlr.GroovySourceAST;
import org.codehaus.groovy.antlr.LineColumn;
import org.codehaus.groovy.antlr.SourceBuffer;
import org.codehaus.groovy.antlr.parser.GroovyTokenTypes;
import org.codehaus.groovy.antlr.treewalker.VisitorAdapter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaDocCommentVisitor extends VisitorAdapter implements GroovyTokenTypes {

	private static final Pattern PREV_JAVADOC_COMMENT_PATTERN = Pattern.compile("(?s)/\\*\\*(.*?)\\*/");
    private SourceBuffer sourceBuffer;
    private LineColumn lastLineCol;
    private List<String> javaDocComments;

    public JavaDocCommentVisitor(SourceBuffer sourceBuffer, List<String> javaDocComments) {
    	this.sourceBuffer = sourceBuffer;
    	this.javaDocComments = javaDocComments;
    	lastLineCol = new LineColumn(1, 1);
    }
 
    @Override
    public void visitClassDef(GroovySourceAST t, int visit) {
    	logJavaDocComment(t, visit);
    }
    
    @Override
    public void visitImport(GroovySourceAST t, int visit) {
    	logJavaDocComment(t, visit);
    }
    
    @Override
    public void visitPackageDef(GroovySourceAST t, int visit) {
    	logJavaDocComment(t, visit);
    }
    
    private void logJavaDocComment(GroovySourceAST t, int visit){
    	if (visit == OPENING_VISIT) {
        	if(javaDocComments != null){
        		String comment = getJavaDocCommentsBeforeNode(t);
        		if(comment.length() > 0){
        			javaDocComments.add(comment);
        		}
        	}
        }
    }

    private String getJavaDocCommentsBeforeNode(GroovySourceAST t) {
        String result = "";
        LineColumn thisLineCol = new LineColumn(t.getLine(), t.getColumn());
        String text = sourceBuffer.getSnippet(lastLineCol, thisLineCol);
        if (text != null) {
            Matcher m = PREV_JAVADOC_COMMENT_PATTERN.matcher(text);
            if (m.find()) {
                result = m.group(1);
            }
        }
        lastLineCol = thisLineCol;
        
        return result;
    }
    

   
}
