import importlib
import inspect
from os import listdir
from os.path import isfile, join, basename


def getClassesFromPath(path) -> list:
    """
    Funzione che, ricevuto in input un percorso, restituisce una lista delle classi contenute in quel percorso.

    :param path: Percoso da cui estrapolare i moduli
    :return: Lista delle classi contenute all'interno dei moduli di un percorso
    """

    # Lista dei moduli
    # Ottengo il path del modulo sostituendo / con . e unendo al nome del modulo trovato
    modules = [
        path.replace("/", ".") + f".{basename(f)[:-3]}"
        for f in listdir(path)
        if isfile(join(path, f)) and "__init__" not in f
    ]

    listClasses = []

    for module in modules:
        # Importo il modulo e lo salvo all'interno della variable mdl
        mdl = importlib.import_module(module)

        # Prendo tutte le classi del modulo, tramite il predicato inspect.isclass
        moduleClasses = inspect.getmembers(mdl, inspect.isclass)

        # Filtro che mi permette di ottenere tutte le classi presenti nel modulo
        filtClassesInSamePackage = filter(
            lambda cls: cls[1].__module__ == module, moduleClasses
        )

        # Per tutte le classi presenti nel modulo, prendine il path su cui devono essere mappate

        listClasses += list(filtClassesInSamePackage)

    return listClasses


def getCrudMethods(class_):
    # Lista metodi CRUD
    crudOps: list = ["get", "post", "put", "delete", "patch"]

    methodList = dir(class_)

    method_crud = []

    # Creami una lista di tutti i metodi crud contenuti nella classe con i riferimenti dei metodi,
    # se è decorata, allora prendi in riferimento la funzione decorata e non il decoratore.

    for method in methodList:
        if any(method in crud_op for crud_op in crudOps):
            methodRef = getattr(class_, method)

            """
            Fino a quando non trovo il metodo decorato, continuo nella discesa dei decoratori tramite l'attributo __wrapped__.
            """

            while hasattr(methodRef, "__wrapped__"):
                methodRef = methodRef.__wrapped__

            method_crud.append((method, methodRef))

    return method_crud


def getCrudMappingURI(path) -> dict:
    """
    Funzione utile per ottenere una lista di metodi CRUD da un insieme di moduli.
    Permette di ottenere tutti i metodi CRUD esposti da una classe (utile per fare una
    mappatura delle API RESTful)

    :param path: Percorso da cui estrapolare le classi, da cui verranno estratti i metodi
    :return: mappatura CRUD su diverse classi contenute nel path
    """

    resourcesMapURI: dict = {}

    # Lista delle classi dei moduli nel path
    listClasses: list = getClassesFromPath(path)

    for name, cls in listClasses:
        # Se la classe non esiste nel dizionario, inseriscilo come chiave associato ad una lista vuota in cui verranno inseriti tutti i metodi utilizzati
        if cls not in resourcesMapURI:
            resourcesMapURI[cls] = []

        crudMethods = getCrudMethods(cls)

        # Ottengo i parametri da passare all'URI per arrivare alla risorsa
        for method, methodRef in crudMethods:

            # Se la funzione è decorata, prendi il riferimento alla funzione decorata
            if hasattr(methodRef, "__wrapped__"):
                methodRef = methodRef.__wrapped__

            # Prendimi gli argomenti da passare alla funzione
            # Per ogni metodo infatti faccio la mappatura con l'URI corrispondente
            args = set(inspect.getfullargspec(methodRef).args) - {"self", "current_user"}
            args = list(args)
            URI = cls.path()

            # Riguarda.
            for argument in args:
                if "<" + argument + ">" not in URI:
                    URI += "/<%s>" % argument

            resourcesMapURI[cls].append((method, URI))

    return resourcesMapURI
