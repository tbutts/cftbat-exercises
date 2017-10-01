# Chapter Review Commentary Notes

Here lies a ~~very brief~~ summarization of each chapter from the CFTBAT book.

- [Chapter 1 - Building, Running, and the REPL](#chapter-1-building-running-and-the-repl)
- [Chapter 2 - How to Use Emacs, an Excellent Clojure Editor](#chapter-2-how-to-use-emacs-an-excellent-clojure-editor)
- [Chapter 3 - Do Things: A Clojure Crash Course](#chapter-3-do-things-a-clojure-crash-course)
- [Chapter 4 - Core Functions in Depth](#chapter-4-core-functions-in-depth)
- [Chapter 5 - Functional Programming](#chapter-5-functional-programming)
- [Chapter 6 - Organizing Your Project: A Librarian's Tale](#chapter-6-organizing-your-project-a-librarians-tale)
- [Chapter 7 - Clojure Alchemy: Reading, Evaluation, and Macros](#chapter-7-clojure-alchemy-reading-evaluation-and-macros)
- [Chapter 8 - Writing Macros](#chapter-8-writing-macros)

## Chapter 1 - Building, Running, and the REPL

[Web link](https://www.braveclojure.com/getting-started/)

Introductions to dense topics are best left short, so as not to overwhelm the
reader. CftBaT does wonderful at introducing the oh-so-startling world of REPL-
driven development, a whole new build tool: [leiningen](https://leiningen.org/),
Lisp syntax, alternative languages hosted on the JVM, and Taylor Swift.

All of this fits in a slim dozen pages, as Higginbotham gently places the
specific subjects, such as how Clojure ties into the JVM, on the back burner.


## Chapter 2 - How to Use Emacs, an Excellent Clojure Editor

[Web link](https://www.braveclojure.com/basic-emacs/)

This chapter covers Emacs, a separate entity all together, following the minimum
steps to become a pleasant Clojure-smith, annealing s-expressions at a whim.
Although there are certainly more complete texts pertaining to Emacs, the
accompanying screenshots and key binding tables, I feel, are unique to this
tutorial. Along with the writing style, it gives this chapter greater value
that what I imagine are much more dry introductions to Emacs.

Emacs turned out to be both one of the largest stumbling blocks, and sources of
enjoyment while learning Clojure. As a steady `vim` user for many years, the
fluidity of movement and text manipulation felt cleaner compared to `vim`-style
modal editing. Perhaps this is simply a coincidence of age & experience coming
together to make the learning process of such a complex tool a more brisk,
rewarding experience. I can safely say, however, that I will continue to adapt
Emacs into my workflow.


## Chapter 3 - Do Things: A Clojure Crash Course

[Web link](https://www.braveclojure.com/do-things/)

Chapter 3 marks the beginning of the 'meat' of this Clojure learning experience.
It carries the burden of introducing:
* Every core data structure (Strings, maps, keywords, vectors, lists, sets, etc.)
* Higher-order functions (functions are values, which can be returned and saved with ease)
* Special forms (operations which are not functions, but allow for specific required
  functionality in the language; e.g. `def`, `if`, and `do`)
* Macros (which the text only mentions in passing, which I consider a good choice at this
  point in a newbie's understanding; e.g. `and` and `or`)
* Arity overloading (key to the coming explanations on recursion)
* Destructuring (a very handy syntactical quirk over function args)
* `let` bindings (the unique way of declaring 'variables' as it were, in Clojure)
* `loop` & `recur`, and `reduce` (two of the most powerful iterating functions)
* ... and providing context to glue all of this together, coherently.

As is, I found this chapter too daunting for a newbie's first dive into the
language. There are plenty of code samples, and the REPL always helps to clear
up issues, but the content feels stuffed to the brim. I suppose it was with the
whimsical hobbit generating exercise that took me more head scratching than I
would have wished for. By introducing `let`, `into`, and `loop` & `recur` all at
once, my mind became jumbled with the back and forth as I was trying to
understand the whole program quickly. I do appreciate the effort that went into
such an expansive example, though


## Chapter 4 - Core Functions in Depth

[Web link](https://www.braveclojure.com/core-functions-in-depth/)

Here, Higginbotham presents the wonderful suite of sequence & collection
functions that are so core the general idea of FP:
* `first`, `last`, `nth` (for accessing elements)
* `take`, `drop`, `head`, `tail` (selecting n number elements)
* `take-while`, `drop-while`, `filter`, `some` (selecting elements w/ custom logic)
* `cons` (add 1 elem to head of seq), `concat` (append one seq to another), `into`
  (place elems of from coll into target coll, pos based on target's coll type),
  `conj` (based on collection type, add n elems)
* `map` (the ever powerful, arbitrary, 1-to-1 seq manipulator!)

These tools form an expressive language for working with collections. I have had
the pleasure of using these functions in Python, Java 8, and JS, where I feel
they consistently improve readability.

Clojure performance also gets brought up. The lightweight collections which
enable immutability, as well as the idea of lazy sequences, combine at the
language level to give Clojure a leg up on other, imperative languages.

For further manipulating functions, we are introduced to `apply` (call a func
with a seq where each element becomes an argument) and `partial`
(create a new func with some args 'locked in').

All of this is wrapped up with the silly context of a vampire identification
program. This gives us file I/O (`slurp` & `spit`) and an interesting exercise
in CSV parsing.


## Chapter 5 - Functional Programming

[Web link](https://www.braveclojure.com/functional-programming/)

This chapter explores the important concepts that FP centers around:
+ Pure functions (no side-effects)
+ Immutable data (functions return new, updated data, instead of updating data
  in place)
+ Function composition (encourages tiny functions which are put together like
  lego)

These ideas are not unique to FP, and can in fact be applied in just about every
language. Clojure's core encourages these aspects of programming, however, which
makes these ideas natural.

We 'close' the chapter with over a dozen pages of _Peg Thing_, a game that
molds these ideas together. I found this example fun and very complete. The
interactivity through stdin, combined with the REPL for testing individual
pieces, made grokking my first larger Clojure program an easy and delightful
experience. Plus, you know, everybody loves Cracker Barrel.


## Chapter 6 - Organizing Your Project: A Librarian's Tale

[Web link](https://www.braveclojure.com/organization/)

Dialling things back, the book takes a breath to explain how files in Clojure
projects are organized, and how they refer to each other as needed. In short,
Clojure namespaces & importing work like you'd expect (unlike Python, where
import is a lot more magic and confusing, due to churn). Namespaces contain any
number of Vars, which are 'interned' with `def`, `defn`, etc. and isolated from
other namespaces to prevent conflicts (so you may have your own `get`, `+`, or
anything). Finally, directories on the filesystem directly correspond with the
namespace name, other than underscores being replaced with dashes in .clj files.

Pulling in other Clojure code is best summed up in the
[NS cheatsheet](https://gist.github.com/ghoseb/287710/):
* `:require` makes functions available with a namespace prefix
  and optionally can refer functions to the current ns
    * `(ns foo.core (:require [clojure.string :as string :refer [lower-case]))`
    * The above example is requiring a namespace, giving it an alias, and
      directly adding the `lower-case` function to the current ns.
* `:import` refers Java classes to the current namespace
    * `(ns foo.core (:import [java.util ArrayList HashMap]))`
* `:refer-clojure` allows you to exclude or only add specific core functions
    * `(ns foo.core (:refer-clojure :only [print]))`
    * `(ns foo.core (:refer-clojure :exclude [print println]))`


## Chapter 7 - Clojure Alchemy: Reading, Evaluation, and Macros

[Web link](https://www.braveclojure.com/read-and-eval/)

In most languages, learning about how syntax is transformed into runnable
instructions is reserved for standard library contributors and curious folks.
In Lisps such as Clojure, control of the way all those ASCII symbols get
executed on the CPU is available to all, through macros. Between reading but
and evaluating source code, there exists a phase called 'macro expansion', that
allows the AST (abstract syntax tree - the parsed tokens of the code) to be
manipulated, just like any other piece of data!

This concept is made clear through these diagrams from the book:
+ [Clojure Evaluation](https://www.braveclojure.com/assets/images/cftbat/read-and-eval/lisp-eval.png)
+ [Evaluation with Macro Expansion](https://www.braveclojure.com/assets/images/cftbat/read-and-eval/whole-shebang.png)

Standard macros appear as the first argument to an expression, such as
`(and ...)`. At the same time, there are the much more convenient _reader
macros_, such as `#(- %1 my-var)` or `'("a" "b" "c")`, which appear before the
s-expression. Reader macros cannot be implemented by regular users, which is a
choice by the Clojure designers that I agree with them on.

All this metaprogramming can be confusing, so the book opts to split this
chapter and the next up (_Writing Macros_). This is another good decision I
agree with, not least because it took me the greater part of a day to complete
the exercise on this chapter (writing a function to calculate a string of an
infix math expression; very interesting in Clojure!).

Whenever struggling with developing or using macros, be sure to rearch for the
core function `macroexpand`, used on any clojure list `'()`. Example:
``` clojure
(macroexpand '(and true false {} nil))
; => (let* [and__4467__auto__ true] (if and__4467__auto__ (clojure.core/and false {} nil) and__4467__auto__)
```


## Chapter 8 - Writing Macros

[Web link](https://www.braveclojure.com/writing-macros/)

The complete guide to writing Clojure macros is right here. This chapter starts
where the previous left off, and carefully explains how to naturally write
macros, including the new tools required to work around issues unique to macro
writing, and how to deal with common pitfalls such as variable capture and
double evaluation. As a whole, I can't imagine a more comprehensive guide on
this subject. Higginbotham makes macros approachable, while also making it clear
they are not to be used in every situation. The sample he uses for validation is
appropriate and powerful. I've also seen macros used to succinctly define
repetitive functions in well-regarded Clojure libraries.

A brief review of the reader macros used in writing macros:
* Simple Quoting: ` '(...) `
* Syntax Quoting: ``` `(...) ```
    * Unquoting: ``` `(inc ~my-var) ```
    * Unquote splicing ``` `(+ ~@args-to-add) ```
* Create unique names with `gensym`, or by adding a `#` to the end of a name
  binding while syntax quoting.


<!-- Todo: Finish the remaining 5 chapters -->

