# Makefile for jflex documentation (ps + pdf + html)

all: psgz pdf html  

build: realclean all

tex: manual.dvi

psgz: manual.ps.gz

pdf: manual.pdf

html: manual.html


manual.dvi: manual.tex
	make texclean
	latex manual
	latex manual
	latex manual

manual.ps.gz: manual.dvi
	dvips manual
	gzip -f manual.ps

manual.pdf: logo.png manual.tex
	make texclean
	pdflatex manual
	pdflatex manual
	pdflatex manual

manual.html: manual.tex
	make texclean
	latex manual
	latex manual
	latex manual
	latex2html -split 0 -no_navigation -html_version 3.2,math -t "JFlex User's Manual" -local_icons -address "`date`, <a href=\"http://www.doclsf.de\">Gerwin Klein</a>" -info "" manual 
	cp manual/manual.html .
	rm -rf manual 

logo.png: logo.eps
	epstopng logo.eps

logo.pdf: logo.eps
	epstopdf logo.eps

clean: texclean 
	rm -f *.ps
	rm -f *.png

realclean: clean 
	rm -f *.pdf *.ps.gz *.html

texclean:
	rm -f *.dvi *~ *.aux *.log log *.toc *.out
