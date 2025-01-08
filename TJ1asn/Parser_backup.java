/*
 *    Name : YaChen Hsieh
 *    Tiny J Assignment #1
 */

package TJ1asn;

import static TJlexer.LexicalAnalyzer.getCurrentToken;
import static TJlexer.LexicalAnalyzer.nextToken;
import static TJlexer.Symbols.*;
import TJlexer.Symbols;


// ************************************ Recursive Descent Parser **************************************

public final class Parser {

  private static void accept (Symbols expectedToken) throws SourceFileErrorException
  {
    if (getCurrentToken() == expectedToken)
      nextToken();
    else throw new SourceFileErrorException("Something's wrong--maybe the following token is missing: "
                          + expectedToken.symbolRepresentationForOutputFile);
  }


  static void program () throws SourceFileErrorException
  {
    TJ.output.printSymbol(NTprogram);
    TJ.output.incTreeDepth();

    if (getCurrentToken() == IMPORT) importStmt();

    accept(CLASS);
    accept(IDENT);
    accept(LBRACE);

    while (getCurrentToken() == STATIC)
      dataFieldDecl();

    mainDecl();

    while (getCurrentToken() == STATIC)
      methodDecl();

    accept(RBRACE);

    TJ.output.decTreeDepth();
  }


  private static void importStmt() throws SourceFileErrorException
  {
    TJ.output.printSymbol(NTimport);
    TJ.output.incTreeDepth();

    accept(IMPORT);
    accept(JAVA);
    accept(DOT);
    accept(UTIL);
    accept(DOT);
    accept(SCANNER);
    accept(SEMICOLON);

    TJ.output.decTreeDepth();
  }


  private static void dataFieldDecl() throws SourceFileErrorException
  {
    TJ.output.printSymbol(NTdataFieldDecl);
    TJ.output.incTreeDepth();

    accept(STATIC);
    varDecl();

    TJ.output.decTreeDepth();
  }


  private static void varDecl() throws SourceFileErrorException
  {
    TJ.output.printSymbol(NTvarDecl);
    TJ.output.incTreeDepth();

    if (getCurrentToken() == INT) {
      nextToken();
      singleVarDecl();
      while (getCurrentToken() == COMMA) {
        nextToken();
        singleVarDecl();
      }
      accept(SEMICOLON);
    }
    else if (getCurrentToken() == SCANNER) {
      nextToken();

      if (getCurrentToken() == IDENT) {
        nextToken();
      }
      else
        throw new SourceFileErrorException("Scanner name expected");

      accept(BECOMES);
      accept(NEW);
      accept(SCANNER);
      accept(LPAREN);
      accept(SYSTEM);
      accept(DOT);
      accept(IN);
      accept(RPAREN);
      accept(SEMICOLON);
    }
    else throw new SourceFileErrorException("\"int\" or \"Scanner\" expected");

    TJ.output.decTreeDepth();
  }


  private static void singleVarDecl() throws SourceFileErrorException
  {
    TJ.output.printSymbol(NTsingleVarDecl);
    TJ.output.incTreeDepth();

    /* ???????? */
    //1st
    // <singleVarDecl> ::= IDENTIFIER { '[' ']' } [ = <expr3> ]
    if(getCurrentToken() == IDENT) nextToken();
    else throw new SourceFileErrorException(" \"IDENT\" expected");
        
        //{ '[' ']' }-> 0 or more
        while (getCurrentToken() == LBRACKET){
        nextToken();
        accept(RBRACKET);
        }
        
        // [ = <expr3> ] -> 0 or 1
        if (getCurrentToken() == BECOMES) {
            nextToken();
            expr3();
            }
    // end of 1st

    TJ.output.decTreeDepth();
  }


  private static void mainDecl() throws SourceFileErrorException
  {
    TJ.output.printSymbol(NTmainDecl);
    TJ.output.incTreeDepth();

    accept(PUBLIC);
    accept(STATIC);
    accept(VOID);
    accept(MAIN);
    accept(LPAREN);
    accept(STRING);
    accept(IDENT);
    accept(LBRACKET);
    accept(RBRACKET);
    accept(RPAREN);

    compoundStmt();

    TJ.output.decTreeDepth();
  }


  private static void methodDecl() throws SourceFileErrorException
  {
    TJ.output.printSymbol(NTmethodDecl);
    TJ.output.incTreeDepth();

    /* ???????? */
    // <methodDecl> ::= static ( void | int { '[' ']' } ) IDENTIFIER '(' <parameterDeclList> ')' <compoundStmt>
    // 2nd

    //static ( void | int { '[' ']' } )
    accept(STATIC);
    
    if (getCurrentToken() == VOID) { nextToken(); }
    else {
        accept(INT);
        while (getCurrentToken() == LBRACKET) {
            nextToken();
            accept(RBRACKET);
            }
    }

    // IDENTIFIER '(' <parameterDeclList> ')' <compoundStmt>
    accept(IDENT);
    accept(LPAREN);
    parameterDeclList();
    accept(RPAREN);
    compoundStmt();
    
    // End of 2nd

    TJ.output.decTreeDepth();
  }


  private static void parameterDeclList() throws SourceFileErrorException
  {
    TJ.output.printSymbol(NTparameterDeclList);
    TJ.output.incTreeDepth();

    if (getCurrentToken() == INT) {
        parameterDecl();
        while (getCurrentToken() == COMMA) {
          nextToken();
          parameterDecl();
        }
    }
    else TJ.output.printSymbol(EMPTY);

    TJ.output.decTreeDepth();
  }


  private static void parameterDecl() throws SourceFileErrorException
  {
    TJ.output.printSymbol(NTparameterDecl);
    TJ.output.incTreeDepth();

    accept(INT);
    accept(IDENT);
    while (getCurrentToken() == LBRACKET) {
          nextToken();
          accept(RBRACKET);
    }

    TJ.output.decTreeDepth();
  }


  private static void compoundStmt() throws SourceFileErrorException
  {
    TJ.output.printSymbol(NTcompoundStmt);
    TJ.output.incTreeDepth();

    /* ???????? */
    // <compoundStmt> ::= '{' { <statement> } '}'
    //3rd

    accept(LBRACE);
    while(getCurrentToken() != RBRACE){
        statement();
    }
    accept(RBRACE);
    //end of 3rd

    TJ.output.decTreeDepth();
  }


  private static void statement() throws SourceFileErrorException
  {
    TJ.output.printSymbol(NTstatement);
    TJ.output.incTreeDepth();

    // <statement> ::= 
    // ; | return [<expr3>] ; | <varDecl> | <assignmentOrInvoc>
    // | <compoundStmt> | <ifStmt> | <whileStmt> | <outputStmt>

    switch (getCurrentToken()) {
      case SEMICOLON: nextToken(); break;
      case RETURN: nextToken();
                           if (getCurrentToken() != SEMICOLON)
                             expr3();
                           accept(SEMICOLON);
                           break;
      case INT: case SCANNER: varDecl(); break;
      case IDENT: assignmentOrInvoc(); break;
      case LBRACE: compoundStmt(); break;
      case IF: ifStmt(); break;
      case WHILE: whileStmt(); break;
      case SYSTEM: outputStmt(); break;
      default: throw new SourceFileErrorException("Expected first token of a <statement>, not "
                              + getCurrentToken().symbolRepresentationForOutputFile);
    }

    TJ.output.decTreeDepth();
  }


  private static void assignmentOrInvoc() throws SourceFileErrorException
  {
    TJ.output.printSymbol(NTassignmentOrInvoc);
    TJ.output.incTreeDepth();

    /* ???????? */
    // <assignmentOrInvoc> ::= IDENTIFIER ( { '[' <expr3> ']' } ) = <expr3> ; | <argumentList> ; )
    //4th
    //LPAREN-> (
    //LBRACKET->[
    //LBRACE-> {

    accept(IDENT);
    // ( { '[' <expr3> ']' } ) = <expr3> ;
    if (getCurrentToken() != LPAREN) {
      while (getCurrentToken() == LBRACKET) {
        nextToken();
        expr3();
        accept(RBRACKET);
      }
      accept(BECOMES);
      expr3();
      accept(SEMICOLON);
    }

    // <argumentList> ;
    // <argumentList> ::= '('[<expr3>{,<expr3>}]')'
    else if (getCurrentToken() == LPAREN) {
      argumentList();
      accept(SEMICOLON);
    }

    else
      throw new SourceFileErrorException(getCurrentToken().symbolRepresentationForOutputFile + " not expected here" + " (SOURCE: assignmentOrInvoc()");
    
    //end of 4th

    TJ.output.decTreeDepth();
  }


  private static void argumentList() throws SourceFileErrorException
  {
     TJ.output.printSymbol(NTargumentList);
     TJ.output.incTreeDepth();

     /* ???????? */
     // <argumentList> ::= '(' [ <expr3> ] { , <expr3> } ] ')'
     //5th

     accept(LPAREN);

     if(getCurrentToken() != RPAREN){
          expr3();
          while(getCurrentToken() == COMMA){
              nextToken();
              expr3();
              }
     }
     accept(LPAREN);
     //end of 5th

     TJ.output.decTreeDepth();
  }


  private static void ifStmt() throws SourceFileErrorException
  {
    TJ.output.printSymbol(NTifStmt);
    TJ.output.incTreeDepth();

    accept(IF);
    accept(LPAREN);
    expr7();
    accept(RPAREN);

    statement();

    if (getCurrentToken() == ELSE) {
      nextToken();
      statement();
    }

    TJ.output.decTreeDepth();
  }


  private static void whileStmt() throws SourceFileErrorException
  {
    TJ.output.printSymbol(NTwhileStmt);
    TJ.output.incTreeDepth();

    /* ???????? */
    // <whileStmt> ::= while '(' <expr7> ')' <statement>
    //6th
    accept(WHILE);
    accept(LPAREN);
    expr7();
    accept(RPAREN);
    statement();

    //end of 6th

    TJ.output.decTreeDepth();
  }


  private static void outputStmt() throws SourceFileErrorException
  {
    TJ.output.printSymbol(NToutputStmt);
    TJ.output.incTreeDepth();

    accept(SYSTEM);
    accept(DOT);
    accept(OUT);
    accept(DOT);

    switch (getCurrentToken()) {

      /* ????????*/
      // <outputStmt> ::= System.out.( print '(' <printArgument> ')' ; | println '(' [ <printArgument> ] ')' ; )
      // 7th
      case PRINT: 
          nextToken();
          accept(LPAREN);
          printArgument();
          accept(RPAREN);
          accept(SEMICOLON);
          break;
      case PRINTLN: 
          nextToken();
          accept(LPAREN);
          if(getCurrentToken() != RPAREN){
              printArgument();
              nextToken();
          }
          accept(RPAREN);
          accept(SEMICOLON);
          break;
      //end of 7th
      

      default: throw new SourceFileErrorException("print() or println() expected, not "
                              + getCurrentToken().symbolRepresentationForOutputFile);


    }

    TJ.output.decTreeDepth();
  }


  private static void printArgument() throws SourceFileErrorException
  {
    TJ.output.printSymbol(NTprintArgument);
    TJ.output.incTreeDepth();

    /* ???????? */
    // <printArgument> ::= CHARSTRING | <expr3>
    // 8th
    if(getCurrentToken() != CHARSTRING){
        expr3();
    }
    else{
        accept(CHARSTRING);
    }

    //end of 8th

    TJ.output.decTreeDepth();
  }


  private static void expr7() throws SourceFileErrorException
  {
    TJ.output.printSymbol(NTexpr7);
    TJ.output.incTreeDepth();

    /* ???????? */
    // <expr7> ::= <expr6> { '|' <expr6> }
    // 9th
    expr6();

    while(getCurrentToken() == OR ){
      nextToken();
      expr6();
    }
  
    //end of 9th

    TJ.output.decTreeDepth();
  }


  private static void expr6() throws SourceFileErrorException
  {
    TJ.output.printSymbol(NTexpr6);
    TJ.output.incTreeDepth();

    /* ???????? */
    // <expr6> ::= <expr5> { & <expr5> }
    // 10th
    expr5();
    while(getCurrentToken() == AND){
        nextToken();
        expr5();
    }
    //end of 10th

    TJ.output.decTreeDepth();
  }


  private static void expr5() throws SourceFileErrorException
  {
    TJ.output.printSymbol(NTexpr5);
    TJ.output.incTreeDepth();

    /* ???????? */
    // <expr5> ::= <expr4> { ( == | != ) <expr4> }
    // 11th

    expr4();

    while(getCurrentToken() == EQ || getCurrentToken() == NE){
        nextToken();
        expr4();

    }
    //end of 11th


    TJ.output.decTreeDepth();
  }


  private static void expr4() throws SourceFileErrorException
  {
    TJ.output.printSymbol(NTexpr4);
    TJ.output.incTreeDepth();

    /* ???????? */
    // NE("!=")
    // GT(">")
    // LT("<")
    // GE(">=")
    // LE("<=")
    // <expr4> ::= <expr3> [ ( > | < | >= | <= ) <expr3> ]
    // 12th
    expr3();

    if(getCurrentToken() == GT || getCurrentToken() == LT || getCurrentToken() == GE || getCurrentToken() == LE ){
        nextToken();
        expr3();
    }

    // switch (getCurrentToken()) {

    //     case GT:
    //     case LT:
    //     case GE:
    //     case LE: 
    //         nextToken();
    //         expr3();
    //   default:
    // }

    //end of 12th

  

    TJ.output.decTreeDepth();
  }


  private static void expr3() throws SourceFileErrorException
  {
    TJ.output.printSymbol(NTexpr3);
    TJ.output.incTreeDepth();

    /* ???????? */
    // <expr3> ::= <expr2> { ( + | - ) <expr2> }
    // 13th

    expr2();

    while (getCurrentToken() == PLUS || getCurrentToken() == MINUS) {
        nextToken();
        expr2();
    }

    // END of 13th

    TJ.output.decTreeDepth();
  }


  private static void expr2() throws SourceFileErrorException
  {
    TJ.output.printSymbol(NTexpr2);
    TJ.output.incTreeDepth();

    expr1();

    while (   getCurrentToken() == TIMES
           || getCurrentToken() == DIV
           || getCurrentToken() == MOD) {

      nextToken();

      expr1();
    }

    TJ.output.decTreeDepth();
  }


  private static void expr1() throws SourceFileErrorException
  {
    TJ.output.printSymbol(NTexpr1);
    TJ.output.incTreeDepth();

    switch (getCurrentToken()) {

      /* ????????*/
      // <expr1> ::= '(' <expr7> ')' | ( + | - | ! ) <expr1> | UNSIGNEDINT | null |
      //             new int '[' <expr3> ']' { '[' ']' } |
      //             IDENTIFIER ( . nextInt '(' ')' | [ <argumentList> ] { '[' <expr3> ']' } )
      // START MODIFICATION

      //14th
      case LPAREN:
          nextToken();
          expr7();
          accept(RPAREN);
          break;

      case PLUS:
          nextToken();
          expr1();
          break;

      case MINUS:
          nextToken();
          expr1();
          break;

      case NOT:
          nextToken();
          expr1();
          break;

      case UNSIGNEDINT:
          nextToken();
          break;

      case NULL:
          nextToken();
          break;

      case NEW:
          nextToken();
          accept(INT);
          accept(LBRACKET);
          expr3();
          accept(RBRACKET);
          while(getCurrentToken()==LBRACKET){
              nextToken();
              accept(RBRACKET);
          }
          break;

      case IDENT:
          nextToken();
          if(getCurrentToken()!= DOT){
              if(getCurrentToken() == LPAREN){
                  nextToken();
                  argumentList();
                  accept(RPAREN);
              }
              while(getCurrentToken() == LBRACKET){
                  nextToken();
                  expr3();
                  accept(RBRACKET);
              }
          }
          else{
              nextToken();
              accept(NEXTINT);
              accept(LPAREN);
              accept(RPAREN);
          }
          break;


      //end of 14th

      default: throw new SourceFileErrorException("Malformed expression");

      
    }

    TJ.output.decTreeDepth();
  }
}


