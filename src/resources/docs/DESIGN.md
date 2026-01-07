
---

## ✅ `DESIGN.md`

# NoEl Design Rationale

This document explains why NoEl is designed the way it is.
It is not a specification and does not define syntax.

---

## Why NoEl Exists

Most font systems describe what letters look like.
NoEl describes how letters are constructed.

This distinction drives all design decisions.

...and, this is fun!

---

## Letters as Functions

Letters are functions because they:
- encapsulate structure
- enable reuse
- allow controlled exposure

A letter is not an image.
It is a procedural description.

---

## Semantic Geometry

Humans think about writing in terms of:
- baseline
- ascenders
- descenders

Not pixels.

NoEl’s coordinate system mirrors how handwriting and typography are taught.

---

## Returns as Visibility Boundaries

Instead of access modifiers, NoEl uses returns.

If something is returned, it is usable.
If it is not returned, it is private.

This creates explicit, intentional composition.

---

## Constraints Are a Feature

NoEl intentionally avoids:
- loops
- mutation
- complex control flow

This prevents accidental complexity and unreadable glyph logic.

---

## Interpreter vs Renderer

The language does not render itself.

Separating interpretation from rendering allows:
- clearer semantics
- easier experimentation
- multiple render targets

---

## Design Philosophy Summary

- Explicit over implicit
- Meaning over flexibility
- Structure over cleverness
- Constraints over features

---

## Status

This document reflects current intent and may evolve.
