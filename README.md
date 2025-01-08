# TinyJavaCompilerAndVMcode
This project is based on the TinyJ Compiler Assignment from CSCI 316 (Principles of Programming Languages) at CUNY Queens College, designed and provided by Professor Tat Yung Kong. Some parts of the code were provided by Professor Kong, while I have modified and extended certain sections as part of my coursework.

## TinyJ Assignment 1 - Workflow Explanation

## ✅ Phase 1: Lexical Analysis (Tokenization)
**Goal:** Convert source code into a sequence of tokens.

### **Step 1: Tokenizing the Code**
- A **Lexical Analyzer (Tokenizer)** scans the source code and generates tokens.
- Tokens represent the smallest units of meaning in the source code (e.g., keywords, identifiers, literals).

### **Example:**
```java
int x = 5;
x = x + 2;
```
**Generated Tokens:**
```plaintext
Token(INT), Token(ASSIGN, "="), Token(SEMICOLON, ";")
```

---

## ✅ Phase 2: Parsing (Syntax Analysis)
**Goal:** Build a parse tree to represent the syntactic structure of the code.

### **Step 2: Building the Parse Tree**
- **Recursive Descent Parser** checks if the sequence of tokens conforms to the TinyJ grammar.
- If a token violates the grammar, a **syntax error** is reported.

### **Step 3: Example Parse Tree Generation**
**Source Code:**
```java
int x = 5;
x = x + 2;
```
**Parse Tree (Sideways Representation):** see the .sol provided by professor
```plaintext
<program>
   <varDecl>
      int x = 5
   <assignment>
      x = x + 2
```

---

## ✅ Phase 3: Syntax Error Handling
- If a syntax error occurs (e.g., missing `;` or incorrect expression), the parser should generate a meaningful error message indicating:
  - **Error Type** (e.g., `Unexpected Token`)
  - **Line Number**
  - **Expected Token**

---

## ✅ Phase 4: Parse Tree Output
**Goal:** Generate a parse tree in a structured format for debugging and validation.
- The tree should be printed in a **sideways representation**, with indentation representing depth.

### **Example Output:**
```plaintext
<program>
   <varDecl>
      int x = 5
   <assignment>
      x = x + 2
```

## **TinyJ Assignment 2 - Workflow Explanation **

This is the workflow for **TinyJ Assignment 2**, focusing on **Semantic Analysis** and **Intermediate Code Generation** for the TinyJ compiler project.

---

## ✅ **Phase 1: Semantic Analysis**
**Goal:** Verify variable declarations, type correctness, and scope rules.

### **Step 1: Build the Symbol Table** 
- Create a **symbol table** using a data structure such as a `HashMap` or `TreeMap`.
- Each identifier (`IDENTIFIER`) entry should store:
  - **Name** (e.g., `x`)
  - **Data type** (`int` or `Scanner`)
  - **Scope** (`local` or `global`)
  - **Memory Location** (Stack offset or static address)
- A **scope stack** is used for nested blocks `{}` to handle variable scoping correctly.

---

### **Step 2: Declaration Check**
- When encountering a variable declaration (e.g., `int x = 5;`):
  - Check if the variable already exists in the current scope.
  - If **not declared yet**, add it to the symbol table with the type and address.
  - If **already declared**, throw an error like:  
    `"Error: Variable x already declared."`

---

### **Step 3: Usage Check**
- When encountering a variable usage (e.g., `y = x + 2;`):
  - Verify if the variable `x` exists in the symbol table.
  - If it doesn’t exist, report an error:  
    `"Error: Variable x not declared."`

---

### **Step 4: Scope Management**
- When entering a new block `{}`:
  - Push a new scope onto the symbol table stack.
- When exiting a block:
  - Pop the current scope from the stack.

---

### **Step 5: Error Handling**
- If any error is detected during **semantic analysis**, compilation should halt, and a meaningful error message should be displayed.


---

## ✅ **Phase 2: Intermediate Code Generation (ICG)**
**Goal:** Convert the parsed TinyJ source code into stack-based virtual machine instructions.

---

### **What is Intermediate Code?**
- **Intermediate Code (IC)** is a low-level representation between the TinyJ source code and actual machine code. 
- TinyJ uses a **stack-based virtual machine** where all operations use a stack for computation.

---

### **Step 6: Convert the Parse Tree into Intermediate Code**
**Example Source Code:**
```java
int x = 5;
x = x + 2;
```

**Parse Tree:**
```
<program>
  <varDecl>
    int x = 5;
  <assignment>
    x = x + 2;
```

**Generated Intermediate Code (Stack-Based VM Instructions):**
```plaintext
PUSHSTATADDR 0          // Push the static address of 'x' to the stack
PUSHNUM 5               // Push the integer value 5 onto the stack
SAVETOADDR              // Save the value from the stack top to 'x'

PUSHSTATADDR 0          // Push the static address of 'x' again
LOADFROMADDR            // Load the value of 'x' onto the stack
PUSHNUM 2               // Push the integer value 2 onto the stack
ADD                     // Add the top two values on the stack
SAVETOADDR              // Save the result back to 'x'
```

---

### **Step 7: Code Generation Process (Tree Traversal)**
The code generation process works by **traversing the parse tree** (often in a **preorder traversal**) and generating VM instructions for each node:

- **Variable Declaration (`int x = 5;`)**
  ```
  PUSHSTATADDR <address>
  PUSHNUM <value>
  SAVETOADDR
  ```
  
- **Addition (`x = x + 2;`)**
  ```
  PUSHSTATADDR <address>
  LOADFROMADDR
  PUSHNUM <value>
  ADD
  SAVETOADDR
  ```

- **If-Condition (`if (x > 2)`)**
  ```
  PUSHSTATADDR <address>
  LOADFROMADDR
  PUSHNUM <value>
  GT
  JUMPONFALSE <target>
  ```
---

## TinyJ Assignment 3 - Virtual Machine Execution

This workflow for **TinyJ Assignment 3**, focusing on the final phase of a compiler: executing compiled code using a virtual machine (VM). The primary goal of this assignment is to implement the **execution phase** of the TinyJ compiler by completing the virtual machine's instruction set.

---

### **Step 1: Load and Read VM Instructions**
- The VM reads the `vm` file containing the generated instructions.
- Instructions are stored in `TJ.generatedCode` for execution.

### **Step 2: Initialize VM Components**
- **Registers:** `PC` (Program Counter), `ESP` (Expression Stack Pointer), `FP` (Frame Pointer), `ASP` (Allocated Stack Pointer).
- **Stack:** `EXPRSTACK[]` for expression evaluations.
- **Memory:** `TJ.data[]` for variable storage.

### **Step 3: Execute Instructions (Fetch-Decode-Execute Cycle)**
**Fetch:**
- The VM fetches the next instruction using `PC`.
- `PC` is incremented after reading the instruction.

**Decode:**
- The instruction is decoded and loaded into `IR` (Instruction Register).

**Execute:**
- Each instruction executes its `execute()` method (e.g., `ADD`, `PUSHNUM`).
- Example (`ADDinstr.java`):
   ```java
   public void execute() {
       EXPRSTACK[--ESP-1] += EXPRSTACK[ESP];
   }
   ```

### **Step 4: Method Calls and Returns**
- **Method Handling Instructions:** `CALLSTATMETHOD`, `RETURN`, `INITSTKFRM`.
- The VM saves the current `PC` when calling a method and restores it upon returning.

### **Step 5: Program Termination**
- The program terminates when `PC` exceeds the instruction list.
- If an error occurs (e.g., uninitialized variable access), the VM should display error messages.

---

### ✅ Why a Virtual Machine (VM)?
The TinyJ compiler uses a **stack-based virtual machine** for educational purposes:
- **Hardware Abstraction:** No direct interaction with physical CPUs.
- **Portability:** `vm` files can run on any TinyJ-compatible VM.
- **Simplified Design:** Easier to understand how code execution works.



