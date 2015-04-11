# Makefile for jflex documentation (ps + pdf + html)

all: pdf

build: realclean all

pdf: ymanual.pdf

xmanual.html: xmanual.md
	pandoc -s xmanual.md -o xmanual.html

ymanual.pdf: ymanual.tex intro.tex installing.tex example.tex xmanual.tex
	pdflatex ymanual
	bibtex ymanual
	pdflatex ymanual
	pdflatex ymanual	

manual.pdf: logo.png manual.tex manual.bib jay.tex installing.tex
	make clean
	pdflatex manual
	bibtex manual
	pdflatex manual
	pdflatex manual

#	latex2html -split 0 -no_navigation -html_version 3.2,math -t "JFlex User's Manual" -local_icons -address "`date`, <a href=\"http://www.doclsf.de\">Gerwin Klein</a>" -info "" manual 

%.tex: %.md
	pandoc -f markdown -t latex --biblatex $< -o $@

clean:
	rm -f *~ *.aux *.log log *.toc *.out *.bbl *.blg *.bcf

realclean: clean 
	rm -f *.pdf *.html
