from marshmallow import fields, validate
from blog.resources.validation.BaseValidation import BaseValidationSchema


class PostRequestSchema(BaseValidationSchema):
    contenuto = fields.String(
        required=True,
        error_messages=BaseValidationSchema.error_messages,
        validate=[
            validate.Length(min=6, error="Si prega di inserire una grandezza dal contenuto min. {min} caratteri.")
        ]
    )