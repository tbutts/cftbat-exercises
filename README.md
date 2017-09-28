# cftbat-exercises

I'm learning Clojure using [CftBaT](https://www.braveclojure.com/clojure-for-the-brave-and-true/),
a delightful book about a fun, modern Lisp language for the JVM.

This repo contains my complete & partial solutions for exercises provided
at the end of most chapters.

Many things are done the way they are, as part of the learning & exploration process
of both Clojure the language, and the ecosystem.

## Running

Clojure files - ending in `*.clj` - can be executed standalone with the
`clojure <path/to/file.clj>` command, but that unfortunately won't do anything
interesting.

All solution cracking happened within `emacs`, with
the amazing [nRepl and CIDER plugins](https://github.com/clojure-emacs/cider).

### In Emacs

With those plugins and inside emacs, you can start a REPL linked to the project
by opening one of my .clj files, and then typing `M-x cider-jack-in` or `M-c C-j`.
That is the notation for entering emacs commands, and if that isn't making sense,
try reading [Chapter 2 of CftBaT](https://www.braveclojure.com/basic-emacs/),
which covers getting used to Emacs.

Once the interactive environment is up, emacs w/ CIDER will let you:
* Compile the project (`C-c C-k`)
* Run tests (`C-c C-t C-l`)
* Run bits of code under the cursor/point (`C-c e`)
* And many other things, if you [read the docs!](https://cider.readthedocs.io/)

### From the CLI with lein

You can build the project or run tests stand alone using
[leiningen](https://leiningen.org/).

* Build and run the project with `lein run`, which does nothing more than
  print a message and confirms the project is syntactically valid.
* Start the REPL `lein repl`, from which you can run `(-main)` for help!
* Run tests using `lein test <namespace from the top of any file>`
    * You have to specify the individual namespaces, as I did not separate tests
    into separate files, because it made iterating through exercises faster.

## Other resources

In addition to everything else already mentioned, I leaned on many other
knowledge fountains:
* [ClojureDocs](https://clojuredocs.org/)
* [Eli Bendersky's Blog](https://eli.thegreenplace.net/archives/all)
* [Kyle Kingsbury Blog - "Aphyr"](https://aphyr.com/tags/Clojure)
* [Community Clojure style guide](https://github.com/bbatsov/clojure-style-guide)
* [Emacs reference card](https://www.gnu.org/software/emacs/refcards/pdf/refcard.pdf) (the 'card' is two 8x11.5" pieces of paper)

