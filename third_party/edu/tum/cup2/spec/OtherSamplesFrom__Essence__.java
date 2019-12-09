package edu.tum.cup2.spec;

/*
 * These are samples from the Essence Parser Generator.
 * Also likely to be "true" LR(1)
 * see: http://www.informatik.uni-freiburg.de/proglang/software/essence/
 */

/*

; Copyright (c) 2000-2008 by Michael Sperber and Peter Thiemann. See file COPYING.

; Balanced parentheses

(define-grammar g00 g00-symbol
  (l)
  S
  ((S ((l) $1))))

(define-grammar g08 g08-symbol
  (l r)
  S
  ((S ((S T) $1)
      ((T) $1))
   (T ((l S r) $1)
      ((l r) $1))))

; Constant arithmetic expressions

(define-grammar g10 g10-symbol
  (+ - * / l r n)
  E
  ((E ((T) $1)
      ((T + E) (+ $1 $3))
      ((T - E) (- $1 $3)))
   (T ((P) $1)
      ((P * T) (* $1 $3))
      ((P / T) (/ $1 $3)))
   (P ((n) $1)
      ((l E r) $2))))

(define-grammar g10-error g10-error-symbol
  (+ - * / l r n)
  E
  ((E ((T) $1)
      (($error) 0)
      ((T + E) (+ $1 $3))
      ((T - E) (- $1 $3)))
   (T ((P) $1)
      ((P * T) (* $1 $3))
      ((P / T) (/ $1 $3)))
   (P ((n) $1)
      ((l E r) $2)
      ((l $error r) 0))))

(define-grammar g13 g13-symbol
  (comma blah dot)
  S
  ((SLK (() $1)
	((NESLK) $1))
   (NESLK ((N) $1)
	  ((NESLK K N) $1))
   (SLD (() $1)
	((NESLD) $1))
   (NESLD ((N) $1)
	  ((NESLD P N) $1))
   (S ((SLK) $1)
      ((SLD) $1))
   (K ((comma) $1))
   (P ((dot) $1))
   (N ((blah) $1))))
   
;; javascript example expanded

(define-grammar g14 g14-symbol
  (const comma colon lcurly rcurly lbracket rbracket)
  S
  ((S ((E) $1))
   (E ((const) $1)
      ((lcurly OL rcurly) $1)
      ((lbracket AL rbracket) $1))
   (C ((comma) $1))
   (A ((const colon E) $1))
   (OL (() $1)
       ((ON) $1))
   (ON ((A) $1)
       ((ON C A) $1))
   (AL (() $1)
       ((AN) $1))
   (AN ((E) $1)
       ((AN C E) $1))))

*/



public class OtherSamplesFrom__Essence__
{

}
