
---

## Overview

This project implements the **code generation phase** of a Badlang compiler.
The compiler translates a Badlang program into **MIPS assembly**, which can be executed using SPIM or other MIPS simulators.

The goal of this project is to generate correct and executable assembly that fully implements the semantics of Badlang, including:

* Variables and assignments
* Arithmetic and boolean expressions
* If / while control flow
* Function definitions and calls
* Return statements
* Print statements
* Proper stack frame handling

---

## Architecture Choice

**Target architecture:** MIPS
**Simulator used:** SPIM (online or local)

The compiler outputs valid MIPS assembly that follows the required constraints:

* All variables and temporaries live on the **stack**
* Only registers `$t0` and `$t1` are used for intermediate computation
* No variable named `b` and no function named `add`
* Code is generated in **three passes** (data, functions, main)
* A newline is printed after every `print` statement
* Execution ends with an exit syscall
* Function stack frames follow the convention:

  * First block has offset `-(8 + params.size * 4)`
  * `$fp` is **not** updated inside nested blocks

---

## How to Build and Run

### **Compile the compiler**

```
javac edu/wisc/Main.java
```

### **Run the compiler**

```
java edu.wisc.Main <source-file> [output-file]
```

* If `output-file` is omitted, the compiler writes assembly to `out.s`.

### **Run the generated MIPS code**

Open the generated `.s` file in SPIM (desktop or online):

1. Load the file
2. Press **Run**
3. Verify the output matches the expected Badlang behavior

---

## Implementation Notes

### **Stack Model**

* Each function creates a stack frame containing:

  * saved `$fp`
  * return address `$ra`
  * function parameters
  * local variables
* All expressions load operands into `$t0` and `$t1` only
* Results are immediately written back to the stack

### **Control Flow**

* `if` / `else` and `while` are implemented using unique labels
* Conditions evaluate to 0 or 1, consistent with Badlang boolean semantics

### **Printing**

`print` outputs integers or booleans (as **0 or 1**), followed by a newline.

---

## Testing

I tested the compiler using an online SPIM environment:

1. Generate MIPS code with the compiler
2. Copy and paste the assembly into SPIM
3. Run and compare output with the expected behavior of the original Badlang program

I verified all major language features:

* Arithmetic and boolean expressions
* Variable assignment and scopes
* Blocks `{}`
* If–else statements
* While loops
* Functions (parameters, return values, nested calls)
* Printing
* Programs using multiple functions

---

## Test Programs Included

The `tests/` folder contains at least **10 example Badlang programs**, covering:

* Simple arithmetic
* Boolean logic
* Nested blocks
* If / if-else
* Loops
* Recursive function
* Function parameters and return
* Variable shadowing
* Mixed prints
* A full “all features” program

---
