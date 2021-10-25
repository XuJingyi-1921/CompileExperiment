# CompileExperiment

-- Part 3 实现正号、负号
在 Part 3 中，你的编译器需要支持正号、负号以及表达式中可能存在的括号。

你需要支持的语法规则如下（以 CompUnit 为开始符号）：

CompUnit   -> FuncDef
FuncDef    -> FuncType Ident '(' ')' Block
FuncType   -> 'int'
Ident      -> 'main'
Block      -> '{' Stmt '}'
Stmt       -> 'return' Exp ';'
Exp        -> AddExp
AddExp     -> MulExp
MulExp     -> UnaryExp
UnaryExp   -> PrimaryExp | UnaryOp UnaryExp
PrimaryExp -> '(' Exp ')' | Number
UnaryOp    -> '+' | '-'



-- Part 4 实现四则运算及模运算
在 Part 4 中，你的编译器需要实现四则运算以及模运算。

miniSysY 算符的优先级与结合性与 C 语言一致，文法中已经体现出了优先级定义，同一优先级的运算符运算顺序为从左到右运算。

如果你打算手工实现编译器，并且采用了递归下降的语法分析方式，在分析四则运算相关的语法时局部采用算符优先分析法可能效果会更好。
你需要支持的语法规则如下（以 CompUnit 为开始符号）：

CompUnit   -> FuncDef
FuncDef    -> FuncType Ident '(' ')' Block
FuncType   -> 'int'
Ident      -> 'main'
Block      -> '{' Stmt '}'
Stmt       -> 'return' Exp ';'
Exp        -> AddExp
AddExp     -> MulExp 
              | AddExp ('+' | '−') MulExp
MulExp     -> UnaryExp
              | MulExp ('*' | '/' | '%') UnaryExp
UnaryExp   -> PrimaryExp | UnaryOp UnaryExp
PrimaryExp -> '(' Exp ')' | Number
UnaryOp    -> '+' | '-'

其中除法取整和模运算规则与 C 语言 int 类型相同。
