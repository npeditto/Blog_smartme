from marshmallow import fields, validate
from blog.resources.validation.BaseValidation import BaseValidationSchema


class LoginSchema(BaseValidationSchema):
    error_messages = {
        "required" : "Il campo non risulta essere presente nella richiesta."
    }

    email = fields.Email(required=True, error_messages=error_messages)
    password = fields.String(
        required=True, error_messages=error_messages,
        validate=[
            validate.Length(min=8, max=255, error="La password deve essere di lunghezza compresa tra {min} e {max}"),
        ]
    )
