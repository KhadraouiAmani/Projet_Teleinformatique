In this file we are just explaining what is happening with errors/checksums.

Premièrement: Du Coté Client

    Pour l'opérateur: (+ - * /):
        On introduit deux bit de parité (si nombre de bits égal à 1 est pair --> on ajoute 00 si non 11)

    Pour les opérandes:  
        On ajoute des checksums pour chaque opérande: prenons l'exemple 7 (111 en binaire)
            1- on ajoute un 0 pour garantir parité du nombre de bits --> 0111
            2- somme = 0
            3- deuxbits = 0 + 1 (char[0] + char[1] = 0 + 1) = 01 (c'est une chaine)
            4- conversion de deuxBits en decimal ==> 01 devient 1 donc somme = 1
            5- puis de meme pour les 2 autres bits restants: deuxBits = 1 + 1 = 11 --> 3 ==> somme = 1 + 3 = 4
            6- somme = somme & 0x03 --> on garde les deux derniers bits de somme = 4 = 100 --> somme = 00 = 0
            7- complement à 2 de somme = 11 = 3 = checksum
            8- on renvoit donc checksum = 11 

    L'opération finale devient: [opérateur+BitsParite, operand1Binary+checksum1, operand2Binary+checksum2, result=""]

Deuxièment: Through The Network:
    On introduit une erreur dans un champs choisi au hasard sans bien sur toucher au checksum ni aux bits de parité (pour le cas d'une erreur introduite à l'opérateur)

Troisiemment: Du Coté Serveur   
    1- Le serveur teste les bits de parité (pour l'opérateur) et le checksum (pour les opérandes) pour vérifier si une erreur etait introduite ou pas.
    2- Si on a introduit une erreur, on mentionne à quel niveau (opérateur ou premier opérande ou deuxième opérande) et on affiche l'opération totale souhaitée (qui peut 
    ainsi etre totalement différente en cas d'erreur)
    3- Dans tous les cas, on procède au calcul du résultat (en binaire).
    4- Le résultat sera envoyé au client en binaire.

Finalement: 
    Le client affiche le résultat reçu en décimal.