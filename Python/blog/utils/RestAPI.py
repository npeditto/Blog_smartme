from blog.utils.Helpers import getCrudMappingURI, getClassesFromPath
from flask_restful import Api


class RestAPI:
    def __init__(self, app):
        errors = self.getExceptions()
        self.api = Api(app, errors=errors)

    def getExceptions(self):
        """
        Funzione che mi permette di ottenere un dizionario con tutte le eccezioni
        utilizzabili dalle mie risorse. In questo modo mi basterà creare una eccezione,
        lanciarla dalla risorsa per fornire un messaggio custom ed un codice associato.
        Il tutto è stato fatto secondo documentazione flask_restful che specifica
        che le eccezioni devono derivare da HTTPException.
        :return:
        """
        exceptions = getClassesFromPath("blog/exception")
        errors = {}

        for exception, exceptionClass in exceptions:
            obj = exceptionClass()
            """
                Prendo dall'oggetto gli attributi status e message che
                verranno restituiti come risposta alla richiesta che
                ha scatenato il lancio dell'errore.
            """

            errors[exception] = {
                "message": obj.message,
                "status": obj.status
            }

        return errors

    @classmethod
    def __getClasses(cls, path) -> dict:
        return getCrudMappingURI(path)

    def loadURIs(self):
        path = "blog/resources"
        mapUri: dict = self.__getClasses(path)

        # Effettuo il mapping aggiungendo le varie risorse
        for cls, URIs in mapUri.items():
            for method, URI in URIs:
                self.api.add_resource(
                    cls,
                    "/api/v1" + URI,
                    endpoint=cls.__name__.lower() + "." + method.lower()
                )
