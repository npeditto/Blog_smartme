import datetime
from marshmallow import fields, ValidationError, validate

from blog.resources.validation.LoginSchema import LoginSchema

def is_adult(date):
    """
    Funzione predicato che valuta se, tramite la data di nascita passata in input,
    l'utente risulta essere maggiorenne
    :param date: data nascita
    :return: None
    """
    today = datetime.date.today()
    age = today.year - date.year

    if age < 18:
        raise ValidationError("Data non valida, devi risultare più grande di 18 anni per poterti registrare.")


class RegisterSchema(LoginSchema):
    nome = fields.String(
        required=True,
        error_messages=LoginSchema.error_messages,
        validate=[
            validate.Length(min=1, max=255, error="Il campo nome deve essere di lunghezza compresa tra {min} e {max}"),
        ]
    )

    cognome = fields.String(
        required=True,
        error_messages=LoginSchema.error_messages,
        validate=[
            validate.Length(
                min=1, max=255,
                error="Il campo cognome deve essere di lunghezza compresa tra {min} e {max}"),
        ]
    )

    data_nascita = fields.Date(
        required=True,
        error_messages=LoginSchema.error_messages,
        validate=[is_adult]
    )
