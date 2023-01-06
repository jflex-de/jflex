; -*- Mode: Emacs-Lisp; -*-

;;;  jflex-mode

;;; Copyright 2015, Gerwin Klein <lsf@jflex.de>
;;; SPDX-License-Identifier: BSD-3-Clause

(require 'derived)
(require 'font-lock)

(define-derived-mode jflex-mode java-mode "JFlex"
  "Major mode for editing JFlex files"

  ;; set the indentation
  (setq c-basic-offset 2)

  (c-set-offset 'knr-argdecl-intro 0)
  (c-set-offset 'topmost-intro-cont 0)

  ;; remove auto and hungry anything
  (c-toggle-auto-hungry-state -1)
  (c-toggle-auto-state -1)
  (c-toggle-hungry-state -1)

  (use-local-map jflex-mode-map)

  ;; get rid of that damn electric-brace
  (define-key jflex-mode-map "{"	'self-insert-command)
  (define-key jflex-mode-map "}"	'self-insert-command)

  (define-key jflex-mode-map [tab] 'jflex-indent-command)

  )

(defalias 'jflex-indent-command 'c-indent-command)

(defconst jflex-font-lock-keywords
  (append
   '(
     ("^%%" . font-lock-reference-face)
     "^%{"
     "^%init{"
     "^%initthrow{"
     "^%eof{"
     "^%eofthrow{"
     "^%yylexthrow{"
     "^%eofval{"
     "^%}"
     "^%init}"
     "^%initthrow}"
     "^%eof}"
     "^%eofthrow}"
     "^%yylexthrow}"
     "^%eofval}"
     "^%standalone"
     "^%scanerror"
     "^%states" ; fixme: state identifiers
     "^%state"
     "^%s"
     "^%xstates"
     "^%xstate"
     "^%x"
     "^%char"
     "^%line"
     "^%column"
     "^%byaccj"
     "^%cupsym"
     "^%cupdebug"
     "^%cup"
     "^%eofclose"
     "^%class"
     "^%function"
     "^%type"
     "^%integer"
     "^%intwrap"
     "^%int"
     "^%yyeof"
     "^%notunix"
     "^%7bit"
     "^%full"
     "^%8bit"
     "^%unicode"
     "^%16bit"
     "^%caseless"
     "^%ignorecase"
     "^%implements"
     "^%extends"
     "^%public"
     "^%apiprivate"
     "^%final"
     "^%abstract"
     "^%debug"
     "^%pack"
     "^%include"
     "^%buffer"
     "^%initthrow"
     "^%eofthrow"
     "^%yylexthrow"
     "^%throws"
     "^%warn"
     "^%no-warn"
     "^%suppress"
     "^%no_suppress_warnings"
     ("%[%{}0-9a-zA-Z]+" . font-lock-warning-face) ; errors
     ("{[ \t]*[a-zA-Z][0-9a-zA-Z_]+[ \t]*}" . font-lock-variable-name-face) ; macro uses
     "<<EOF>>" ; special <<EOF>> symbol
     ("<[ \t]*[a-zA-Z][0-9a-zA-Z_]+[ \t]*\\(,[ \t]*[a-zA-Z][0-9a-zA-Z_]+[ \t]*\\)*>" . font-lock-type-face) ; lex state list
     )
   java-font-lock-keywords-2)
  "JFlex keywords for font-lock mode")

(put 'jflex-mode 'font-lock-defaults
	 '(jflex-font-lock-keywords
	   nil nil ((?_ . "w")) beginning-of-defun))

(provide 'jflex-mode)
