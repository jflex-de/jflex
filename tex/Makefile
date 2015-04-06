# Makefile for jflex documentation (ps + pdf + html)

all: pdf html

build: realclean all

pdf: manual.pdf

html: manual.html

manual.pdf: logo.png manual.tex jay.tex installing.tex
	make clean
	pdflatex manual
	pdflatex manual
	pdflatex manual

manual.html: manual.tex installing.tex jay.tex
	make clean
	pdflatex manual
	pdflatex manual
	pdflatex manual
	latex2html -split 0 -no_navigation -html_version 3.2,math -t "JFlex User's Manual" -local_icons -address "`date`, <a href=\"http://www.doclsf.de\">Gerwin Klein</a>" -info "" manual 
	cp manual/manual.html .
	rm -rf manual 

installing.tex: installing.md
	pandoc installing.md -o installing.tex

clean:
	rm -f *~ *.aux *.log log *.toc *.out

realclean: clean 
	rm -f *.pdf *.html
