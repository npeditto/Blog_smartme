class Singleton(type):
    """
        Creazione di una metaclasse, ovvero una classe che definisce il
        comportamento di un altra classe. Si parte creando una classe che
        derivi da type
    """

    # Variabile statica, mi permette di tracciare globalmente l'esistenza di una singola classe istanziata.
    __instances = {}

    def __call__(cls, *args, **kwargs):
        if cls not in cls.__instances:
            """
                Richiamo il magic method "__call__" della classe Singleton al fine di inizializzare l'oggetto della classe 
                che sta venendo istanziata. Passo tramite il metodo __call__ (chiamato prima di __init__ e __new__) 
                gli argomenti e le keyword arguments passate all'istanza della classe. e mi assicuro (tramite la condizione
                if che si assicura che vi sia un'unica istanza globale 
            """

            cls.__instances[cls] = super(Singleton, cls).__call__(*args, **kwargs)

        # Ritorno l'istanza che si trova nel dizionario.
        return cls.__instances[cls]

