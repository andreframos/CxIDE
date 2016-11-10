%Criar menu com o titulo "NomeMenu"
create_menu(NomeMenu):-java_call('org/eclipse/cxide/Menu_ops/Menu_Operations','createMenu:(Ljava/lang/String;)V',[NomeMenu],_).

%Criar menu com o titulo "NomeMenu" que executa "PrologCmd"
%create_menu(NomeMenu,PrologCmd):-java_call('org/eclipse/cxide/Menu_ops/Menu_Operations','createMenu:(Ljava/lang/String;Ljava/lang/String;)V',[NomeMenu,PrologCmd],_).

%Criar menu com o titulo "NomeMenu" e com os items definidos na "Lista"
create_menu(NomeMenu,Lista):-java_call('org/eclipse/cxide/Menu_ops/Menu_Operations', 'createMenu:(Ljava/lang/String;[Ljava/lang/String;)V', [NomeMenu,Lista], R).

%Criar menu com o titulo "NomeMenu" com um único item com nome "Item_Name" e que executa a acao "Action"
create_menu(NomeMenu,Item_Name,Action):-java_call('org/eclipse/cxide/Menu_ops/Menu_Operations','createMenu:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V',[NomeMenu,Item_Name,Action],_).

%Elimina o menu com o id "Menu_ID", se for um menu criado pelo utilizador todos os seus sub-menus tambem sao eliminados
%Caso contrário os sub-menus continuarão a existir
delete_menu(Menu_ID):-java_call('org/eclipse/cxide/Menu_ops/Menu_Operations','deleteMenu:(Ljava/lang/String;)V',[Menu_ID],_).

%Criar e guarda uma janela de dialogo com o titulo "Titulo" sem output
create_dialog(Titulo):-java_call('org/eclipse/cxide/Menu_ops/Dialog_Operations','createDialog:(Ljava/lang/String;)V',[Titulo],_).

%Criar e guarda uma janela de dialogo com o titulo "Titulo" com a possibilidade de output
create_dialog(Titulo, Feedback):-java_call('org/eclipse/cxide/Menu_ops/Dialog_Operations','createDialog:(Ljava/lang/String;Ljava/lang/Boolean;)V',[Titulo,Feedback],_).

%Exibe uma janela de dialogo com o titulo "Titulo" que tenha sido posteriormente criada pelo utilizador.
show_dialog(Titulo):-java_call('org/eclipse/cxide/Menu_ops/Dialog_Operations','showDialog:(Ljava/lang/String;)V',[Titulo],_).

listMenus:-java_call('org/eclipse/cxide/Menu_ops/Menu_Operations','printMenusCreated:()V',[],_).

%Adiciona um campo de texto com a Label ao dialog Titulo
add_textForm_dialog(Titulo,Label):-java_call('org/eclipse/cxide/Menu_ops/Dialog_Operations','addTextFiel_dialog:(Ljava/lang/String;Ljava/lang/String;)V',[Titulo,Label],_).

%Adiciona um campo de numero com a Label ao dialog Titulo
add_numForm_dialog(Titulo,Label):-java_call('org/eclipse/cxide/Menu_ops/Dialog_Operations','addNumTextFiel_dialog:(Ljava/lang/String;Ljava/lang/String;)V',[Titulo,Label],_).


getTextField(Dialog,Field_Label,Value):-java_call('org/eclipse/cxide/Menu_ops/Dialog_Operations','dialog_getTextField:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;',[Dialog,Field_Label],Value),writeln(Value).
getNumField(Dialog,Field_Label,N):-java_call('org/eclipse/cxide/Menu_ops/Dialog_Operations','dialog_getNumField:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;',[Dialog,Field_Label],V),atom_codes(V,L),number_codes(N,L), writeln(N).
%getNumField(Dialog_Id,Field_Label,Value):-java_call('com/simple/plugin/Menu_ops/Dialog_Operations','dialog_getNumField:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;',[Dialog_Id,Field_Label],X),java_convert('Ljava/lang/String;', X, V),atom_codes(V,L),number_codes(N,L), Value is N+N, writeln(Value).

dialog_setOutput(Dialog,[Head|Tail]):-java_call('org/eclipse/cxide/Menu_ops/Dialog_Operations','setOutput:(Ljava/lang/String;[Ljava/lang/Object;)V',[Dialog,[Head|Tail]],_).

dialog_setAction(Dialog,Action):-java_call('org/eclipse/cxide/Menu_ops/Dialog_Operations','dialog_setAction:(Ljava/lang/String;Ljava/lang/String;)V',[Dialog,Action],_).

%mudar Value para Path
file_chooser(FilePath):-java_call('org/eclipse/cxide/Menu_ops/FileChooser_Operations','FileChooseDialog:()Ljava/lang/String;',[],FilePath).
dir_chooser(Value):-java_call('org/eclipse/cxide/Menu_ops/FileChooser_Operations','DirChooseDialog:()Ljava/lang/String;',[],Value),writeln(Value).
fd_chooser(Value):-java_call('org/eclipse/cxide/Menu_ops/FileChooser_Operations','FileDirChooseDialog:()Ljava/lang/String;',[],Value),writeln(Value).

%abrir ficheiro externo no editor
open_file(Path):-java_call('org/eclipse/cxide/Menu_ops/FileChooser_Operations','open_external_file_onEditor:(Ljava/lang/String;)V',[Path],_).

%Add new items to the cxProlog context menu
add_contextItem(MenuName,CMD):-java_call('org/eclipse/cxide/Menu_ops/EditorContextMenu','add_item_editor:(Ljava/lang/String;Ljava/lang/String;)V',[MenuName,CMD],_).

%Get current selected text in CxProlog Editor
selectedText(Text):-java_call('org/eclipse/cxide/Menu_ops/Editor_Operations','getSelectedText:()Ljava/lang/String;',[],X),java_convert('Ljava/lang/String;', X, Text),writeln(Text).

%injects code in console

%getFunctors([X|Xs],[Fun,Ar|Ys]) :- functor(X, Fun, Ar), getFunctors(Xs,Ys).
%----------------------

%Obtem a lista de predicados com a dada propriedade 
getPredicates(B,L):-findall(A,predicate_property(A,B),L).

%recebe uma lista de predicados e retorna a lista com os seus functors
getFunctors([],[]).
getFunctors([X|Xs],[Fun|Ys]) :- functor(X, Fun, Ar), getFunctors(Xs,Ys).

%obtem a lista de funtores de todos os predicados com a propriedade Pro
getPredicatesWithProperty(Pro,Pre):-getPredicates(Pro,L),getFunctors(L,Pre).

%----------------------

editor_singleLineRule(FieldName, StartSeq, EndSeq, EscChar, Red, Green, Blue):-java_call('org/eclipse/cxide/utilities/Editor_Utilities','singleLineRule:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;III)V',[FieldName,StartSeq, EndSeq, EscChar, Red, Green, Blue],_).

editor_endLineRule(FieldName, StartSeq,Red,Green,Blue):-java_call('org/eclipse/cxide/utilities/Editor_Utilities','endOfLineRule:(Ljava/lang/String;Ljava/lang/String;III)V',[FieldName, StartSeq, Red, Green, Blue],_).

editor_varRule(FieldName,R,G,B):-java_call('org/eclipse/cxide/utilities/Editor_Utilities','addVarRule:(Ljava/lang/String;III)V',[FieldName,R, G, B],_).

editor_addNormalTextRule(FieldName,Red,Green,Blue):-java_call('org/eclipse/cxide/utilities/Editor_Utilities','addDefaultTextRule:(Ljava/lang/String;III)V',[FieldName, Red, Green, Blue],_).

editor_addWordRule(FieldName,Word,Red,Green,Blue):-java_call('org/eclipse/cxide/utilities/Editor_Utilities','addWordRule:(Ljava/lang/String;Ljava/lang/String;III)V',[FieldName,Word,Red, Green, Blue],_).
editor_addWordsRule(FieldName,[Head|Tail],R, G, B):-editor_addWordRule(FieldName,Head, R, G, B),editor_addWordsRule(FieldName,Tail, R, G, B).

editor_addPropertyHighlight(Pro,R,G,B):-getPredicatesWithProperty(Pro,List),getFunctors(List,Functors),editor_addWordsRule(Pro,Functors, R, G, B).

editor_getCurrOpenFilePath(Path):-java_call('org/eclipse/cxide/Menu_ops/Editor_Operations','getCurrentEditorFilePath:()Ljava/lang/String;',[],Path).

editor_getAllOpenFilePaths(FilePaths):-java_call('org/eclipse/cxide/Menu_ops/Editor_Operations','getCurrentEditorAllFilesPath:()[Ljava/lang/String;',[], X), java_convert('[Ljava/lang/String;', X, FilePaths).

editor_getCurrentContent(Content):-java_call('org/eclipse/cxide/Menu_ops/Editor_Operations','getCurrentEditorContent:()Ljava/lang/String;',[], Content).

editor_getCodeErrors:-editor_getCurrentContent(Code),code_summary(Code,Summary,Errors),java_call('org/eclipse/cxide/Menu_ops/Editor_Operations','errors_Folding_Outline:([Ljava/lang/Object;[Ljava/lang/Object;)V',[Summary,Errors],_).

editor_injectSelCode:-java_call('org/eclipse/cxide/Menu_ops/Editor_Operations','injectSelCodeConsole:()V',[], _).
%
getBuiltins(Lista):-getPredicatesWithProperty('built_in',Lista),writeln(Lista),java_call('org/eclipse/cxide/utilities/Editor_Utilities','setBuiltins:([Ljava/lang/String;)V',[Lista],_).
%user_defined
getUserDefined(Lista):-getPredicatesWithProperty('user_defined',Lista),writeln(Lista),java_call('org/eclipse/cxide/utilities/Editor_Utilities','setUserDefined:([Ljava/lang/String;)V',[Lista],_).

editor_setContentAssistFile(FilePath):-java_call('org/eclipse/cxide/utilities/Editor_Utilities','setContentAssistFile:(Ljava/lang/String;)V',[FilePath],_).

consultAll([Head|Tail]):-consult(Head),consultAll(Tail).

%consultAll([Head|Tail]):-writeln([Head|Tail]).%,consult([Head|Tail]).

teste(Texto):-java_call('org/eclipse/cxide/Menu_ops/EditorContextMenu','add_item_editor:(Ljava/lang/String;)V',[Texto],_).

%Java sends a list with all the predicates encountered and info about their positions
%Cx process this list, extracting the functor and the args of the predicates and sends this info back to Java
%This is used for the OutlineView
getOutlineInfo([]):-true.

getOutlineInfo([Head,Start,Offset|Tail]):-atom_termq(Head,Term),functor(Term,Fun,Ar),extractArgs(Term,Ar,Args),java_call('org/eclipse/cxide/Menu_ops/Editor_Operations','addFileFunctor:(Ljava/lang/String;III)V',[Fun,Ar,Start,Offset],_),getOutlineInfo(Tail).
getOutlineInfo([Head,Start,Offset|Tail]):-getOutlineInfo(Tail).

%It was necessary to sometimes convert terms to their textual representation
%Because the parser would not allow the process of some terms otherwise.
atomsToTexts([],[]).
atomsToTexts([Atom|Atoms],[Text|Texts]):-atom_term(Text,Atom),atomsToTexts(Atoms,Texts).
%Extract arguments of a given term.
extractArgs(Term,Ar,List):-getArgs1(Term, Ar,List),atomsToTexts(List,L2),java_call('org/eclipse/cxide/Menu_ops/Editor_Operations','addFileArg:([Ljava/lang/String;)V',[L2],_).
getArgs1(Term, 0,[]).
getArgs1(Term, Arity,L):-
arg(Arity,Term,Arg),
	Ari is Arity-1,
	getArgs1(Term, Ari,P), app([Arg],P,L).
%%%%%%%%%%%%%%%%%

	