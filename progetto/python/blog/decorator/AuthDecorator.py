import os
from functools import wraps
from flask import request, jsonify, make_response

import jwt

from blog.exception.TokenInvalidException import TokenInvalidException

from blog.models.User import User
from blog.models.Token import Token


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

            # Verifico se il token risulta valido controllando nel database se è presente e l'utente che l'ha richiesto corrisponde
            token = Token.query.filter_by(token=token).first()

            isValidTokenDB = token is not None and \
                             current_user.user_id == token.owner and \
                             token.revoked_date is None

            if not current_user or not isValidTokenDB:
                raise TokenInvalidException()

        except (TokenInvalidException, jwt.ExpiredSignatureError) as e:
            return make_response(jsonify({"error": "Token scaduto o non valido."}), 400)

        return func(current_user=current_user, *args, **kwargs)

    return wrapper
