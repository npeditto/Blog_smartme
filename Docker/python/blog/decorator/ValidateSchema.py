from functools import wraps
from flask import request, jsonify, make_response
from marshmallow import ValidationError
from marshmallow import EXCLUDE

def ValidateSchema(schema):
    """
    Essendo che un decoratore è una funzione che prende in input un altra funzione,
    viene creato un decoratore che prenda in input uno Schema di validazione e mi restituisca un
    decoratore per validare quello schema tramite il tipico meccanismo dei decoratori

    :param schema: Schema di validazione
    :return: Decoratore per validare una richiesta
    """

    def validate_schema_outer(fn):
        @wraps(fn)
        def wrapped(*args, **kwargs):
            instance = schema()
            data = request.json

            try:
                instance.load(data, unknown=EXCLUDE)
            except ValidationError as e:
                return make_response(jsonify({"error": e.messages}), 400)

            return fn(*args, **kwargs)

        return wrapped

    return validate_schema_outer
