Tema 2 Paradigme de Programare
==============================

## 1. Descrierea scheletului de cod
	- FlowLike.java
		- interfata ce ofera suport pentru intersectie, subset, rewrite pentru flow-uri si pentru cflow-uri
		- trebuie implementata deepClone() atat pentru compact flow-uri, cat si pentru flow-uri
		  pentru ca testele se asteapta ca intersect si rewrite sa nu modifice obiectul
		  asupra caruia se aplica modificarile, ci sa creeze unul nou (clonat puternic,
		  pentru a nu exista referinte din cea veche)
		- acest fisier NU trebuie modificat

	- TestCompactFlow.java
		- fisierul de testare pentru CompactFlow.java
		- acest fisier o sa fie folosit si in punctarea temei (nu vor exista alte teste)
		- pentru obtinerea unui punctaj partial (teme incomplete):
			- fisierul TREBUIE sa compileze
			- punctajul este cel returnat de score
			- modificarea testelor astfel incat sa treaca atrage punctaj 0 pe intreaga tema

	- TestFlow.java
		- fisierul de testare pentru Flow.java
		- inainte de a rula testele pentru Flow.hs, trebuie implementata functionalitatea
		pentru CompactFlow
		- acest fisier o sa fie folosit si in punctarea temei (nu vor exista alte teste)
		- pentru obtinerea unui punctaj partial (teme incomplete):
			- fisierul TREBUIE sa compileze
			- punctajul este cel returnat de score
			- modificarea testelor astfel incat sa treaca atrage punctaj 0 pe intreaga tema

	- TestReachability.hs
		- fisierul de testare pentru Reachability.java
		- inainte de a rula testele pentru Reachability.hs, trebuie implementata functionalitatea
		pentru CompactFlow si Flow
		- acest fisier o sa fie folosit si in punctarea temei (nu vor exista alte teste)
		- pentru obtinerea unui punctaj partial (teme incomplete):
			- fisierul TREBUIE sa compileze
			- punctajul este cel returnat de score
			- modificarea testelor astfel incat sa treaca atrage punctaj 0 pe intreaga tema

	- CompactFlowNonVoid.java
		- aici se vor implementa metodele specifice FlowLike si a compararii a doua compact flow-uri, urmariti TODO-uri

	- FlowNonVoid.java
		- aici se vor implementa metodele specifice FlowLike si a compararii a doua flow-uri, urmariti TODO-uri

	- Reachability.java
		- aici va trebui implementata functia "reachability"

	- Dst.java, Src.java, Port.java, Header.java
		- utilizate pentru tipul de date Header echivalent din Haskell ; nu e nevoie sa modificati fisierele

	- Any.java, Null.java, StringAtom.java, Value.java
		- utliziate pentru tipul de date Value echivalent din Haskell
		- doar StringAtom necesita adaugari pentru compararea a doua StringAtom-uri, vedeti TODO-uri

	- Elements.java
		- defineste interfata pentru getMatchAndModify
		- echivalent cu class Elements din haskell

	- Network.java
		- parseaza un string de input si il transforma in mai multe elemente de retea precum
		  Wire (fir), Filter, Rewriter
		- vedeti exemple in cadrul fisierului

	- NetworkElement.java
		- incapsuleaza metodele Match si Modify ce trebuie definite (clasa e abstracta)
		  intr-un tip concret pentru a le putea folosi
		- contine si functia fuse, folosita deja (nu e nevoie s-o folositi voi) in Network.java

	- Wire.java, Filter.java, Rewriter.java
		- extind NetworkElement.java si ofera implementare pentru match si pentru modify

	- Visitable.java, Visitor.java
		- intefete folosite pentru a implementa un pattern Visitor
		- Reachability.java va implementa Visitor
		- Flow.java va implementa Visitable (va fi vizitat de Reachability)
		- cand flow-ul e vizitat de vizitator, ii sunt generati succesori flow-ului vizitat
		- apoi fiecare nou succesor va da la randul sau accept, procesul repetandu-se
		  pana cand "all" (retinem toate flow-urile vizitate) nu se mai modifica, sau pana
		  cand orice flow am vrea sa mai vizitam, vedem ca l-am vizitat deja (e in all).


## 2. Precizari generale

	- Tema vina ca un proiect gata sa fie inclus in Eclipse. Va recomandam sa folositi Eclipse pentru ea,
	  pentru ca suita de teste (unit tests cu JUnit) este foarte usor de rulat direct din Eclipse,
	  si mai complicat altfel.
	  Pentru a include tema in Eclipse:
		- File -> Import -> General -> Existing projects into Workspace[2].
		- Pentru a rula testele, deschideti fisierul de test (din pachetul tests.graded) si rulati-l din
		  meniu (Run -> Run) ; eventual selectati daca sunteti intrebati Run as JUnit Test.
		[2] http://help.eclipse.org/juno/index.jsp?topic=%2Forg.eclipse.platform.doc.user%2Ftasks%2Ftasks-importproject.htm
	- Orice modificare a claselor in care se specifica faptul ca nu trebuie modificate aduce punctaj 0.
	  Definitiile existente sunt suficiente pentru finalizarea temei, orice
	  adaugari/modificari atrag complicatii nenecesare.
	- Tema consta in efectuarea:
		- operatiilor pe Flow si pe CompactFlow (implementarea interfetei FlowLike)
		- deepClone este importanta pentru ca testele sa treaca, pentru ca se considera
		  ca intersect, subset si rewrite nu modifica flow-ul sau compact flow-ul initial, ci
		  creeaza unul nou
		- sa extindeti NetworkElement pentru Wire, Rewriter si Filter la Element
		- implementarii algoritmului "reachability" (conform enuntului si a pattern-ului visitor) 
	- Respectati declaratiile functiilor care trebuie implementate (tipurile acestora)
	  pentru a nu avea probleme la rularea testelor.
	- Orice implementare voit incorecta care aduce punctaj pe anumite teste atrage
	  punctaj nul pe intreaga tema. Nu rulati testele care testeaza functionalitate pe
	  care nu ati implementat-o.
	- Puteti folosi orice biblioteca sau feature al limbajului, nu exista restrictii.
	- Nu folositi `==` niciodata in comaratia obiectelor sau a String-urilor, ci obiect1.equals(obiect2)!
	- pentru a putea compara compact flow-uri si flow-uri cu .equals(), suprascrieti
	  atat hashCode() cat si equals() (override) din fiecare din ele. Vedeti [1] de ce e nevoie de amandoua.
	  [1] http://stackoverflow.com/questions/27581/overriding-equals-and-hashcode-in-java
	- Va incurajam sa cititi si sa intelegeti si fisierele in care am mentionat ca e nerecomandat sa le editati.
	  Este nerecomandata abordarea "caut unde sunt TODO-uri" si completez; daca tratati codul schelet ca un
	  black box, posibil ca codul scris de voi sa nu functioneze cum trebuie. Cu o privire de ansamblu insa,
	  nu aveti cum sa gresiti!
	  Fiindca aveti teste, este recomandat sa incepeti de la acelea citirea codului, puteti
	  folosi si debugging din Eclipse.

## 3. Trimitere si testare tema

	- Trebuie sa va asigurati inainte de deadline ca nu exista probleme de compilare.
	- Codul care nu compileaza NU este punctat.
	- Rularea testelor se va face din Eclipse, dand pe rand Run pe fisierele de test din pachetul tests.graded:
		- TestCompactFlow (20 teste - maxim 20p)
		- TestFlow (35 teste - maxim 35p)
		- TestReachability (9 teste - maxim 45p)
	- Documentarea codului se va face ad-hoc, prin comentarii - fisierele Readme sau
	  orice altceva, mai putin fisierele *.java nu sunt luate in considerare.
	- Punctajul maxim este de 100 de puncte - obtinut in intregime ca rezultat
	  al testerului.
	- Tema va fi trimisa sub forma unei arhive "zip" cu formatul:
	  nume_prenume_3XYCZ.zip
	  de exemplu: popovici_matei_321CB.zip sau popovici_matei_342C1.zip
	- Din cauza volumului, temele sunt corectate automat, daca nu se respecta
	  conventiile, studentul este responsabil.

## 4. Resurse

	- Tutorial JUnit http://www.vogella.com/tutorials/JUnit/article.html
	- Tutorial Debugging in Eclipse http://www.vogella.com/tutorials/EclipseDebugging/article.html
	- http://en.wikipedia.org/wiki/Visitor_pattern

