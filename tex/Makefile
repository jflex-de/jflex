# Makefile for jflex documentation (ps + pdf + html)

all: psgz pdf html  

tex:
	make texclean
	latex manual
	latex manual
	latex manual

manual.ps: tex
	dvips manual

clean: texclean 
	rm -f *.ps
	rm -f *.png

realclean: clean javaclean
	rm -f *.pdf *.ps.gz 

texclean:
	rm -f *.dvi *~ *.aux *.log log *.toc *.out

psgz: manual.ps
	gzip -f manual.ps

pdf: logo.png 
	make texclean
	pdflatex manual
	pdflatex manual
	pdflatex manual

logo.png: logo.eps
	epstopng logo.eps

logo.pdf: logo.eps
	epstopdf logo.eps

build: realclean all

html: clean
	make tex
	latex2html -split 0 -no_navigation -html_version 3.2,math -t "JFlex User's Manual" -local_icons -address "`date`, <a href=\"http://www.doclsf.de\">Gerwin Klein</a>" -info "" manual 
	cp manual/manual.html .
	rm -rf manual 
