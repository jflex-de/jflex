# Makefile for jflex documentation (ps + pdf)

all: psgz pdf  

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
	gzip manual.ps

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
