from blog.hateoas.Marshmallow import ma
from marshmallow import post_dump
from os import environ


class BaseSchema(ma.Schema):
    """
    Classe impiegata per la costruzione di schemi generali utilizzati per la rappresentazione del modello.
    """

    @post_dump
    def transformURI(self, data, **kwargs):
        """
        Fase prima della trasformazione di un'entità, viene richiamata
        transformURI al fine di aggiungere il protocollo HTTP e l'HOST
        ai link dell'HATEOAS
        """
        links : dict = data["_links"]

        for rel, link in links.items():
            links[rel] = environ.get("HTTP_PROTOCOL").lower() + "://" + environ.get("HOST") + link

        return data