# Makefile for jflex documentation (ps + pdf + html)

VERSION=1.7.0-SNAPSHOT
RELEASE_DATE=16 March 2015
UNICODE_VER=7.0

all: pdf html

build: realclean all

pdf: manual.pdf
html: manual.html

manual.md: head.md intro.md installing.md example.md lex-specs.md \
           encodings.md performance.md porting-and-parsers.md end.md
	cat $^ > $@

manual.html: manual.mdx
	pandoc -S --css manual.css --filter pandoc-citeproc -f markdown $< -o $@

#dependency manual.tex would fire manual.md -> .tex conversion
xmanual.pdf: xmanual.tex intro.tex installing.tex example.tex lex-specs.tex \
             encodings.tex performance.tex porting-and-parsers.tex end.tex
	pdflatex xmanual
	bibtex xmanual
	pdflatex xmanual
	pdflatex xmanual

manual.pdf: xmanual.pdf
	cp $< $@

# variable substitutions:
%.mdx: %.md Makefile
	perl -pi -e 's/\$$VERSION/$(VERSION)/g' < $< | \
	perl -pi -e 's/\$$RELEASE_DATE/$(RELEASE_DATE)/g' | \
	perl -pi -e 's/\$$UNICODE_VER/$(UNICODE_VER)/g' > $@

%.tex: %.mdx
	pandoc -f markdown -t latex --biblatex $< -o $@

clean:
	rm -f *~ *.aux *.log log *.toc *.out *.bbl *.blg *.bcf

realclean: clean 
	rm -f *.mdx
	rm -f manual.html manual.pdf manual.md xmanual.pdf
	rm -f intro.tex installing.tex example.tex lex-specs.tex
	rm -f encodings.tex performance.tex porting-and-parsers.tex end.tex
