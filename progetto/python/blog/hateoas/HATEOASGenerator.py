from blog.hateoas.BaseSchema import BaseSchema
from blog.hateoas.Marshmallow import ma

from blog.utils.Helpers import getCrudMethods

import inspect


class SchemaGenerator:
    @classmethod
    def getCrudMethMapping(cls):
        # Metodi CRUD mappati sulle relazioni crud
        return {
            "get": "self",
            "post": "create",
            "put": "update",
            "delete": "delete",
            "patch": "patch"
        }

    @classmethod
    def getCrudMethods(cls, class_):
        crudOps = cls.getCrudMethMapping()

        # Mappa tra un il nome di metodo di istanza della classe e il suo riferimento
        crudMethods = getCrudMethods(class_)

        # Lista di tutti i metodi di istanza della classe con relative informazioni sui parametri e sulla tipologia di operazione CRUD
        method_list = {}

        for method, methodRef in crudMethods:
            # Prendo i parametri da passare alla richiesta escludendo il self (metodi di istanza)
            # Nel caso in cui fossero decorati, bisogna entrare all'interno dell'attributo __wrapped__ per prenderne gli argomenti da passare

            paramsList = inspect.getfullargspec(methodRef).args

            paramsList = list(set(paramsList) - {"self", "current_user"})

            # Creazione di un dizionario con la lista dei parametri da passare alla funzione
            # (secondo documentazione il valore va formattato con il nome della variabile tra parentesi angolari)
            paramsDict = {param: "<%s>" % param for param in paramsList}

            # Mappatura del metodo sulla relazione
            method_list[method] = {
                "rel": crudOps[method],
                "params": paramsDict
            }

        return method_list

    @classmethod
    def generate(cls, class_, resource):
        # Creazione class Meta che verrà impiegata per contenere una
        # serie di metadati legati all'oggetto, come il modello a cui
        # fa riferimento e i campi pubblici che potranno essere
        # restituiti come risorsa.

        class Meta(object):
            fields = class_.public_attribute

        # Generazione links
        links: dict = dict()

        methods_resource: dict = cls.getCrudMethods(resource)

        # Creazione dell'oggetto che verrà utilizzato per generare l'URL dell'HATEOAS
        for method, info in methods_resource.items():
            links[info["rel"]] = ma.URLFor(
                resource.__name__.lower() + "." + method, values=info["params"]
            )

        for rel, url in resource.relationships().items():
            links[rel] = url

        schema_class = type(
            class_.__name__ + "Schema",
            (BaseSchema,),
            {"Meta": Meta, "_links": ma.Hyperlinks(links)}
        )

        return schema_class
