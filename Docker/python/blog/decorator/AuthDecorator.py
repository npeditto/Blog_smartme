import os
from functools import wraps
from flask import request, jsonify

import jwt

from blog.exception.TokenInvalidException import TokenInvalidException
from blog.models.User import User


def authenticated(func):
    """
    Utilizzo il decoratore wraps in maniera tale che si copiano i metadati della funzione
    originale sulla funzione decorata (essendo che altrimenti non avrebbe gli stessi dati
    """

    @wraps(func)
    def wrapper(*args, **kwargs):
        token = None

        try:
            # Controllo se l'utente stia cercando di autenticarsi, se si ne prendo il token
            if "Authorization" in request.headers:
                token = request.headers.get('Authorization')

            # Se non è un bearer, allora errore.
            if not token or not token.startswith("Bearer "):
                raise TokenInvalidException()

            token = token.replace("Bearer ", "")

            # Provo a decodificare il token, se non si riesce allora errore!
            data = jwt.decode(token, os.environ.get("SECRET_KEY"), algorithms=["HS256"])

            # Prelevo l'utente che ha tentato l'autenticazione, in questo modo posso fare operazioni "protette"
            current_user = User.query.filter_by(public_id=data["sub"]).first()

            if not current_user:
                raise TokenInvalidException()
        except Exception as e:
            raise TokenInvalidException()

        return func(current_user=current_user, *args, **kwargs)

    return wrapper
