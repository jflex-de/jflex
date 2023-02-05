# Copyright 2022, Gerwin Klein, Régis Décamps, Steven Rowe
# SPDX-License-Identifier: BSD-3-Clause

# Makefile for jflex documentation (ps + pdf + html)

VERSION=1.9.0
RELEASE_DATE=5 February 2023
UNICODE_VER=12.1

all: pdf html

build: realclean all

pdf: manual.pdf
html: manual.html

md/manual.md: md/head.md md/toc.md md/intro.md md/installing.md \
							md/maven-plugin.md md/ant-task.md md/example.md md/lex-specs.md \
							md/generated-class.md md/encodings.md md/performance.md \
							md/porting-and-parsers.md md/end.md
	cat $^ > $@

manual.html: manual.mdx
	pandoc --standalone --css manual.css $(shell ./citeproc-arg) -f markdown $< -o $@

#dependency manual.tex would fire manual.md -> .tex conversion
xmanual.pdf: xmanual.tex intro.tex installing.tex example.tex lex-specs.tex \
						 generated-class.tex encodings.tex performance.tex \
						 porting-and-parsers.tex end.tex ant-task.tex maven-plugin.tex
	pdflatex xmanual
	bibtex xmanual
	pdflatex xmanual
	pdflatex xmanual

manual.pdf: xmanual.pdf
	cp $< $@

# variable substitutions:
%.mdx: md/%.md Makefile
	sed -e 's/\$$VERSION/$(VERSION)/g' \
	    -e 's/\$${project.version}/$(VERSION)/g' \
	    -e 's/\$$RELEASE_DATE/$(RELEASE_DATE)/g' \
	    -e 's/\$$UNICODE_VER/$(UNICODE_VER)/g' $< > $@

%.tex: %.mdx
	pandoc --no-highlight -f markdown -t latex --biblatex $< -o $@

clean:
	rm -f *~ *.aux *.log log *.toc *.out *.bbl *.blg *.bcf

realclean: clean
	rm -f *.mdx
	rm -f manual.html manual.pdf md/manual.md xmanual.pdf
	rm -f intro.tex installing.tex example.tex lex-specs.tex ant-task.tex
	rm -f encodings.tex performance.tex porting-and-parsers.tex end.tex
	rm -f maven-plugin.tex generated-class.tex
