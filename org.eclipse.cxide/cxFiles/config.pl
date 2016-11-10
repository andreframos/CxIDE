%Este ficheiro permite definir a configuração inicial do CxIDE


% Predicados para a fórmula resolvente
delta(A, B, C, D):- D is B*B - 4*A*C.
equation(A,B,C,R1,R2):-delta(A,B,C,D),R1 is (-B+sqrt(D))/(2*A), R2 is (-B-sqrt(D))/(2*A).

% Criação de diálogo sobre a Fórmula resolvente
:-create_dialog('Fórmula Resolvente').
:-add_numForm_dialog('Fórmula Resolvente','a').
:-add_numForm_dialog('Fórmula Resolvente','b').
:-add_numForm_dialog('Fórmula Resolvente','c').
:-dialog_setAction('Fórmula Resolvente','getNumField(''Fórmula Resolvente'',''a'',A),getNumField(''Fórmula Resolvente'',''b'',B),getNumField(''Fórmula Resolvente'',''c'',C),equation(A,B,C,R1,R2),dialog_setOutput(''Fórmula Resolvente'',[R1,R2])').



% Criar menu com um item que evoca a fórmula resolvente
:-create_menu('Math Menu','Quadratic Equation','show_dialog(''Fórmula Resolvente'')').

:-create_dialog('Teste').
:-add_textForm_dialog('Teste','Msg').
:-dialog_setAction('Teste','getTextField(''Teste'',''Msg'',Output),writeln(Output),dialog_setOutput(''Teste'',[Output])').
:-create_menu('Math Menu','teste','writeln(''Começou''),show_dialog(''Teste''),writeln(''Acabou'')').

%Criar menu com um item que possibilita abrir um ficheiro externo no editor
:-create_menu('Cx File',['Open','file_chooser(Path),open_file(Path)','Consult','editor_getCurrOpenFilePath(Path),consult(Path)']).
%:-create_menu('Cx File','Open','file_chooser(Path),open_file(Path),'Consult','editor_getCurrOpenFilePath(Path),consult(Path)').
%:-create_menu('Cx File',['Open','file_chooser(Path),open_file(Path)','Consult','editor_getCurrOpenFilePath(Path),consult(Path)','Consult all','editor_getAllOpenFilePaths(Paths), consultAll(Paths)','Verify errors','editor_getCodeErrors']).
%Adicionar item CxProlog no ContextMenu do editor CxProlog com alguns sub-itens
%:-add_contextItem('Teste1','writeln(''Teste1 OK'')').
%:-add_contextItem('Selected Text','selectedText(Text)').

:-add_contextItem('Inject selected text','editor_injectSelCode').

%Definir regras para syntax highlight NULL POINTER EXCEPTION
%Definir texto entre " como um certo highlight
:-editor_singleLineRule('word "','"', '"', '\\', 100, 100, 100).
:-editor_singleLineRule('word ''','''', '''', '\\', 50, 50, 50).
:-editor_endLineRule('comment %','%', 150, 0, 150).
:-editor_varRule('variables',0,255,0).
:-editor_addNormalTextRule('default text',0,0,0).
%:-editor_addWordRule('special','andre',200,200,0).
:-editor_addPropertyHighlight('built_in',0,0,255).
:-editor_addPropertyHighlight('user_defined',0,255,0).

:-editor_setContentAssistFile('/src/builts.xml').
:-getUserDefined(_).


