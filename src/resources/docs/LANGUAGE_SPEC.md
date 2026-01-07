# NoEl Language Specification (Draft)

This document defines the formal syntax and semantics of the NoEl language.

NoEl is a domain-specific language (DSL) for procedural handwriting and glyph construction.
It is intentionally constrained and is not a general-purpose programming language.

This specification describes how the language behaves, not how it is implemented.

---

## 1. Program Structure

A NoEl program consists of one or more top-level definitions.

Valid top-level definitions:
- `letter`
- `function`
- `number` (reserved, not yet implemented)
- `symbol` (reserved, not yet implemented)
- `glyph`  (potentially)

---

## 2. Definitions

### 2.1 Letter Definitions

Syntax:

    def letter <Name>() [uses <OtherLetter>] {
        <statements>
    }

- `<Name>` identifies the glyph being defined
- `uses` declares a dependency on another letter
- A letter body contains variable declarations, primitives, and a return statement

Example:

    def letter l() {
        ...
    }

---

### 2.2 Function Definitions

Syntax:

    def function <Name>() {
        <statements>
    }

Functions:
- exist to reduce duplication
- may be called by letters or other functions
- follow the same return and visibility rules as letters

Functions are intentionally limited in scope and purpose.

---

## 3. Statements

Valid statements inside a definition:

- Variable declarations (`let`)
- Primitive declarations (`line`, `arc`, etc.)
- Return statements (`return`)

Control flow constructs (loops, mutation, conditionals) are intentionally excluded.

---

## 4. Variables (`let`)

Variables are declared using `let` and are immutable.

Syntax:

    let x1 = 0.05.
    let y1 = ascender.

Variables may reference:
- numeric literals
- previously declared variables
- semantic coordinate keywords

Variables are local to their definition unless returned.

---

## 5. Coordinate System

### 5.1 Conceptual Model

NoEl uses a normalized, semantic coordinate system.

- The vertical origin is the `baseline`
- Vertical values represent percentages of `fontHeight`
- Horizontal values are normalized (0.0 – 1.0)

Coordinates are interpreted, not pixel-based.

---

### 5.2 Semantic Vertical Keywords

The following identifiers are valid coordinate values:

| Keyword     | Meaning |
|------------|--------|
| ascender  | Ascender height |
| capheight | Capital letter height |
| xheight   | Lowercase x height |
| baseline   | Text baseline |
| descender | Descender depth |

Example:

    let yTop = ascender.
    let yBottom = baseline.

---

## 6. Primitives

### 6.1 Line

Syntax:

    line <name> = [x1, y1, x2, y2]

Defines a straight line segment.

Example:

    line stem = [0.05, ascender, 0.05, baseline]

---

### 6.2 Arc

Arc primitives exist conceptually but may evolve as rendering stabilizes.
Quadratic Curves and Bézier curves are being implemented.

---

## 7. Return Semantics (Visibility Control)

Each definition may include one return statement.
The return statement determines which internal elements are externally accessible.

---

### 7.1 Return by Type

    return line

Only and all line primitives are exposed.

---

### 7.2 Return Variables

    return let

All user defined variables are exposed.

---

### 7.3 Explicit Return List

    return x1, y1, stem

Only explicitly named elements are exposed.

---

### 7.4 No Return

If no return statement is present:
- Nothing is externally accessible
- The definition may still render internally

---

## 8. Accessing Returned Members

Returned elements are `currently` accessed using dot notation:

    l.stem.x1
    l.y1

Access is only valid if the element was returned.

---

## 9. Composition (`uses`)

A definition may declare a dependency:

    def letter B() uses l {
        let x = l.stem.x1
    }

`uses` allows access only to returned members of the dependency.

---

## 10. Execution Model (Conceptual)

1. Source code is tokenized
2. Tokens produce an abstract syntax tree (AST)
3. The AST produces a semantic glyph model
4. The renderer consumes glyph data

NoEl itself does not perform rendering.

---

## 11. Non-Goals

NoEl intentionally does not support:
- loops
- mutation
- IO
- dynamic control flow

These constraints are deliberate.

---

## Status

This specification is a living document and will evolve with the language.
