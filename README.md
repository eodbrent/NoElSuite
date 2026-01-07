# NoElSuite

**NoElSuite** is a collection of projects centered around **NoEl** — a custom, domain-specific programming language designed specifically for **procedural handwriting, glyph construction, and typographic rendering**.

NoEl is not a general-purpose language.  
It is a *drawing language with intent*, where every construct exists to describe **how a letter is built**, not merely how it looks.

This repository represents the **design, interpreter, and rendering ecosystem** around NoEl.

---

## What Is NoEl?

#### **NoEl** (short for *“No-E Language”*, a personal nod and not a technical constraint) is a language where:

- Letters are *functions*
- Lines, arcs, and shapes are *first-class primitives*
- Coordinates are **semantic**, not pixel-absolute
- A glyph is *code*, not a static asset

#### **NoEl** sits somewhere between:

- a font format,
- a vector drawing DSL,
- and a programming language inspired by *Crafting Interpreters*.

The end goal is **human-like handwriting**, not mathematically perfect typography.

---
## Repository Structure

This repository may contain (or evolve to contain) multiple related modules:

- **NoElang** – the language grammar, interpreter, and semantic model
- **NoElFX** – a JavaFX-based rendering engine for NoEl primitives
- **ci_NoEl** – a learning / sandbox fork following *[Crafting Interpreters](https://craftinginterpreters.com/)* patterns
- Supporting tools, experiments, and documentation

Not all components are finalized or fully decoupled yet — this is an *actively evolving language project*.

---

## Core Language Concepts

### Letters Are Functions

Each *letter* is defined as a function:

```noel
def letter B() {
    ...
}
```
A letter function:
- declares primitives (lines, arcs, etc.)
- declares variables
- returns selected data for reuse

Letters may compose or reuse other letters through controlled exposure.

---
### Drawing Primitives
NoEl currently supports a small, intentional set of primitives:
- line
- arc (standard, requiring angle and angle extent)
- quadratic curves
- Bézier curves (...soon)

Primitives are **data**, not immediate draw calls.  
&nbsp;&nbsp;&nbsp;&nbsp;They are interpreted first, rendered later.

#### Example:
```noel
line stem = [x1, y1, x2, y2].
```
---
### Semantic Coordinate System
#### NoEl avoids pixel-based coordinates.
&nbsp;&nbsp;Instead, coordinates are:
- normalized
- relative to font metrics
- expressed using meaningful vertical landmarks or as percentages of the font height.

#### Vertical keywords include:
- baseline
- ascender
- capheight
- xheight
- descender

#### Example:
```noel
let yTop = ascender.
let yBottom = baseline.
```
### Variables (let)

Variables are declared using let and are immutable:
```
let x1 = 0.05.
let y1 = ascender.
```
Variables improve clarity and allow reuse within glyph definitions.

---
### Returns as Visibility Control
A definition’s return statement determines what is externally accessible.

Example:
```
return line.
```

Only [all] line primitives are exposed.

Other options include:
```
return let.
return x1, y1, stem.
```
*This acts as a public / private boundary without explicit access modifiers.*

---
### Composition via uses

Letters may depend on other letters:
```
def letter B() uses l {
    let x = l.stem.x1. 
}
```

**Only explicitly returned members may be accessed.**

---
### Comments
```
-- inline comments inspired by Lua.
-- block comments (comming ...eventually).
```
---
### User-Defined Functions (Planned)

NoEl will support helper functions:
```
def function myFunc() {
    ...
}
```

These follow the same return and visibility rules as letters and exist to reduce duplication - not to generalize the language.

---
### Rendering Model
NoElSuite separates interpretation from rendering:
1. Source → Tokens
2. Tokens → AST
3. AST → semantic glyph model
4. Glyph model → renderer (NoElFX)
   The language itself does not render.

---
### Project Status
*NoElSuite is an active work in progress.*
Expect:
- breaking changes
- refactors
- evolving semantics
  This repository reflects both implementation and learning.

---

Documentation

```LANGUAGE_SPEC.md``` — formal language definition  
```DESIGN.md``` — design rationale and intent  
```CONTRIBUTING.md``` — contribution philosophy and guidelines

## Inspiration
Crafting Interpreters - Robert Nystrom  
Typography and handwriting pedagogy  
The idea that letters are constructed, not drawn

### License
To be determined.