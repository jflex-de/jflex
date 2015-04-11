# Makefile for jflex documentation (ps + pdf + html)

all: pdf html

build: realclean all

pdf: manual.pdf

html: manual.html

# while the conversion to markdown is in progress:
xmanual.md: manual.tex manual.bib jay.tex installing.tex
	pandoc manual.tex -o xmanual.md

xmanual.html: xmanual.md
	pandoc -s xmanual.md -o xmanual.html

manual.pdf: logo.png manual.tex manual.bib jay.tex installing.tex
	make clean
	pdflatex manual
	bibtex manual
	pdflatex manual
	pdflatex manual

manual.html: manual.tex manual.bib installing.tex jay.tex
	make clean
	pdflatex manual
	bibtex manual
	pdflatex manual
	pdflatex manual
	latex2html -split 0 -no_navigation -html_version 3.2,math -t "JFlex User's Manual" -local_icons -address "`date`, <a href=\"http://www.doclsf.de\">Gerwin Klein</a>" -info "" manual 
	cp manual/manual.html .
	rm -rf manual 

%.tex: %.md
	pandoc -f markdown -t latex --biblatex $< -o $@

clean:
	rm -f *~ *.aux *.log log *.toc *.out *.bbl *.blg

realclean: clean 
	rm -f *.pdf *.html
