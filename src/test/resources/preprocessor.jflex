/*
 * The pre-processor performs initial parsing steps used to initialize
 * metadata, replace syntax that should not be saved to the database,
 * and prepare the document for the full parsing by the processor.
 */
package org.jamwiki.parser.jflex;

import org.jamwiki.Environment;
import org.jamwiki.utils.WikiLogger;
import org.springframework.util.StringUtils;

%%

%public
%class JAMWikiPreProcessor
%extends AbstractLexer
%type String
%unicode
%ignorecase

/* code included in the constructor */
%init{
    allowHTML = Environment.getBooleanValue(Environment.PROP_PARSER_ALLOW_HTML);
    yybegin(NORMAL);
    states.add(new Integer(yystate()));
%init}

/* code called after parsing is completed */
%eofval{
    StringBuffer output = new StringBuffer();
    if (StringUtils.hasText(this.templateString)) {
        // FIXME - this leaves unparsed text
        output.append(this.templateString);
        this.templateString = "";
    }
    return (output.length() == 0) ? null : output.toString();
%eofval}

/* code copied verbatim into the generated .java file */
%{
    protected static WikiLogger logger = WikiLogger.getLogger(JAMWikiPreProcessor.class.getName());
    protected boolean allowHTML = false;
    protected int templateCharCount = 0;
    protected String templateString = "";
%}

/* character expressions */
newline            = ((\r\n) | (\n))
whitespace         = {newline} | [ \t\f]

/* nowiki */
nowiki             = (<[ ]*nowiki[ ]*>) ~(<[ ]*\/[ ]*nowiki[ ]*>)

/* pre */
htmlprestart       = (<[ ]*pre[ ]*>)
htmlpreend         = (<[ ]*\/[ ]*pre[ ]*>)
wikiprestart       = (" ")+ ([^ \t\r\n])
wikipreend         = ([^ ]) | ({newline})

/* comments */
htmlcomment        = "<!--" ~"-->"

/* wiki links */
wikilink           = "[[" [^\]\n\r]+ "]]"
protocol           = "http://" | "https://" | "mailto:" | "mailto://" | "ftp://" | "file://"
htmllinkwiki       = "[" ({protocol}) ([^\]\n\r]+) "]"
/* FIXME - hard-coding of image namespace */
imagelinkcaption   = "[[" ([ ]*) "Image:" ([^\n\r\]\[]* ({wikilink} | {htmllinkwiki}) [^\n\r\]\[]*)+ "]]"

/* templates */
templatestart      = "{{"
templatestartchar  = "{"
templateendchar    = "}"
templateparam      = "{{{" [^\{\}\r\n]+ "}}}"
includeonly        = (<[ ]*includeonly[ ]*[\/]?[ ]*>) ~(<[ ]*\/[ ]*includeonly[ ]*>)
noinclude          = (<[ ]*noinclude[ ]*[\/]?[ ]*>) ~(<[ ]*\/[ ]*noinclude[ ]*>)

/* signatures */
wikisignature      = ([~]{3,5})

%state NORMAL, PRE, WIKIPRE, TEMPLATE

%%

/* ----- nowiki ----- */

<WIKIPRE, PRE, NORMAL>{nowiki} {
    logger.finer("nowiki: " + yytext() + " (" + yystate() + ")");
    WikiNowikiTag parserTag = new WikiNowikiTag();
    return this.parseToken(yytext(), parserTag);
}

/* ----- pre ----- */

<NORMAL>{htmlprestart} {
    logger.finer("htmlprestart: " + yytext() + " (" + yystate() + ")");
    if (allowHTML) {
        beginState(PRE);
    }
    HtmlPreTag parserTag = new HtmlPreTag();
    return this.parseToken(yytext(), parserTag);
}

<PRE>{htmlpreend} {
    logger.finer("htmlpreend: " + yytext() + " (" + yystate() + ")");
    // state only changes to pre if allowHTML is true, so no need to check here
    endState();
    HtmlPreTag parserTag = new HtmlPreTag();
    return this.parseToken(yytext(), parserTag);
}

<NORMAL, WIKIPRE>^{wikiprestart} {
    logger.finer("wikiprestart: " + yytext() + " (" + yystate() + ")");
    // rollback the one non-pre character so it can be processed
    yypushback(yytext().length() - 1);
    if (yystate() != WIKIPRE) {
        beginState(WIKIPRE);
    }
    return yytext();
}

<WIKIPRE>^{wikipreend} {
    logger.finer("wikipreend: " + yytext() + " (" + yystate() + ")");
    endState();
    // rollback the one non-pre character so it can be processed
    yypushback(1);
    return yytext();
}

/* ----- templates ----- */

<NORMAL, TEMPLATE>{templatestart} {
    logger.finer("templatestart: " + yytext() + " (" + yystate() + ")");
    String raw = yytext();
    if (!Environment.getBooleanValue(Environment.PROP_PARSER_ALLOW_TEMPLATES)) {
        return yytext();
    }
    this.templateString += raw;
    this.templateCharCount += 2;
    if (yystate() != TEMPLATE) {
        beginState(TEMPLATE);
    }
    return "";
}

<TEMPLATE>{templateendchar} {
    logger.finer("templateendchar: " + yytext() + " (" + yystate() + ")");
    String raw = yytext();
    this.templateString += raw;
    this.templateCharCount -= raw.length();
    if (this.templateCharCount == 0) {
        endState();
        String value = new String(this.templateString);
        this.templateString = "";
        TemplateTag parserTag = new TemplateTag();
        return this.parseToken(value, parserTag);
    }
    return "";
}

<TEMPLATE>{templatestartchar} {
    logger.finer("templatestartchar: " + yytext() + " (" + yystate() + ")");
    String raw = yytext();
    this.templateString += raw;
    this.templateCharCount += raw.length();
    if (this.templateString.equals("{{{")) {
        // param, not a template
        this.templateCharCount = 0;
        endState();
        String value = new String(this.templateString);
        this.templateString = "";
        return value;
    }
    return "";
}

<NORMAL>{templateparam} {
    logger.finer("templateparam: " + yytext() + " (" + yystate() + ")");
    String raw = yytext();
    return raw;
}

<TEMPLATE>{whitespace} {
    // no need to log this
    String raw = yytext();
    this.templateString += raw;
    return "";
}

<TEMPLATE>. {
    // no need to log this
    String raw = yytext();
    this.templateString += raw;
    return "";
}

<NORMAL, TEMPLATE>{includeonly} {
    logger.finer("includeonly: " + yytext() + " (" + yystate() + ")");
    IncludeOnlyTag parserTag = new IncludeOnlyTag();
    return this.parseToken(yytext(), parserTag);
}

<NORMAL, TEMPLATE>{noinclude} {
    logger.finer("noinclude: " + yytext() + " (" + yystate() + ")");
    NoIncludeTag parserTag = new NoIncludeTag();
    return this.parseToken(yytext(), parserTag);
}

/* ----- wiki links ----- */

<NORMAL>{imagelinkcaption} {
    logger.finer("imagelinkcaption: " + yytext() + " (" + yystate() + ")");
    WikiLinkTag parserTag = new WikiLinkTag();
    return this.parseToken(yytext(), parserTag);
}

<NORMAL>{wikilink} {
    logger.finer("wikilink: " + yytext() + " (" + yystate() + ")");
    WikiLinkTag parserTag = new WikiLinkTag();
    return this.parseToken(yytext(), parserTag);
}

/* ----- signatures ----- */

<NORMAL>{wikisignature} {
    logger.finer("wikisignature: " + yytext() + " (" + yystate() + ")");
    WikiSignatureTag parserTag = new WikiSignatureTag();
    return this.parseToken(yytext(), parserTag);
}

/* ----- comments ----- */

<NORMAL>{htmlcomment} {
    logger.finer("htmlcomment: " + yytext() + " (" + yystate() + ")");
    HtmlCommentTag parserTag = new HtmlCommentTag();
    return this.parseToken(yytext(), parserTag);
}

/* ----- other ----- */

<WIKIPRE, PRE, NORMAL>{whitespace} {
    // no need to log this
    CharacterTag parserTag = new CharacterTag();
    return this.parseToken(yytext(), parserTag);
}

<WIKIPRE, PRE, NORMAL>. {
    // no need to log this
    CharacterTag parserTag = new CharacterTag();
    return this.parseToken(yytext(), parserTag);
}
