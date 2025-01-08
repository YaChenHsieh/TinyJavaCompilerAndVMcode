# TinyJavaCompilerAndVMcode
This project is based on the TinyJ Compiler Assignment from CSCI 316 (Principles of Programming Languages) at CUNY Queens College, designed and provided by Professor Tat Yung Kong. Some parts of the code were provided by Professor Kong, while I have modified and extended certain sections as part of my coursework.

## TinyJ Assignment 1 - Compiler Workflow Explanation

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
